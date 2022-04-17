package br.edu.ufabc.EsToDoeasy.model

import java.util.*

data class Achievement(
    /**
     * The task's unique identifier.
     */
    val id: Long,

    /**
     * The task's given title.
     */
    val title: String,

    /**
     * The task's details.
     */
    val details: String,

    /**
     * The task's start date.
     */
    val date: String,

    /**
     * The task's finish date.
     */
    val achieved: String,

    /**
     * The task's difficulty.
     */
    val difficulty: String,

)
