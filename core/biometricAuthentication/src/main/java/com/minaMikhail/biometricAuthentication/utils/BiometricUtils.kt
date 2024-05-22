package com.minaMikhail.biometricAuthentication.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.minaMikhail.biometricAuthentication.enums.AuthenticationErrorType
import java.util.concurrent.Executor

object BiometricUtils {

    private val TAG: String = BiometricUtils::class.java.simpleName

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
            override fun onAuthenticationError(errorCode: Int, errorString: CharSequence) {
                super.onAuthenticationError(errorCode, errorString)

                Log.i(TAG, "errorCode is : $errorCode and errorString is : $errorString")

                when (errorCode) {
                    BiometricPrompt.ERROR_TIMEOUT -> {
                        processError(AuthenticationErrorType.TIMEOUT)
                    }

                    BiometricPrompt.ERROR_NO_SPACE -> {
                        processError(AuthenticationErrorType.NO_SPACE)
                    }

                    BiometricPrompt.ERROR_CANCELED,
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                    BiometricPrompt.ERROR_USER_CANCELED -> {
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

                Log.i(TAG, "User biometric rejected")

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