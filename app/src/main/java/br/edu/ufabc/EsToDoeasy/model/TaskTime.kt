package br.edu.ufabc.EsToDoeasy.model

import java.text.SimpleDateFormat
import java.util.*


/**
 *  TaskTime object.
 */
data class TaskTime(
    /**
     * The TaskTime's unique identifier.
     */
    val id: Long,

    /**
     * The TaskTime's user identifier.
     */
    val userId: String,

    /**
     * The TaskTime's Task identifier.
     */
    val taskId: Long,

    /**
     * The TaskTime's date.
     */
    val date: Date?,

    /**
     * Time elapsed for this TaskTime.
     */
    val timeElapsed: Long,


    ){
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