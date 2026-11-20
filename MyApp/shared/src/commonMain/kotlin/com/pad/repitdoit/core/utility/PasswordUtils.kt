package com.pad.repitdoit.core.utility

// Reúne las operaciones de derivación y comprobación de contraseñas.
internal expect fun hashPassword(password: String): String

internal expect fun matchesPassword(
    password: String,
    encodedHash: String
): Boolean