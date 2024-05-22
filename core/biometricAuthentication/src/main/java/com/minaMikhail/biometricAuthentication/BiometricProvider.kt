package com.minaMikhail.biometricAuthentication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.minaMikhail.biometricAuthentication.enums.AuthenticationErrorType
import com.minaMikhail.biometricAuthentication.utils.BiometricUtils.createBiometricPrompt
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.inject.Inject

class BiometricProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val executor: Executor
) : IBiometricProvider {

    override fun canAuthenticateWithBiometric(): Boolean = BiometricManager.from(context)
        .canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS

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
    override fun createPromptInfo(
        title: String,
        subTitle: String,
        description: String,
        negativeButtonText: String
    ): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(title)
            setSubtitle(subTitle)
            setDescription(description)
            setConfirmationRequired(false)
            setNegativeButtonText(negativeButtonText)
        }.build()

    override fun authenticateWithBiometric(
        promptInfo: BiometricPrompt.PromptInfo,
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        processError: (AuthenticationErrorType) -> Unit,
        cipher: Cipher
    ) {
        val biometricPrompt = createBiometricPrompt(
            executor,
            activity,
            processSuccess,
            processError
        )

        biometricPrompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(cipher)
        )
    }
}