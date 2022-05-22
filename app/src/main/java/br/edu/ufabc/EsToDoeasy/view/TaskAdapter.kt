package br.edu.ufabc.EsToDoeasy.view

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
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
    private val viewModel: MainViewModel,
    private val navController: NavController,
) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    /**
     * Task item view holder.
     */
    class TaskHolder(
        itemBinding: NextTaskListItemBinding,
        viewModel: MainViewModel,
        navController: NavController
    ) :
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
                viewModel.clickedItemId.value = id
            }
            itemBinding.nextTaskItemPlay.setOnClickListener {
                when (viewModel.selectedStudyTechnique.value) {
                    "Pomodoro" -> {
                        viewModel.state.value = if (viewModel.isTimerRunning()) {
                            MainViewModel.State.STOPPED
                        } else {
                            MainViewModel.State.STARTED
                        }
                        HomeFragmentDirections.actionNavigationListToNavigationPomodoro(id)
                            .let {
                                navController.navigate(it)
                            }
                    }
                    "Free Timer" -> {
                        viewModel.state.value = if (viewModel.isTimerRunning()) {
                            MainViewModel.State.STOPPED
                        } else {
                            MainViewModel.State.STARTED
                        }
                        HomeFragmentDirections.actionMenuItemListHomeToTimerFragment(id).let {
                            navController.navigate(it)
                        }
                    }
                    else -> {
                        Log.e(
                            "VIEW",
                            "Invalid study technique ${viewModel.selectedStudyTechnique.value}"
                        )
                    }
                }
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
            viewModel,
            navController
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
