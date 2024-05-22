package com.minaMikhail.biometricAuthentication

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.minaMikhail.biometricAuthentication.enums.AuthenticationErrorType
import javax.crypto.Cipher

interface IBiometricProvider {

    /**
     * - Returns whether the device is enabled biometrics or not.
     *
     * @return `true` if biometric authentication enabled in device settings.
     */
    fun canAuthenticateWithBiometric(): Boolean

    /**
     * - Returns a `PromptInfo` object used to display an authentication prompt to the user.
     *
     * @param title a title for the authentication prompt dialog.
     * @param subTitle a subTitle for the authentication prompt dialog.
     * @param description a description for the authentication prompt dialog.
     * @param negativeButtonText a text for the negative button in the authentication prompt dialog.
     *
     *
     * @return [BiometricPrompt.PromptInfo] object.
     */
    fun createPromptInfo(
        title: String,
        subTitle: String,
        description: String,
        negativeButtonText: String
    ): BiometricPrompt.PromptInfo

    /**
     * - Displays the authentication prompt dialog to the user.
     *
     * @param promptInfo The object used to display an authentication prompt to the user.
     * @param activity The activity of the client application that will host the prompt.
     * @param processSuccess an action to be performed when the authentication is successful.
     * @param processError an action to be performed when the authentication is failed.
     * @param cipher a cipher to be used for encryption/decryption process after authentication success.
     *
     *
     */
    fun authenticateWithBiometric(
        promptInfo: BiometricPrompt.PromptInfo,
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        processError: (AuthenticationErrorType) -> Unit,
        cipher: Cipher
    )
}