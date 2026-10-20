package com.pad.multiplatformapp.features.training.domain

data class RoutineDTO(
    val recommendedIntermediateRestDays : Int,
    val id : Long,
    val name : String,
    var dailyRoutines : List<DailyRoutineDTO>
)