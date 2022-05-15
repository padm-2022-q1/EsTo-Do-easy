package br.edu.ufabc.EsToDoeasy.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.ScheduledTaskListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Scheduled task item adapter.
 */
class ScheduledTaskAdapter(
    private val tasks: List<Task>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<ScheduledTaskAdapter.ScheduledTaskHolder>() {

    /**
     * Scheduled task item view holder.
     */
    class ScheduledTaskHolder(itemBinding: ScheduledTaskListItemBinding, viewModel: MainViewModel) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Task identifier.
         */
        var id: String = ""

        /**
         * Title text view component.
         */
        val title = itemBinding.scheduledTaskItemTitle

        /**
         * Due date text view component.
         */
        val dueDate = itemBinding.scheduledTaskItemDueDate

        init {
            itemBinding.root.setOnClickListener {
                viewModel.clickedScheduledTaskId.value = id
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduledTaskHolder =
        ScheduledTaskHolder(
            ScheduledTaskListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: ScheduledTaskHolder, position: Int) {
        val task = tasks[position]

        holder.id = task.id
        holder.title.text = task.title
        holder.dueDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(task.dateDue)
    }

    override fun getItemCount(): Int = tasks.size
}
