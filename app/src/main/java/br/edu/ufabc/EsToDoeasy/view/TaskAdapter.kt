package br.edu.ufabc.EsToDoeasy.view

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.R
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

        /**
         * List item context.
         */
        val context = itemBinding.root.context

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

            //viewModel.clickedPlanningTaskId.value = id

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

        holder.id = task.id
        holder.title.text = task.title
        holder.timeElapsed.text = DateUtils.formatElapsedTime(task.timeElapsed)

        val color = ContextCompat.getColor(
            holder.context,
            if (task.timeElapsed != 0L) R.color.text_primary else R.color.text_faded
        )
        holder.timeElapsed.setTextColor(color)
        holder.timeElapsedIcon.setColorFilter(color)
    }

    override fun getItemCount(): Int = tasks.size
}
