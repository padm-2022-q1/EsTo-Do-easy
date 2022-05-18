package br.edu.ufabc.EsToDoeasy.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.DependenciesTasksSelectListBinding
import br.edu.ufabc.EsToDoeasy.databinding.PlanningListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Planning task item adapter.
 */
class SelectDependenciesAdapter(
    private val tasks: List<Task>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<SelectDependenciesAdapter.TaskHolder>() {

    /**
     * Planning task item view holder.
     */
    class TaskHolder(itemBinding: DependenciesTasksSelectListBinding, viewModel: MainViewModel) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Task identifier.
         */
        var id: Long = 0

        /**
         * Title text view component.
         */
        val title = itemBinding.dependeciesTaskItemTitle

        //val group = itemBinding.dependeciesTaskItemGroup

        val checkBox = itemBinding.dependeciesTaskItemCheckbox

        init {
            itemBinding.root.setOnClickListener {
                viewModel.selectedDependencies.value?.add(id)
                Log.d("dependencies","${viewModel.selectedDependencies.value}")
                checkBox.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskHolder =
        TaskHolder(
            DependenciesTasksSelectListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = tasks[position]
        holder.id = task.id
        holder.checkBox.isChecked = viewModel.selectedDependencies.value?.any { task.id == it } ?: holder.checkBox.isChecked
        holder.title.text = task.title
    }

    override fun getItemCount(): Int = tasks.size

    override fun onViewRecycled(holder: TaskHolder) {
        super.onViewRecycled(holder)
    }
}
