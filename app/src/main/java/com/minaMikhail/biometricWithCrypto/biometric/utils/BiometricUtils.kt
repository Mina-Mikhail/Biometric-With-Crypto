package com.minaMikhail.biometricWithCrypto.biometric.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.minaMikhail.biometricWithCrypto.biometric.BiometricProvider
import com.minaMikhail.biometricWithCrypto.biometric.enums.AuthenticationErrorType
import java.util.concurrent.Executor

object BiometricUtils {

    private val TAG: String = BiometricProvider::class.java.simpleName

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
    internal fun createPromptInfo(
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

    /**
     * - Returns a `BiometricPrompt` object used to display an authentication prompt to the user.
     *
     * @param executor The executor that will be used to run [BiometricPrompt.AuthenticationCallback] methods.
     * @param activity The activity of the client application that will host the prompt.
     * @param processSuccess an action to be performed when the authentication is successful.
     * @param processError an action to be performed when the authentication is failed.
     *
     *
     * @return [BiometricPrompt] object.
     */
    internal fun createBiometricPrompt(
        executor: Executor,
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        processError: (AuthenticationErrorType) -> Unit
    ): BiometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                Log.i(TAG, "errCode is $errorCode and errString is: $errString")

                when (errorCode) {
                    BiometricPrompt.ERROR_TIMEOUT -> {
                        processError(AuthenticationErrorType.TIMEOUT)
                    }

                    BiometricPrompt.ERROR_NO_SPACE -> {
                        processError(AuthenticationErrorType.NO_SPACE)
                    }

                    BiometricPrompt.ERROR_CANCELED, BiometricPrompt.ERROR_USER_CANCELED -> {
                        processError(AuthenticationErrorType.USER_CANCELLED)
                    }

                    BiometricPrompt.ERROR_LOCKOUT -> {
                        processError(AuthenticationErrorType.BIOMETRIC_LOCKED_OUT)
                    }

                    else -> {
                        processError(AuthenticationErrorType.OTHER_ERROR)
                    }
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

                Log.i(TAG, "User biometric rejected.")
                processError(AuthenticationErrorType.UNKNOWN_ERROR)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                Log.i(TAG, "Authentication successful")
                processSuccess(result)
            }
        }
    )
}