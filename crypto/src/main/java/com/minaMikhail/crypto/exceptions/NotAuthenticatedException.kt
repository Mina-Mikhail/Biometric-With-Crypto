package com.minaMikhail.crypto.exceptions

class NotAuthenticatedException(
    message: String = "Not Authenticated, Use biometric authentication first"
) : Exception(message)