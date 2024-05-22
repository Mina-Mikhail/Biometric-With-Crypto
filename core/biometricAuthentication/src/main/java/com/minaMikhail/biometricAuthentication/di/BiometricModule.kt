package com.minaMikhail.biometricAuthentication.di

import android.content.Context
import androidx.core.content.ContextCompat
import com.minaMikhail.biometricAuthentication.BiometricProvider
import com.minaMikhail.biometricAuthentication.IBiometricProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidingBiometricModule {

    @Provides
    @Singleton
    fun provideMainExecutor(
        @ApplicationContext context: Context
    ): Executor = ContextCompat.getMainExecutor(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingBiometricModule {

    @Binds
    @Singleton
    abstract fun bindBiometricProvider(
        biometricProvider: BiometricProvider
    ): IBiometricProvider
}