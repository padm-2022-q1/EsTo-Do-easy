package br.edu.ufabc.EsToDoeasy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import br.edu.ufabc.EsToDoeasy.R

class PomodoroViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Defines a Pomodoro state.
     */
    enum class State { FOCUS, SHORT_BREAK, LONG_BREAK }

    /**
     * Defines a single pomodoro state setting.
     * @property state the pomodoro state
     * @property timer the pomodoro state timer
     * @property color the pomodoro state coloring
     * @property action the text to show on the pomodoro action button
     */
    sealed class StateSetting(
        val state: State,
        val timer: Long,
        val color: Int,
        val action: Int,
    ) {
        object FocusState :
            StateSetting(State.FOCUS, 25 * 60, R.color.orange, R.string.pomodoro_pause_focus)

        object ShortBreakState :
            StateSetting(State.SHORT_BREAK, 5 * 60, R.color.green, R.string.skip_break_label)

        object LongBreakState :
            StateSetting(State.LONG_BREAK, 15 * 60, R.color.purple, R.string.skip_break_label)
    }

    /**
     * Status hierarchy.
     */
    sealed class Status {
        /**
         * The error status.
         * @property e the exception
         */
        class Failure(val e: Exception) : Status()

        /**
         * The success status.
         * @property result the result
         */
        class Success(val result: Result) : Status()
    }

    /**
     * The result hierarchy.
     */
    sealed class Result {
        /**
         * A Result without value.
         */
        object EmptyResult : Result()

        /**
         * Result with a Pomodoro state value.
         * @property value the Pomodoro state
         */
        data class StateResult(val value: StateSetting) : Result()
    }

    private val order = listOf(
        StateSetting.FocusState,
        StateSetting.ShortBreakState,
        StateSetting.FocusState,
        StateSetting.ShortBreakState,
        StateSetting.FocusState,
        StateSetting.ShortBreakState,
        StateSetting.FocusState,
        StateSetting.LongBreakState,
    )

    private var currentIndex = 0

    fun currentState() = order[currentIndex]

    fun nextState() = liveData {
        try {
            currentIndex++
            if (currentIndex >= order.size) currentIndex = 0

            emit(Status.Success(Result.StateResult(order[currentIndex])))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to proceed to next pomodoro state", e)))
        }
    }
}