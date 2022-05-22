package br.edu.ufabc.EsToDoeasy.viewmodel

import SingleLiveEvent
import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.EsToDoeasy.model.*
import com.google.firebase.auth.FirebaseAuth
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
     * Indicates if there are data / operation being loaded.
     */
    val isLoading = MutableLiveData(false)

    /**
     * Currently selected study technique.
     */
    val selectedStudyTechnique = MutableLiveData("Pomodoro")


    val sortBy = MutableLiveData("Default Sort")

    val tasks = MutableLiveData<Tasks>(mutableListOf())

    val selectedDependencies =  MutableLiveData<MutableList<Long>>(mutableListOf())

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

    val clickedSortBy by lazy { SingleLiveEvent<Boolean?>() }


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
            isLoading.value = true
            emit(Status.Success(Result.TaskList(repository.getAllTasks())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun getTask(id: Long) = liveData {
        try {
            isLoading.value = true
            emit(Status.Success(Result.SingleTask(repository.getTask(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid
        ?: ""

    /**
     *
     */
    fun addTask(task: Task) = liveData {
        try {
            tasks.value = mutableListOf()
            isLoading.value = true
            emit(Status.Success(Result.Id(repository.addTask(task))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun addGroup(group: Group) = liveData {
        try {
            isLoading.value = true
            emit(Status.Success(Result.Id(repository.addGroup(group))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    val getSuggestTask by lazy { SingleLiveEvent<Task?>() }

    /**
     * Returns all tasks to be done next.
     */
    var getAllNextTasks = listOf<Task>()

    /**
     * Returns all tasks that aren't done.
     */
    fun getAllDueTasks() = liveData {
        try {
            isLoading.value = true
            emit(Status.Success(Result.TaskList(repository.getAllDueTasks())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    /**
     * Returns all groups.
     */
    fun getAllGroups() = liveData {
        try {
            isLoading.value = true
            emit(Status.Success(Result.GroupList(repository.getAllGroups())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun updateTask(id: Long, time: Long) = liveData {
        try {
            isLoading.value = true
            repository.updateTaskTime(id, time)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update item $id", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun addTimeTask(id: Long, time: Long) = liveData {
        try {
            //emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addTimeTask(id, time))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add time task $id", e)))
        }
    }

    /**
     * Returns all Dependencies.
     */
    fun getTaskDependencies(id: Long) = liveData {
        try {
            isLoading.value = true
            emit(Status.Success(Result.TaskList(repository.getDependencies(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
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
            isLoading.value = true
            emit(Status.Success(Result.SingleGroup(repository.getGroup(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun getGroup(id: String) = listOf<Group>()

    fun deleteTask(id: Long) = liveData {
        try {
            tasks.value = mutableListOf()
            isLoading.value = true
            repository.deleteTask(id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to delete task from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun deleteGroup(id: Long) = liveData {
        try {
            isLoading.value = true
            repository.deleteGroup(id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to delete group from repository", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun updateTask(task: Task) = liveData {
        try {
            tasks.value = mutableListOf()
            repository.updateTask(task)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update task from repository", e)))
        }
    }

    fun updateGroup(group: Group) = liveData {
        try {
            repository.updateGroup(group)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update group from repository", e)))
        }
    }

    fun getAllTasksByGroup(id: Long) = liveData {
        try {
            isLoading.value = true
            emit(Status.Success(Result.TaskList(repository.getAllTasksByGroup(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to retrieve tasks from a given groupId $id", e)))
        } finally {
            isLoading.value = false
        }
    }

    fun finishTask(id: Long) = liveData {
        try {
            tasks.value = mutableListOf()
            isLoading.value = true
            repository.finishTask(id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to retrieve tasks from a given groupId $id", e)))
        } finally {
            isLoading.value = false
        }
    }


    // TIMER
    enum class State { INITIAL, STARTED, STOPPED }

    val timeElapsed: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(0L)
    }
    val state: MutableLiveData<State> by lazy {
        MutableLiveData<State>(State.INITIAL)
    }

    init {
        runTimer()
    }

    private fun runTimer() {
        val handler = Handler(Looper.getMainLooper())

        handler.post(object : Runnable {
            override fun run() {
                if (isTimerRunning())
                    timeElapsed.value?.let {
                        timeElapsed.value = it + 1
                    }
                handler.postDelayed(this, 1000)
            }
        })
    }

    fun isTimerRunning() = state.value == State.STARTED

    fun getUserId() = repository.getUserId()
}
