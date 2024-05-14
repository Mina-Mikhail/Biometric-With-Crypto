package com.minaMikhail.biometricWithCrypto.crypto.di

import com.minaMikhail.biometricWithCrypto.crypto.CryptoProvider
import com.minaMikhail.biometricWithCrypto.crypto.ICryptoProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.KeyStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidingCryptoModule {

    @Provides
    @Singleton
    fun provideKeyStore(): KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingCryptoModule {

    @Binds
    @Singleton
    abstract fun bindCryptoProvider(
        cryptoProvider: CryptoProvider
    ): ICryptoProvider
}