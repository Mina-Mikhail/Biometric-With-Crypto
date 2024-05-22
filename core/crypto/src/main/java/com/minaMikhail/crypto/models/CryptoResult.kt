package com.minaMikhail.crypto.models

data class CryptoResult(
    val initializationVector: ByteArray,
    val encryptedBytes: ByteArray,
    val encryptedData: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CryptoResult

        if (!initializationVector.contentEquals(other.initializationVector)) return false
        if (!encryptedBytes.contentEquals(other.encryptedBytes)) return false
        if (encryptedData != other.encryptedData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = initializationVector.contentHashCode()
        result = 31 * result + encryptedBytes.contentHashCode()
        result = 31 * result + encryptedData.hashCode()
        return result
    }
}