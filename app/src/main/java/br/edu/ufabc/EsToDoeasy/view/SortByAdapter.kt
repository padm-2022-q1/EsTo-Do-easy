package br.edu.ufabc.EsToDoeasy.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.FragmentStudyTechniquesItemBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Study technique item adapter.
 */
class SortByAdapter (
    val tasks: List<String>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<SortByAdapter.SortByHolder>() {

    /**
     * Study technique item view holder.
     */
    class SortByHolder(
        itemBinding: FragmentStudyTechniquesItemBinding,
        viewModel: MainViewModel
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Unique identifier.
         */
        var id: String = ""

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
                viewModel.clickedSortBy.value = false
                viewModel.sortBy.value = title.text.toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SortByHolder =
        SortByHolder(
            FragmentStudyTechniquesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: SortByHolder, position: Int) {
        //val task = tasks[position]

        //holder.id = getItemId(position)
        holder.title.text = tasks[position]
    }

    override fun getItemCount(): Int = tasks.size

    override fun getItemId(position: Int): Long = position.toLong()
}
