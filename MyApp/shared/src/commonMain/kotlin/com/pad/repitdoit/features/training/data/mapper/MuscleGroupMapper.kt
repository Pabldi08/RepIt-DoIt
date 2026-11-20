package com.pad.repitdoit.features.training.data.mapper

import com.pad.repitdoit.features.training.domain.model.MuscleGroup

// Convierte la información muscular del catálogo a los grupos que usa entrenamiento.
internal fun toMuscleGroup(
    muscleId: String,
    name: String,
    muscleGroup: String?
): MuscleGroup? {
    // Los UUID del catalogo inicial son estables, aunque cambie el nombre.
    val byId = when (muscleId) {
        "c84c1112-8e98-5d67-9925-3825858a304a" -> MuscleGroup.CHEST // Pectoral
        "78050e56-ef6a-5cd7-8fb1-09835b3040c5" -> MuscleGroup.BACK // Dorsal ancho
        "10701b87-97e5-5a3f-8e66-1af2256026bf" -> MuscleGroup.BACK // Romboides y espalda media
        "2ab9662c-3646-52e7-b78b-e5c363d4a708" -> MuscleGroup.BACK // Trapecio
        "cd6a70b2-f393-54ee-9176-88c0fcb1501f" -> MuscleGroup.SHOULDERS // Deltoides anterior
        "90f04b8c-963b-5177-b163-b52d555133fa" -> MuscleGroup.SHOULDERS // Deltoides lateral
        "98f49f69-fa8d-512d-bacd-0c729806bab0" -> MuscleGroup.REAR_SHOULDERS // Deltoides posterior
        "529e8374-e008-5a66-b074-b76bcf77b538" -> MuscleGroup.BICEPS // Bíceps
        "26c05d03-200f-5460-9bac-e89d83fa1bb2" -> MuscleGroup.TRICEPS // Tríceps
        "7c652126-cdb2-51a6-b95d-4225b21028d6" -> MuscleGroup.FOREARMS // Antebrazos
        "ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9" -> MuscleGroup.CORE // Recto abdominal
        "03ebe6b0-debc-5568-b1e4-1ba0354bdad8" -> MuscleGroup.CORE // Oblicuos
        "f0b035d2-87a6-5829-9993-3e45a9f146ca" -> MuscleGroup.CORE // Erectores espinales
        "4a13cd8b-cc50-5877-8c11-6de0c3ba011d" -> MuscleGroup.LEGS // Cuádriceps
        "59337eb2-2ae6-5991-8b8c-f2abe6a73fad" -> MuscleGroup.LEGS // Isquiotibiales
        "1ee92ffe-33b0-580e-9bcc-9da042742a58" -> MuscleGroup.LEGS // Glúteos
        "801e454b-7c85-5513-beb1-dd8553234ff5" -> MuscleGroup.LEGS // Aductores
        "95d53e06-ac76-5c7d-9331-5dbf027d52de" -> MuscleGroup.LEGS // Abductores de cadera
        "0282ca01-a60b-5d35-8d4a-32248e6c995b" -> MuscleGroup.LEGS // Gemelos y sóleo
        else -> null
    }
    if (byId != null) return byId

    // Permite incorporar nuevos musculos del catalogo con los mismos nombres/grupos.
    val byName = when (name.trim().lowercase()) {
        "pectoral" -> MuscleGroup.CHEST
        "dorsal ancho" -> MuscleGroup.BACK
        "romboides y espalda media" -> MuscleGroup.BACK
        "trapecio" -> MuscleGroup.BACK
        "deltoides anterior" -> MuscleGroup.SHOULDERS
        "deltoides lateral" -> MuscleGroup.SHOULDERS
        "deltoides posterior" -> MuscleGroup.REAR_SHOULDERS
        "bíceps" -> MuscleGroup.BICEPS
        "tríceps" -> MuscleGroup.TRICEPS
        "antebrazos" -> MuscleGroup.FOREARMS
        "recto abdominal" -> MuscleGroup.CORE
        "oblicuos" -> MuscleGroup.CORE
        "erectores espinales" -> MuscleGroup.CORE
        "cuádriceps" -> MuscleGroup.LEGS
        "isquiotibiales" -> MuscleGroup.LEGS
        "glúteos" -> MuscleGroup.LEGS
        "aductores" -> MuscleGroup.LEGS
        "abductores de cadera" -> MuscleGroup.LEGS
        "gemelos y sóleo" -> MuscleGroup.LEGS
        else -> null
    }
    if (byName != null) return byName

    return when (muscleGroup?.trim()?.lowercase()) {
        "espalda" -> MuscleGroup.BACK
        "hombros" -> MuscleGroup.SHOULDERS
        "piernas" -> MuscleGroup.LEGS
        "core" -> MuscleGroup.CORE
        // Brazos y Tren superior no identifican un grupo concreto.
        else -> null
    }
}
