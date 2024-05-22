package com.minaMikhail.biometricWithCrypto.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.minaMikhail.biometricAuthentication.IBiometricProvider
import com.minaMikhail.biometricAuthentication.enums.AuthenticationErrorType
import com.minaMikhail.biometricWithCrypto.R
import com.minaMikhail.biometricWithCrypto.databinding.ActivitySplashBinding
import com.minaMikhail.biometricWithCrypto.home.HomeActivity
import com.minaMikhail.biometricWithCrypto.login.LoginActivity
import com.minaMikhail.biometricWithCrypto.utils.USERNAME_KEY
import com.minaMikhail.biometricWithCrypto.utils.getStringFromResources
import com.minaMikhail.biometricWithCrypto.utils.showSnackbar
import com.minaMikhail.biometricWithCrypto.utils.toJsonModel
import com.minaMikhail.crypto.ICryptoProvider
import com.minaMikhail.crypto.exceptions.BiometricChangedException
import com.minaMikhail.crypto.exceptions.BiometricDisabledException
import com.minaMikhail.crypto.exceptions.NotAuthenticatedException
import com.minaMikhail.crypto.exceptions.NotSecuredDeviceException
import com.minaMikhail.crypto.models.CryptoResult
import com.minaMikhail.prefs.IPrefsProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.crypto.Cipher
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
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

        Handler(Looper.getMainLooper()).postDelayed({
            decideNavigation()
        }, 2000)
    }

    private fun initBinding() {
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpListeners() {
        binding.apply {
            icSplashLogo.setOnClickListener {
                decideNavigation()
            }
        }
    }

    private fun decideNavigation() {
        if (prefsProvider.isLoggedIn()) {
            prefsProvider.getEncryptedData()
                .toJsonModel(CryptoResult::class.java)
                ?.let {
                    authenticateUserForDecryption(it)
                }
        } else {
            openLoginActivity()
        }
    }

    private fun authenticateUserForDecryption(cryptoResult: CryptoResult) {
        try {
            val decryptionCipher = cryptoProvider.getCipherForDecryption(
                keyName = USERNAME_KEY,
                initializationVector = cryptoResult.initializationVector,
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
                    processBiometricAuthenticationForDecryptionSuccess(
                        decryptCipher = it.cryptoObject?.cipher,
                        encryptedBytes = cryptoResult.encryptedBytes
                    )
                },
                processError = {
                    processBiometricAuthenticationError(
                        errorType = it
                    )
                },
                cipher = decryptionCipher
            )
        } catch (ex: Exception) {
            handleExceptions(ex)
        }
    }

    private fun processBiometricAuthenticationForDecryptionSuccess(
        decryptCipher: Cipher?,
        encryptedBytes: ByteArray
    ) {
        decryptCipher?.let {
            try {
                val loggedInUsername = cryptoProvider.decryptData(
                    keyName = USERNAME_KEY,
                    decryptCipher = it,
                    encryptedBytes = encryptedBytes
                )

                openHomeActivity(loggedInUsername)
            } catch (ex: Exception) {
                handleExceptions(ex)
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

                prefsProvider.clearUserData()

                openLoginActivity()
            }

            else -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "Something went wrong"}"
                )
            }
        }
    }

    private fun openHomeActivity(username: String) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra(USERNAME_KEY, username)
        }
        startActivity(intent)
        finish()
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}