package com.minaMikhail.biometricWithCrypto.crypto

import android.content.Context
import com.minaMikhail.biometricWithCrypto.crypto.exceptions.BiometricDisabledException
import com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotAuthenticatedException
import com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotSecuredDeviceException
import com.minaMikhail.biometricWithCrypto.crypto.models.CryptoResult
import com.minaMikhail.biometricWithCrypto.crypto.utils.CryptoUtils.canAuthenticate
import com.minaMikhail.biometricWithCrypto.crypto.utils.CryptoUtils.createCipherForDecryption
import com.minaMikhail.biometricWithCrypto.crypto.utils.CryptoUtils.createCipherForEncryption
import com.minaMikhail.biometricWithCrypto.crypto.utils.CryptoUtils.isDeviceSecure
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
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

            createCipherForEncryption(
                keyStore,
                keyName,
                true
            )
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

            createCipherForDecryption(
                keyStore,
                keyName,
                initializationVector,
                true
            )
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
        decryptCipher: Cipher,
        encryptedBytes: ByteArray?
    ): String = decryptCipher.doFinal(encryptedBytes).decodeToString()
}