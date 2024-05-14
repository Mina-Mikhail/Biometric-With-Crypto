package com.minaMikhail.crypto.exceptions

class NotSecuredDeviceException(
    message: String = "Need to secure device first, by setting Pattern, PIN or Password"
) : Exception(message)