package br.edu.ufabc.EsToDoeasy.viewmodel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.edu.ufabc.EsToDoeasy.model.Repository

/**
 * Application's main ViewModel.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        /**
         * Tasks JSON file asset name.
         */
        const val taskFile = "tasks.json"

        /**
         * Groups JSON file asset name.
         */
        const val groupFile = "groups.json"
    }

    private val repository = Repository()

    init {
        application.resources.assets.open(taskFile).use {
            repository.loadDataTasks(it)
        }

        application.resources.assets.open(groupFile).use {
            repository.loadDataGroups(it)
        }
    }

    /**
     * Maintains the currently selected task ID.
     */
    val clickedItemId by lazy { SingleLiveEvent<Long?>() }

    val clickedSelection by lazy { SingleLiveEvent<Boolean?>() }

    val clickedTaskToPlay by lazy { SingleLiveEvent<Boolean?>() }

    fun getSuggestTask() = getAll()[0]
    /**
     * Returns all tasks.
     */
    fun getAll() = repository.getAllTasks()

    /**
     * Returns all dependencies for a given task
     */
    fun getDependencies(id: Long) = repository.getDependencies(id)
    /**
     * Returns a single task information by its given ID.
     */
    fun get(id: Long) = repository.getTask(id)
}
