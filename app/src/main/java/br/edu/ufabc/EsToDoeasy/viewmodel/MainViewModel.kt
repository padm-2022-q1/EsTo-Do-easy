package br.edu.ufabc.EsToDoeasy.viewmodel

import SingleLiveEvent
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import br.edu.ufabc.EsToDoeasy.model.*
import br.edu.ufabc.EsToDoeasy.view.TaskAdapter
import java.util.*

/**
 * Application's main ViewModel.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RepositoryFactory(application).create()

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

        /**
         * The loading status.
         */
        object Loading : Status()
    }

    /**
     * The result hierarchy.
     */
    sealed class Result {
        /**
         * Result type that holds a list of tasks.
         * @property value the list of tasks
         */
        data class TaskList(
            val value: Tasks
        ) : Result()

        /**
         * Result type that holds a single task.
         * @property value the task
         */
        data class SingleTask(
            val value: Task
        ) : Result()

        /**
         * Result type that holds a list of tags.
         * @property value the list of tags
         */
        data class TagList(
            val value: List<String>
        ) : Result()

        /**
         * Result type that holds an id.
         * @property value the id.
         */
        data class Id(
            val value: Long
        ) : Result()

        /**
         * A Result without value.
         */
        object EmptyResult : Result()

        data class GroupList(
            val value: Groups
        ) : Result()

        data class SingleGroup(
            val value: Group
        ) : Result()

    }

    /**
     * Maintains the currently selected task ID.
     */
    val clickedItemId by lazy { SingleLiveEvent<Long?>() }

    val clickedAddNewGroup by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAddNewTask by lazy { SingleLiveEvent<Boolean?>() }

    /**
     * Maintains the currently selected scheduled task ID.
     */
    val clickedScheduledTaskId by lazy { SingleLiveEvent<Long?>() }

    val clickedStudyTechniqueSelect by lazy { SingleLiveEvent<Boolean?>() }

    val selectedStudyTechnique by lazy { SingleLiveEvent<String?>() }

    val clickedTaskToPlay by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAtDetails by lazy { SingleLiveEvent<Long?>() }


    val clickedAtConfigPomodoro by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAtSkipSession by lazy { SingleLiveEvent<Long?>() }

    val clickedAtAddTask by lazy { SingleLiveEvent<Boolean?>() }

    /**
     * Returns the suggested task to be done.
     */

    val clickedSignOutProfile by lazy { SingleLiveEvent<Boolean?>() }

    val clickedSkipLogin by lazy { SingleLiveEvent<Boolean?>() }

    val clickedLoginLogin by lazy { SingleLiveEvent<Boolean?>() }

    val clickedSettingsProfile by lazy { SingleLiveEvent<Boolean?>() }

    val clickedGroupId by lazy { SingleLiveEvent<Long?>() }

    val clickedPlanningTaskId by lazy { SingleLiveEvent<Long?>() }

    val clickedAchievementProfile by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAchievementItemId by lazy { SingleLiveEvent<String?>() }

    val clickedDashboardDaily by lazy { SingleLiveEvent<Boolean?>() }

    val profileUser by lazy { SingleLiveEvent<String?>() }

    val profileEmail by lazy { SingleLiveEvent<String?>() }

    fun getAll() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getAllTasks())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }


    fun getTask(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleTask(repository.getTask(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }


    /**
     *
     */
    fun addTask(task: Task) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addTask(task))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    fun addGroup(group: Group) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addGroup(group))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    fun getSuggestTask() = Task(
        0,
        "userId",
        "title",
        "detalis",
        Date(),
        Date(),
        Date(),
        0, 2,
        Difficulty.EASY,
        Priority.HIGH,
        br.edu.ufabc.EsToDoeasy.model.Status.TODO,
        listOf()
    )

    /**
     * Returns all tasks to be done next.
     */
    fun getAllNextTasks() = listOf<Task>()

    /**
     * Returns all tasks that aren't done.
     */
    fun getAllDueTasks() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getAllDueTasks())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    /**
     * Returns all groups.
     */
    fun getAllGroups() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GroupList(repository.getAllGroups())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    /**
     * Returns all Dependencies.
     */
    fun getTaskDependencies(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getDependencies(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }


    /**
     * Returns all dependencies for a given task.
     */
    fun getDependencies(id: Long) = listOf<Task>()

    /**
     * Returns a single task information by its given ID.
     */
    fun get(id: String) = Task(
        0,
        "userId",
        "title",
        "detalis",
        Date(),
        Date(),
        Date(),
        0, 1,
        Difficulty.EASY,
        Priority.HIGH,
        br.edu.ufabc.EsToDoeasy.model.Status.TODO,
        listOf()
    )

    fun getTasksByGroupId(id: Long) = listOf<Task>()

    /**
     * Returns all achievement.
     */
    fun getAllAchievements() = listOf<Achievement>()

    /**
     * Returns a group by its given ID.
     */
    fun getGroup(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleGroup(repository.getGroup(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }
    fun getGroup(id: String) = listOf<Group>()

    fun deleteTask(id: String) = liveData {
        try {
            emit(Status.Loading)
            repository.deleteTask(id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to delete task from repository", e)))
        }
    }
}
