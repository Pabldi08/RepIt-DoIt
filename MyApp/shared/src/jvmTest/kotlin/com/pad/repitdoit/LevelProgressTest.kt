package com.pad.repitdoit

import com.pad.repitdoit.features.gamification.domain.model.LevelProgress
import com.pad.repitdoit.features.gamification.domain.model.calculateLevelProgress
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LevelProgressTest {
    @Test
    fun calculatesThresholdsAndCarriesXpAcrossLevels() {
        for ((xp, expected) in listOf(
            0L to LevelProgress(1, 0, 100),
            99L to LevelProgress(1, 99, 100),
            100L to LevelProgress(2, 0, 150),
            180L to LevelProgress(2, 80, 150),
            249L to LevelProgress(2, 149, 150),
            250L to LevelProgress(3, 0, 200),
            470L to LevelProgress(4, 20, 250)
        )) assertEquals(expected, calculateLevelProgress(xp))
    }

    @Test
    fun stateDerivesProgressAgainWhenTotalChanges() {
        val state = GamificationState().copy(totalXp = 180)
        assertEquals(2, state.level)
        assertEquals(80L, state.currentXp)
        assertEquals(150L, state.nextLevelXp)
        assertEquals(3, state.copy(totalXp = 250).level)
    }

    @Test
    fun handlesLargeTotalsWithoutOverflowAndRejectsNegativeXp() {
        val progress = calculateLevelProgress(Long.MAX_VALUE)
        assertTrue(progress.currentXp >= 0)
        assertTrue(progress.currentXp < progress.nextLevelXp)
        val completed = progress.level.toLong() - 1
        assertEquals(Long.MAX_VALUE, 25 * completed * (completed + 3) + progress.currentXp)
        assertFailsWith<IllegalArgumentException> { calculateLevelProgress(-1) }
    }
}
