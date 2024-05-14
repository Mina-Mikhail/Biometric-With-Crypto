package com.minaMikhail.biometricWithCrypto.biometric

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.minaMikhail.biometricWithCrypto.biometric.enums.AuthenticationErrorType
import com.minaMikhail.biometricWithCrypto.biometric.utils.BiometricUtils.createBiometricPrompt
import com.minaMikhail.biometricWithCrypto.biometric.utils.BiometricUtils.createPromptInfo
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

    override fun authenticateWithBiometric(
        title: String,
        subTitle: String,
        description: String,
        negativeButtonText: String,
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        processError: (AuthenticationErrorType) -> Unit,
        cipher: Cipher
    ) {
        val promptInfo = createPromptInfo(
            title,
            subTitle,
            description,
            negativeButtonText
        )

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