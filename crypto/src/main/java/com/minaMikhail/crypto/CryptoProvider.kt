package com.minaMikhail.crypto

import android.content.Context
import android.security.keystore.KeyPermanentlyInvalidatedException
import com.minaMikhail.crypto.exceptions.BiometricChangedException
import com.minaMikhail.crypto.exceptions.BiometricDisabledException
import com.minaMikhail.crypto.exceptions.NotAuthenticatedException
import com.minaMikhail.crypto.exceptions.NotSecuredDeviceException
import com.minaMikhail.crypto.models.CryptoResult
import com.minaMikhail.crypto.utils.CryptoUtils.canAuthenticate
import com.minaMikhail.crypto.utils.CryptoUtils.createCipherForDecryption
import com.minaMikhail.crypto.utils.CryptoUtils.createCipherForEncryption
import com.minaMikhail.crypto.utils.CryptoUtils.deleteKey
import com.minaMikhail.crypto.utils.CryptoUtils.isDeviceSecure
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.inject.Inject

class CryptoProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val keyStore: KeyStore
) : ICryptoProvider {

    override fun getCipherForEncryption(
        keyName: String,
        requireBiometricAuthentication: Boolean
    ): Cipher {
        return if (requireBiometricAuthentication) {
            if (!isDeviceSecure(context)) throw NotSecuredDeviceException()

            if (!canAuthenticate(context)) throw BiometricDisabledException()

            try {
                createCipherForEncryption(
                    keyStore,
                    keyName,
                    true
                )
            } catch (ex: Exception) {
                if (ex is KeyPermanentlyInvalidatedException) {
                    deleteKey(keyStore, keyName)

                    throw BiometricChangedException()
                }

                throw ex
            }
        } else {
            createCipherForEncryption(
                keyStore,
                keyName,
                false
            )
        }
    }

    override fun getCipherForDecryption(
        keyName: String,
        initializationVector: ByteArray?,
        requireBiometricAuthentication: Boolean
    ): Cipher {
        return if (requireBiometricAuthentication) {
            if (!isDeviceSecure(context)) throw NotSecuredDeviceException()

            if (!canAuthenticate(context)) throw BiometricDisabledException()

            try {
                createCipherForDecryption(
                    keyStore,
                    keyName,
                    initializationVector,
                    true
                )
            } catch (ex: Exception) {
                if (ex is KeyPermanentlyInvalidatedException) {
                    deleteKey(keyStore, keyName)

                    throw BiometricChangedException()
                }

                throw ex
            }
        } else {
            createCipherForDecryption(
                keyStore,
                keyName,
                initializationVector,
                false
            )
        }
    }

    override fun encryptData(
        encryptCipher: Cipher,
        dataToEncrypt: String
    ): CryptoResult {
        try {
            val encryptedBytes = encryptCipher.doFinal(dataToEncrypt.encodeToByteArray())

            return CryptoResult(
                initializationVector = encryptCipher.iv,
                encryptedBytes = encryptedBytes,
                encryptedData = encryptedBytes.decodeToString()
            )
        } catch (ex: Exception) {
            if (ex is IllegalBlockSizeException) throw NotAuthenticatedException()

            throw ex
        }
    }

    override fun decryptData(
        keyName: String,
        decryptCipher: Cipher,
        encryptedBytes: ByteArray?
    ): String {
        try {
            return decryptCipher.doFinal(encryptedBytes).decodeToString()
        } catch (ex: Exception) {
            when (ex) {
                is IllegalBlockSizeException -> throw NotAuthenticatedException()
                is BadPaddingException -> {
                    deleteKey(keyStore, keyName)

                    throw BiometricChangedException()
                }

                else -> throw ex
            }
        }
    }
}