package com.minaMikhail.biometricWithCrypto

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minaMikhail.biometricWithCrypto.biometric.IBiometricProvider
import com.minaMikhail.biometricWithCrypto.biometric.enums.AuthenticationErrorType
import com.minaMikhail.biometricWithCrypto.crypto.ICryptoProvider
import com.minaMikhail.biometricWithCrypto.crypto.exceptions.BiometricDisabledException
import com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotAuthenticatedException
import com.minaMikhail.biometricWithCrypto.crypto.exceptions.NotSecuredDeviceException
import com.minaMikhail.biometricWithCrypto.crypto.models.CryptoResult
import com.minaMikhail.biometricWithCrypto.databinding.ActivityMainBinding
import com.minaMikhail.biometricWithCrypto.prefs.IPrefsProvider
import com.minaMikhail.biometricWithCrypto.prefs.enums.PreferencesKey
import com.minaMikhail.biometricWithCrypto.utils.DUMMY_TOKEN_FOR_ENCRYPTION
import com.minaMikhail.biometricWithCrypto.utils.SESSION_TOKEN_KEY
import com.minaMikhail.biometricWithCrypto.utils.getStringFromResources
import com.minaMikhail.biometricWithCrypto.utils.showSnackbar
import com.minaMikhail.biometricWithCrypto.utils.toJsonModel
import com.minaMikhail.biometricWithCrypto.utils.toJsonString
import dagger.hilt.android.AndroidEntryPoint
import javax.crypto.Cipher
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
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
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpListeners() {
        binding.apply {
            btnEncrypt.setOnClickListener {
                authenticateUserForEncryption()
            }

            btnDecrypt.setOnClickListener {
                prefsProvider
                    .getString(PreferencesKey.ENCRYPTED_DATA_KEY)
                    .toJsonModel(CryptoResult::class.java)
                    ?.let {
                        authenticateUserForDecryption(it)
                    } ?: let {
                    tvResult.text = getStringFromResources(R.string.no_data_to_decrypt)
                }
            }
        }
    }

    private fun authenticateUserForEncryption() {
        try {
            val encryptionCipher = cryptoProvider.getCipherForEncryption(
                keyName = SESSION_TOKEN_KEY,
                requireBiometricAuthentication = true
            )

            biometricProvider.authenticateWithBiometric(
                title = getStringFromResources(R.string.biometric_title),
                subTitle = getStringFromResources(R.string.biometric_sub_title),
                description = getStringFromResources(R.string.description),
                negativeButtonText = getStringFromResources(R.string.cancel),
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
                    dataToEncrypt = DUMMY_TOKEN_FOR_ENCRYPTION
                ).apply {
                    prefsProvider.saveString(PreferencesKey.ENCRYPTED_DATA_KEY, this.toJsonString())
                    binding.tvResult.text = this.encryptedData
                }
            } catch (ex: NotAuthenticatedException) {
                handleExceptions(ex)
            }
        }
    }

    private fun authenticateUserForDecryption(cryptoResult: CryptoResult) {
        val decryptionCipher = cryptoProvider.getCipherForDecryption(
            keyName = SESSION_TOKEN_KEY,
            initializationVector = cryptoResult.initializationVector,
            requireBiometricAuthentication = true
        )

        biometricProvider.authenticateWithBiometric(
            title = getStringFromResources(R.string.biometric_title),
            subTitle = getStringFromResources(R.string.biometric_sub_title),
            description = getStringFromResources(R.string.description),
            negativeButtonText = getStringFromResources(R.string.cancel),
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
    }

    private fun processBiometricAuthenticationForDecryptionSuccess(
        decryptCipher: Cipher?,
        encryptedBytes: ByteArray
    ) {
        decryptCipher?.let {
            binding.tvResult.text = cryptoProvider.decryptData(
                decryptCipher = it,
                encryptedBytes = encryptedBytes
            )
        }
    }

    private fun handleExceptions(ex: Exception) {
        when (ex) {
            is NotSecuredDeviceException -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "Something went wrong"}"
                )
            }

            is BiometricDisabledException -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "Something went wrong"}"
                )
            }

            is NotAuthenticatedException -> {
                binding.root.showSnackbar(
                    message = "Error:\n${ex.message ?: ex.cause?.message ?: "Something went wrong"}"
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

            AuthenticationErrorType.OTHER_ERROR, AuthenticationErrorType.UNKNOWN_ERROR -> {
                binding.root.showSnackbar(
                    message = getStringFromResources(R.string.some_error_occurred)
                )
            }
        }
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}