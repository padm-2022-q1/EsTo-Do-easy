package br.edu.ufabc.EsToDoeasy.model

import java.util.*

/**
 * Tasks difficulties.
 */
enum class Difficulty { EASY, MEDIUM, HARD }

/**
 * Tasks priorities.
 */
enum class Priority { LOW, MEDIUM, HIGH }

/**
 * Tasks statuses.
 */
enum class Status { BACKLOG, TODO, DOING, DONE, ARCHIVED }

/**
 * Domain task object.
 */
data class Task(
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
    val dateStarted: Date?,

    /**
     * The task's finish date.
     */
    val dateFinished: Date?,

    /**
     * Total elapsed task duration.
     */
    val timeElapsed: Long,

    /**
     * Task group's identifier.
     */
    val groupId: Long,

    /**
     * The task's difficulty.
     */
    val difficulty: Difficulty,

    /**
     * The task's priority.
     */
    val priority: Priority,

    /**
     * The task's status.
     */
    val status: Status,

    /**
     * The task's dependencies.
     */
    val children: List<Long>,

    /**
     * Tasks that depends from this task.
     */
    val parents: List<Long>,
)
