package com.pad.repitdoit.core.utility

internal expect fun hashPassword(password: String): String

internal expect fun matchesPassword(
    password: String,
    encodedHash: String
): Boolean