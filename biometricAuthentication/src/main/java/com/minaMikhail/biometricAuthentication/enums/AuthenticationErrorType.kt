package com.minaMikhail.biometricAuthentication.enums

enum class AuthenticationErrorType {
    TIMEOUT,
    NO_SPACE,
    USER_CANCELLED,
    BIOMETRIC_LOCKED_OUT,
    OTHER_ERROR,
    UNKNOWN_ERROR
}