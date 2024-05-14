package com.minaMikhail.biometricWithCrypto.crypto.utils

import android.app.KeyguardManager
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object CryptoUtils {

    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    /**
     * - Returns whether the device is secured with a Pattern, PIN or password.
     *
     * @return `true` if a PIN, pattern or password was set.
     */
    internal fun isDeviceSecure(context: Context): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isDeviceSecure
    }

    /**
     * - Returns whether the device is enabled biometrics or not.
     *
     * @return `true` if biometric authentication enabled in device settings.
     */
    internal fun canAuthenticate(context: Context): Boolean = BiometricManager.from(context)
        .canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS

    /**
     * - Returns a `Cipher` object that can be used for encryption.
     *
     * - This method used to create a `Cipher` object used during encryption process only.
     *
     * @param keyStore the key store file.
     * @param keyName name of the key to use for encryption.
     * @param requireBiometricAuthentication used in case of biometric authentication.
     *
     *
     * @return [Cipher] that implements the requested transformation.
     */
    internal fun createCipherForEncryption(
        keyStore: KeyStore,
        keyName: String,
        requireBiometricAuthentication: Boolean
    ): Cipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(
            Cipher.ENCRYPT_MODE,
            getOrCreateSecretKey(keyStore, keyName, requireBiometricAuthentication)
        )
    }

    /**
     * - Returns a `Cipher` object that can be used for decryption.
     *
     * - This method used to create a `Cipher` object used during decryption process only.
     *
     * @param keyStore the key store file.
     * @param keyName name of the key to use for decryption.
     * @param initializationVector which you got after encryption process
     * , you need to use it for decryption.
     * @param requireBiometricAuthentication used in case of biometric authentication.
     *
     *
     * @return [Cipher] that implements the requested transformation.
     */
    internal fun createCipherForDecryption(
        keyStore: KeyStore,
        keyName: String,
        initializationVector: ByteArray?,
        requireBiometricAuthentication: Boolean
    ): Cipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(keyStore, keyName, requireBiometricAuthentication),
            IvParameterSpec(initializationVector)
        )
    }

    /**
     * - Returns a `SecretKey` object that is used to generate a Cipher object.
     *
     * - This method used to create or retrieve a `SecretKey` object if created before.
     *
     * @param keyStore name of the key store file.
     * @param keyName name of the key to use for encryption and decryption.
     * @param requireBiometricAuthentication used in case of biometric authentication.
     *
     *
     * @return [SecretKey] object.
     */
    private fun getOrCreateSecretKey(
        keyStore: KeyStore,
        keyName: String,
        requireBiometricAuthentication: Boolean
    ): SecretKey {
        val existingKey = keyStore.getEntry(keyName, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey(keyName, requireBiometricAuthentication)
    }

    /**
     * - Returns a `SecretKey` object that is used to generate a Cipher object.
     *
     * - This method used to create a new `SecretKey` object.
     *
     * @param keyName name of the key to use for encryption and decryption.
     * @param requireBiometricAuthentication used in case of biometric authentication.
     *
     *
     * @return [SecretKey] object.
     */
    private fun createKey(
        keyName: String,
        requireBiometricAuthentication: Boolean
    ): SecretKey = KeyGenerator.getInstance(ALGORITHM).apply {
        init(
            KeyGenParameterSpec.Builder(
                keyName,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(BLOCK_MODE)
                .setEncryptionPaddings(PADDING)
                .setUserAuthenticationRequired(requireBiometricAuthentication)
                .setRandomizedEncryptionRequired(true)
                .build()
        )
    }.generateKey()
}