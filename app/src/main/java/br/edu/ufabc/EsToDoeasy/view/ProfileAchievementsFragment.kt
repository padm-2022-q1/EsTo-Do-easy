package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentProfileAchievementsBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileAchievementsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileAchievementsFragment : Fragment() {
    private lateinit var binding: FragmentProfileAchievementsBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Filter criteria for tasks listing.
     */
    enum class FilterCriteria { Y, N }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileAchievementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.RecyclerviewProfileAchievements.apply {
            adapter = AchievementAdapter(
                viewModel.getAllAchievements(),
                viewModel
            )
        }
    }
}
