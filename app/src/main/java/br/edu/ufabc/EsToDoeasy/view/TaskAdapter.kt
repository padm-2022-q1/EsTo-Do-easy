package br.edu.ufabc.EsToDoeasy.view

import android.text.format.DateUtils
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

        /**
         * Time elapsed text view component.
         */
        val timeElapsed = itemBinding.nextTaskItemTimeElapsed

        /**
         * Time elapsed icon component.
         */
        val timeElapsedIcon = itemBinding.nextTaskItemTimeElapsedIcon

        init {
            itemBinding.root.setOnClickListener {
                /*
                 * This was made using var property because it wasn't accepting
                 * getItemId(position: Long), only getItemId(), which always returned -1.
                 */
                viewModel.clickedItemId.value = id
            }
            itemBinding.nextTaskItemPlay.setOnClickListener {
                viewModel.clickedTaskToPlay.value = true
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
        holder.title.text = task.title
        holder.timeElapsed.text = DateUtils.formatElapsedTime(task.timeElapsed)

        holder.timeElapsed.alpha = if (task.timeElapsed != 0L) 1.0F else 0.6F
        holder.timeElapsedIcon.alpha = if (task.timeElapsed != 0L) 1.0F else 0.6F
    }

    override fun getItemCount(): Int = tasks.size

    override fun getItemId(position: Int): Long = tasks[position].id
}
