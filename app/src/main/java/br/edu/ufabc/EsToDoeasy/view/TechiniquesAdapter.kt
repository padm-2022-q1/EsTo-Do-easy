package br.edu.ufabc.EsToDoeasy.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.FragmentStudyTechniquesItemBinding
import br.edu.ufabc.EsToDoeasy.databinding.NextTaskListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Task item adapter.
 */
class TechiniquesAdapter (
    val tasks: List<String>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<TechiniquesAdapter.TechiniquesHolder>() {

    /**
     * Task item view holder.
     */
    class TechiniquesHolder(itemBinding: FragmentStudyTechniquesItemBinding, viewModel: MainViewModel) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Task identifier.
         */
        var id: Long = 0

        /**
         * Title text view component.
         */
        val title = itemBinding.studyTechniquesItemTitle
        val description = itemBinding.studyTechniquesItemDescription

        init {
            itemBinding.root.setOnClickListener {
                /*
                 * This was made using var property because it wasn't accepting
                 * getItemId(position: Long), only getItemId(), which always returned -1.
                 */
                viewModel.clickedSelection.value = false
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TechiniquesHolder =
        TechiniquesHolder(
            FragmentStudyTechniquesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: TechiniquesHolder, position: Int) {
        //val task = tasks[position]


        //holder.id = getItemId(position)
        holder.title.text = tasks[position]
//        val hours: Long = (task.timeElapsed) / 60
//        val minutes: Long = (task.timeElapsed) - hours * 60
//        holder.timeElapsed.text = "0%d:0%d".format(hours, minutes)
    }

    override fun getItemCount(): Int = tasks.size

    override fun getItemId(position: Int): Long = position.toLong()
}
