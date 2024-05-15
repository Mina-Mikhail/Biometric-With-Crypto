package com.minaMikhail.biometricWithCrypto.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minaMikhail.biometricWithCrypto.databinding.ActivityHomeBinding
import com.minaMikhail.biometricWithCrypto.login.LoginActivity
import com.minaMikhail.biometricWithCrypto.utils.USERNAME_KEY
import com.minaMikhail.crypto.ICryptoProvider
import com.minaMikhail.prefs.IPrefsProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var cryptoProvider: ICryptoProvider

    @Inject
    lateinit var prefsProvider: IPrefsProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        setUpViews()

        setUpListeners()
    }

    private fun initBinding() {
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews() {
        binding.apply {
            tvUsername.text = "\uD83D\uDC4B Hello, ${intent.getStringExtra(USERNAME_KEY)}"
        }
    }

    private fun setUpListeners() {
        binding.apply {
            btnLogout.setOnClickListener {
                cryptoProvider.deleteKey(USERNAME_KEY)

                prefsProvider.clearUserData()

                openSplashActivity()
            }
        }
    }

    private fun openSplashActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}