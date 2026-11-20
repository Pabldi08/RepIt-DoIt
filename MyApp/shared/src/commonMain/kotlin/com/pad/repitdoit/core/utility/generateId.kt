package com.pad.repitdoit.core.utility

import kotlin.uuid.Uuid

// Genera un identificador para los registros nuevos.
fun generateId(): String {
    return Uuid.random().toString()
}