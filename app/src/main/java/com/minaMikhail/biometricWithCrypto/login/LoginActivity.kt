package com.minaMikhail.biometricWithCrypto.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minaMikhail.biometricAuthentication.IBiometricProvider
import com.minaMikhail.biometricAuthentication.enums.AuthenticationErrorType
import com.minaMikhail.biometricWithCrypto.R
import com.minaMikhail.biometricWithCrypto.databinding.ActivityLoginBinding
import com.minaMikhail.biometricWithCrypto.home.HomeActivity
import com.minaMikhail.biometricWithCrypto.utils.USERNAME_KEY
import com.minaMikhail.biometricWithCrypto.utils.getStringFromResources
import com.minaMikhail.biometricWithCrypto.utils.showSnackbar
import com.minaMikhail.biometricWithCrypto.utils.toJsonString
import com.minaMikhail.crypto.ICryptoProvider
import com.minaMikhail.crypto.exceptions.BiometricChangedException
import com.minaMikhail.crypto.exceptions.BiometricDisabledException
import com.minaMikhail.crypto.exceptions.NotAuthenticatedException
import com.minaMikhail.crypto.exceptions.NotSecuredDeviceException
import com.minaMikhail.prefs.IPrefsProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.crypto.Cipher
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var cryptoProvider: ICryptoProvider

    @Inject
    lateinit var prefsProvider: IPrefsProvider

    @Inject
    lateinit var biometricProvider: IBiometricProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        setUpListeners()
    }

    private fun initBinding() {
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (username.isEmpty()) {
                    root.showSnackbar(
                        message = getStringFromResources(R.string.empty_username)
                    )
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    root.showSnackbar(
                        message = getStringFromResources(R.string.empty_password)
                    )
                    return@setOnClickListener
                }

                authenticateUserForEncryption()
            }
        }
    }

    private fun authenticateUserForEncryption() {
        try {
            val encryptionCipher = cryptoProvider.getCipherForEncryption(
                keyName = USERNAME_KEY,
                requireBiometricAuthentication = true
            )

            val promptInfo = biometricProvider.createPromptInfo(
                title = getStringFromResources(R.string.biometric_title),
                subTitle = getStringFromResources(R.string.biometric_sub_title),
                description = getStringFromResources(R.string.description),
                negativeButtonText = getStringFromResources(R.string.cancel)
            )

            biometricProvider.authenticateWithBiometric(
                promptInfo = promptInfo,
                activity = this,
                processSuccess = {
                    processBiometricAuthenticationForEncryptionSuccess(
                        encryptCipher = it.cryptoObject?.cipher
                    )
                },
                processError = {
                    processBiometricAuthenticationError(
                        errorType = it
                    )
                },
                cipher = encryptionCipher
            )
        } catch (ex: Exception) {
            handleExceptions(ex)
        }
    }

    private fun processBiometricAuthenticationForEncryptionSuccess(
        encryptCipher: Cipher?
    ) {
        encryptCipher?.let {
            try {
                cryptoProvider.encryptData(
                    encryptCipher = encryptCipher,
                    dataToEncrypt = binding.etUsername.text.toString().trim()
                ).let { cryptoResult ->
                    prefsProvider.apply {
                        setLoggedIn(true)
                        saveEncryptedData(cryptoResult.toJsonString())
                    }

                    openHomeActivity()
                }
            } catch (ex: NotAuthenticatedException) {
                handleExceptions(ex)
            }
        }
    }

    private fun handleExceptions(ex: Exception) {
        when (ex) {
            is NotSecuredDeviceException -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "NotSecuredDeviceException"}"
                )
            }

            is BiometricDisabledException -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "BiometricDisabledException"}"
                )
            }

            is NotAuthenticatedException -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "NotAuthenticatedException"}"
                )
            }

            is BiometricChangedException -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "BiometricChangedException"}"
                )
            }

            else -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "Something went wrong"}"
                )
            }
        }
    }

    private fun processBiometricAuthenticationError(
        errorType: AuthenticationErrorType
    ) {
        when (errorType) {
            AuthenticationErrorType.TIMEOUT -> {
                binding.root.showSnackbar(
                    message = getStringFromResources(R.string.biometric_timeout)
                )
            }

            AuthenticationErrorType.NO_SPACE -> {
                binding.root.showSnackbar(
                    message = getStringFromResources(R.string.biometric_no_space)
                )
            }

            AuthenticationErrorType.USER_CANCELLED -> {
                binding.root.showSnackbar(
                    message = getStringFromResources(R.string.user_cancelled)
                )
            }

            AuthenticationErrorType.BIOMETRIC_LOCKED_OUT -> {
                binding.root.showSnackbar(
                    message = getStringFromResources(R.string.biometric_locked_out)
                )
            }

            AuthenticationErrorType.OTHER_ERROR,
            AuthenticationErrorType.UNKNOWN_ERROR -> {
                binding.root.showSnackbar(
                    message = getStringFromResources(R.string.some_error_occurred)
                )
            }
        }
    }

    private fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra(USERNAME_KEY, binding.etUsername.text.toString().trim())
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}