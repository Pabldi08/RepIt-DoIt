package com.pad.repitdoit.core.utility

// Reúne las operaciones de derivación y comprobación de contraseñas.
internal actual fun hashPassword(
    password: String
): String = throw UnsupportedOperationException(
    "Autenticación web no implementada"
)

internal actual fun matchesPassword(
    password: String,
    encodedHash: String
): Boolean = throw UnsupportedOperationException(
    "Autenticación web no implementada"
)