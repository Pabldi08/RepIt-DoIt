package com.pad.repitdoit.core.utility

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// Reúne las operaciones de derivación y comprobación de contraseñas.
private const val ITERATIONS = 600_000

private fun deriveHash(
    password: String,
    salt: ByteArray
): ByteArray {

    val spec = PBEKeySpec(
        password.toCharArray(),
        salt,
        ITERATIONS,
        256
    )

    return try {
        SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec)
            .encoded
    } finally {
        spec.clearPassword()
    }
}

internal actual fun hashPassword(
    password: String
): String {

    val salt = ByteArray(16)
    SecureRandom().nextBytes(salt)

    val hash = deriveHash(password, salt)
    val encoder = Base64.getEncoder()

    return "pbkdf2-sha256:$ITERATIONS:" +
            encoder.encodeToString(salt) + ":" +
            encoder.encodeToString(hash)
}

internal actual fun matchesPassword(
    password: String,
    encodedHash: String
): Boolean {

    val parts = encodedHash.split(":")

    require(parts.size == 4)
    require(parts[0] == "pbkdf2-sha256")
    require(parts[1] == ITERATIONS.toString())

    val decoder = Base64.getDecoder()

    val salt = decoder.decode(parts[2])
    val expectedHash = decoder.decode(parts[3])

    require(salt.size == 16)
    require(expectedHash.size == 32)

    val actualHash = deriveHash(password, salt)

    return MessageDigest.isEqual(
        expectedHash,
        actualHash
    )
}