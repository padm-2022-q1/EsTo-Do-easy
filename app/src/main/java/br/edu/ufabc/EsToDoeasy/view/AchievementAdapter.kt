package br.edu.ufabc.EsToDoeasy.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentProfileAchievementsBinding
import br.edu.ufabc.EsToDoeasy.databinding.FragmentProfileCardAchievementsBinding
import br.edu.ufabc.EsToDoeasy.databinding.NextTaskListItemBinding
import br.edu.ufabc.EsToDoeasy.model.Achievement
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Achievement item adapter.
 */
class AchievementAdapter(
    private val achievements: List<Achievement>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<AchievementAdapter.AchievementHolder>() {

    /**
     * Achievement item view holder.
     */
    class AchievementHolder(itemBinding: FragmentProfileCardAchievementsBinding, viewModel: MainViewModel) :
        RecyclerView.ViewHolder(itemBinding.root) {

        /**
         * Achievement identifier.
         */
        var id: String = ""

        /**
         * Title text view component.
         */
        val title = itemBinding.profileAchievementsItemTitle
//        init {
//            itemBinding.root.setOnClickListener {
//
//                 * This was made using var property because it wasn't accepting
//                 * getItemId(position: Long), only getItemId(), which always returned -1.
//
//                viewModel.clickedAchievementItemId.value = id
//            }
//        }

   }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AchievementHolder =
        AchievementHolder(
            FragmentProfileCardAchievementsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel
        )

    override fun onBindViewHolder(holder: AchievementHolder, position: Int) {
        val achievement = achievements[position]

        holder.id = achievement.id
        holder.title.text = achievement.title

    }

    override fun getItemCount(): Int = achievements.size
}