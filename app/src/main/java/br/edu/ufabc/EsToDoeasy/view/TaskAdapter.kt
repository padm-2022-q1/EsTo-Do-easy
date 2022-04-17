package br.edu.ufabc.EsToDoeasy.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.NextTaskListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Task item adapter.
 */
class TaskAdapter(
    private val tasks: List<Task>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    /**
     * Task item view holder.
     */
    class TaskHolder(itemBinding: NextTaskListItemBinding, viewModel: MainViewModel) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Task identifier.
         */
        var id: Long = 0

        /**
         * Title text view component.
         */
        val title = itemBinding.nextTaskItemTitle
        val timeElapsed = itemBinding.nextTaskItemTimeElapsed

        init {
            itemBinding.root.setOnClickListener {
                /*
                 * This was made using var property because it wasn't accepting
                 * getItemId(position: Long), only getItemId(), which always returned -1.
                 */
                viewModel.clickedItemId.value = id
            }
            itemBinding.nextTaskItemPlay.setOnClickListener{
                viewModel.clickedTaskToPlay.value = true
            }
            itemBinding.root.setOnClickListener{
                viewModel.clickedPlanningTaskId.value = id
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskHolder =
        TaskHolder(
            NextTaskListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = tasks[position]

        holder.id = getItemId(position)
        holder.title.text = task.title.substring(0, 15)
        val hours: Long = (task.timeElapsed) / 60
        val minutes: Long = (task.timeElapsed) - hours * 60
        holder.timeElapsed.text = "0%d:0%d".format(hours, minutes)
    }

    override fun getItemCount(): Int = tasks.size

    override fun getItemId(position: Int): Long = tasks[position].id
}
