package com.minaMikhail.biometricWithCrypto.crypto

import com.minaMikhail.biometricWithCrypto.crypto.models.CryptoResult
import javax.crypto.Cipher

interface ICryptoProvider {

    /**
     * - Returns a `Cipher` object that can be used for encryption.
     *
     * - This method used to create a cipher object used during encryption process only.
     *
     * @param keyName name of the key to use for encryption.
     * @param requireBiometricAuthentication used in case of biometric authentication.
     *
     *
     * @return [Cipher] that implements the requested transformation.
     *
     *
     * @exception [com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotSecuredDeviceException] if
     * device not secured with Pattern, PIN or Password.
     * @exception [com.minaMikhail.biometricWithCrypto.crypto.exceptions.BiometricDisabledException] if
     * biometric authentication is disabled in device settings.
     */
    fun getCipherForEncryption(
        keyName: String,
        requireBiometricAuthentication: Boolean = false
    ): Cipher

    /**
     * - Returns a `Cipher` object that can be used for decryption.
     *
     * - This method used to create a cipher object used during decryption process only.
     *
     * @param keyName name of the key to use for decryption.
     * @param initializationVector which you got after encryption process
     * , you need to use it for decryption.
     * @param requireBiometricAuthentication used in case of biometric authentication.
     *
     *
     * @return [Cipher] that implements the requested transformation.
     *
     *
     * @exception [com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotSecuredDeviceException] if
     * device not secured with Pattern, PIN or Password.
     * @exception [com.minaMikhail.biometricWithCrypto.crypto.exceptions.BiometricDisabledException] if
     * biometric authentication is disabled in device settings.
     */
    fun getCipherForDecryption(
        keyName: String,
        initializationVector: ByteArray?,
        requireBiometricAuthentication: Boolean = false
    ): Cipher

    /**
     * - Returns a `CryptoResult` object that contains encrypted data.
     *
     * - This method used to encrypt data.
     *
     * @param encryptCipher a cipher object used for encryption.
     * @param dataToEncrypt your data to encrypt.
     *
     *
     * @return [CryptoResult] that contains encrypted data and it's initialization Vector.
     *
     *
     * @exception [com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotAuthenticatedException] if
     * method called without biometric authentication.
     */
    fun encryptData(
        encryptCipher: Cipher,
        dataToEncrypt: String
    ): CryptoResult

    /**
     * - Returns a `String` the data that was decrypted.
     *
     * - This method used to decrypt data.
     *
     * @param decryptCipher a cipher object used for decryption.
     * @param encryptedBytes which you got after encryption process.
     *
     *
     * @return [String] the data that was decrypted.
     *
     *
     * @exception [com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotAuthenticatedException] if
     * method called without biometric authentication.
     */
    fun decryptData(
        decryptCipher: Cipher,
        encryptedBytes: ByteArray?
    ): String
}