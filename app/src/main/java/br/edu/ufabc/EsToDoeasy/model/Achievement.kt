package br.edu.ufabc.EsToDoeasy.model

import java.util.*

typealias Achievements = List<Achievement>

data class Achievement(
    /**
     * The achievement's unique identifier.
     */
    val id: Long,

    /**
     * The achievement's user identifier.
     */
    val userId: String,

    /**
     * The achievement's unique identifier.
     */
    val name: String,

    /**
     * The achievement's given title.
     */
    val title: String,

    /**
     * The achievement's details.
     */
    val details: String,

    /**
     * The achievement's win date.
     */
    val date: Date,

    /**
     * The achievement's get achieved.
     */
    val achieved: Boolean,

    /**
     * The achievement's difficulty.
     */
    val difficulty: Difficulty,

)
