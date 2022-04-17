package br.edu.ufabc.EsToDoeasy.view

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.DependenciesTasksListItemBinding
import br.edu.ufabc.EsToDoeasy.databinding.NextTaskListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Status
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Task item adapter.
 */
class DependenciesTaskAdapter(
    private val tasks: List<Task>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<DependenciesTaskAdapter.DependenciesTaskHolder>() {

    /**
     * Task item view holder.
     */
    class DependenciesTaskHolder(itemBinding: DependenciesTasksListItemBinding, viewModel: MainViewModel) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Task identifier.
         */
        var id: Long = 0

        /**
         * Title text view component.
         */
        val title = itemBinding.dependeciesNextTaskItemTitle

        /**
         * Status Image view component.
         */
        val status = itemBinding.dependeciesTaskItemCheckbox

        /**
         * Alert Image view component.
         */
        val alter = itemBinding.dependeciesTaskItemAlert

        /**
         * Time elapsed text view component.
         */
        val timeElapsed = itemBinding.dependeciesNextTaskItemTimeElapsed

        /**
         * Time elapsed icon component.
         */
        val timeElapsedIcon = itemBinding.dependeciesNextTaskItemTimeElapsedIcon

        init {
            itemBinding.root.setOnClickListener {
                /*
                 * This was made using var property because it wasn't accepting
                 * getItemId(position: Long), only getItemId(), which always returned -1.
                 */
                viewModel.clickedAtDetails.value = id
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DependenciesTaskHolder =
        DependenciesTaskHolder(
            DependenciesTasksListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: DependenciesTaskHolder, position: Int) {
        val task = tasks[position]

        holder.status.visibility = View.INVISIBLE
        holder.alter.visibility = View.INVISIBLE

        holder.id = getItemId(position)
        holder.title.text = task.title
        holder.timeElapsed.text = DateUtils.formatElapsedTime(task.timeElapsed)

        if(task.status==Status.DONE){
            holder.status.visibility = View.VISIBLE
        }else if (task.status==Status.DOING || task.status==Status.TODO ){
            holder.status.visibility = View.VISIBLE
            holder.status.setBackgroundResource(R.drawable.ic_action_not_done_task)
            holder.alter.visibility = View.VISIBLE
        }

        holder.timeElapsed.alpha = if (task.timeElapsed != 0L) 1.0F else 0.6F
        holder.timeElapsedIcon.alpha = if (task.timeElapsed != 0L) 1.0F else 0.6F
    }

    override fun getItemCount(): Int = tasks.size

    override fun getItemId(position: Int): Long = tasks[position].id
}
