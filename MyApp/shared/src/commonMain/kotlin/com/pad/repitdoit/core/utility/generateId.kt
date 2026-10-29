package com.pad.repitdoit.core.utility

import kotlin.uuid.Uuid

fun generateId(): String {
    return Uuid.random().toString()
}