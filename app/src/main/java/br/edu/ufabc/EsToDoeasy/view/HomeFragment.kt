package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentHomeBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Tasks list view.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initComponents()
        bindEvents()

        activity?.let {
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerviewNextTasksList.apply {
            adapter = TaskAdapter(
                viewModel.getAllNextTasks(),
                viewModel
            )
        }
    }

    private fun initComponents() {
        val task = viewModel.getSuggestTask()
        if (task != null) {
            val group = viewModel.getGroup(task.groupId)

            binding.suggestedTaskItemTitle.text = task.title
            binding.suggestedTaskItemGroup.text = group.name
            binding.suggestedTaskItemTimeElapsed.text =
                DateUtils.formatElapsedTime(task.timeElapsed)

            binding.suggestedTaskItemTimeElapsed.alpha = if (task.timeElapsed != 0L) 1.0F else 0.6F
            binding.suggestedTaskItemTimeElapsedIcon.alpha =
                if (task.timeElapsed != 0L) 1.0F else 0.6F

            binding.cardviewSuggestedTaskItem.visibility = View.VISIBLE
            binding.suggestedTaskItemNoContent.visibility = View.INVISIBLE
        } else {
            binding.cardviewSuggestedTaskItem.visibility = View.INVISIBLE
            binding.suggestedTaskItemNoContent.visibility = View.VISIBLE
        }
    }

    private fun bindEvents() {
        val task = viewModel.getSuggestTask()
        task?.let {
            binding.cardviewSuggestedTaskItem.setOnClickListener {
                viewModel.clickedItemId.value = task.id
            }
        }

        binding.cardviewStudyTechniquesItemSelector.setOnClickListener {
            viewModel.clickedSelection.value = true
        }

        binding.suggestedTaskItemPlay.setOnClickListener {
            viewModel.clickedTaskToPlay.value = true
        }
    }
}
