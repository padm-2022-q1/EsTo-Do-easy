package br.edu.ufabc.EsToDoeasy.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.PlanningListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Planning task item adapter.
 */
class PlanningTaskAdapter(
    private val tasks: List<Task>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<PlanningTaskAdapter.TaskHolder>() {

    /**
     * Planning task item view holder.
     */
    class TaskHolder(itemBinding: PlanningListItemBinding, viewModel: MainViewModel) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Task identifier.
         */
        var id: String = ""

        /**
         * Title text view component.
         */
        val title = itemBinding.planningListItemTitle

        init {
            itemBinding.root.setOnClickListener {
                viewModel.clickedPlanningTaskId.value = id
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskHolder =
        TaskHolder(
            PlanningListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = tasks[position]

        holder.id = task.id
        holder.title.text = task.title
    }

    override fun getItemCount(): Int = tasks.size
}
