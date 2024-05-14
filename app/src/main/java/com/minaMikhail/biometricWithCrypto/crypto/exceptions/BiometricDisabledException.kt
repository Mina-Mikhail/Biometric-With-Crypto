package com.minaMikhail.biometricWithCrypto.crypto.exceptions

class BiometricDisabledException(
    message: String = "Please enable biometric first"
) : Exception(message)