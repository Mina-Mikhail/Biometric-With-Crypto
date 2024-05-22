package com.minaMikhail.crypto.exceptions

class BiometricChangedException(
    message: String = "There are some changes in your biometric info, please use credentials to login again"
) : Exception(message)