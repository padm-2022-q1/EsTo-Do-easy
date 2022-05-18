package br.edu.ufabc.EsToDoeasy.model

import java.text.SimpleDateFormat
import java.util.*

typealias Tasks = List<Task>

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
     * The task's user identifier.
     */
    val userId: String,

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
     * The task's due date.
     */
    val dateDue: Date?,

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
    val dependencies: List<Long>,

) : Comparable<Task> {
    companion object {
        private val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        /**
         * Converts a string to a date.
         * @param date the string
         * @return the date
         */
        fun parseDate(date: String): Date? = format.parse(date)

        /**
         * Converts a date to a string.
         * @param date the date
         * @return the string
         */
        fun formatDate(date: Date): String = format.format(date)

        /**
         * Normalizes the time information in a date.
         * @param date the date
         * @return the date with normalized time
         */
        fun simplifyDate(date: Date): Date? = parseDate(formatDate(date))
    }

    override fun compareTo(other: Task): Int = when{
        this.dateDue != null && this.dateDue != other.dateDue -> this.dateDue compareTo other.dateDue
        this.id != other.id -> this.id compareTo other.id
        else -> 0
    }
}
