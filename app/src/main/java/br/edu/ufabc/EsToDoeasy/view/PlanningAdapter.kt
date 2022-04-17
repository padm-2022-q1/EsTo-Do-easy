package br.edu.ufabc.EsToDoeasy.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.databinding.PlanningListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Group
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Task item adapter.
 */
class PlanningAdapter (
    private val groups: List<Group>,
    private val viewModel: MainViewModel
    ) : RecyclerView.Adapter<PlanningAdapter.PlanningHolder>() {

        /**
         * Task item view holder.
         */
        class PlanningHolder(groupBinding: PlanningListItemBinding, viewModel: MainViewModel) :
            RecyclerView.ViewHolder(groupBinding.root) {

            /**
             * Task identifier.
             */
            var id: Long = 0

            /**
             * Title text view component.
             */
            val name = groupBinding.planningListItemTitle

            init {
                groupBinding.root.setOnClickListener {
                    /*
                     * This was made using var property because it wasn't accepting
                     * getItemId(position: Long), only getItemId(), which always returned -1.
                     */
                    viewModel.clickedGroupId.value = id
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PlanningHolder =
            PlanningHolder(
                PlanningListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                viewModel
            )

        override fun onBindViewHolder(holder: PlanningHolder, position: Int) {
            val group = groups[position]

            holder.id = getItemId(position)
            holder.name.text = group.name
        }

        override fun getItemCount(): Int = groups.size

        override fun getItemId(position: Int): Long = groups[position].id
}