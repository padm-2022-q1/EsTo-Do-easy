package br.edu.ufabc.EsToDoeasy.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.FragmentStudyTechniquesItemBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Study technique item adapter.
 */
class StudyTechniqueAdapter(
    val tasks: List<String>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<StudyTechniqueAdapter.StudyTechniqueHolder>() {

    /**
     * Study technique item view holder.
     */
    class StudyTechniqueHolder(
        itemBinding: FragmentStudyTechniquesItemBinding,
        viewModel: MainViewModel
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Unique identifier.
         */
        var id: Long = 0

        /**
         * Title text view component.
         */
        val title = itemBinding.studyTechniquesItemTitle

        /**
         * Description text view component.
         */
        val description = itemBinding.studyTechniquesItemDescription

        init {
            itemBinding.root.setOnClickListener {
                viewModel.clickedStudyTechniqueSelect.value = false
                viewModel.selectedStudyTechnique.value = title.text.toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudyTechniqueHolder =
        StudyTechniqueHolder(
            FragmentStudyTechniquesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: StudyTechniqueHolder, position: Int) {
        //val task = tasks[position]

        //holder.id = getItemId(position)
        holder.title.text = tasks[position]
    }

    override fun getItemCount(): Int = tasks.size

    override fun getItemId(position: Int): Long = position.toLong()
}
