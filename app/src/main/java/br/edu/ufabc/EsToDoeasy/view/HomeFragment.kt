package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentHomeBinding
import br.edu.ufabc.EsToDoeasy.model.AdjacencyList
import br.edu.ufabc.EsToDoeasy.model.EdgeType
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Tasks list view.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()

    enum class State { INITIAL, STARTED, STOPPED }

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
            viewModel.getAll().observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to fetch items", status.e)
                    }
                    is MainViewModel.Status.Success -> {
                        val tasks = (status.result as MainViewModel.Result.TaskList).value

                        val graph = AdjacencyList<Task>()

                        // FIX: run once
                        for (task in tasks) { // task virou um vértice
                            graph.createVertex(task)
                        }
                        for (task in tasks) { // test
                            val neighs = tasks.filter { it.id in task.dependencies }
                            for (neigh in neighs) {
                                graph.add(EdgeType.DIRECTED, task, neigh, 0.0)
                            }
                        }
                        val newTasks = graph.dfsUtil()

                        viewModel.getSuggestTask.value = newTasks.first()

                        adapter = TaskAdapter(
                            newTasks.subList(1, newTasks.size - 1),
                            viewModel
                        )
                        Log.d("VIEW", "Finished adapter")
                    }
                }
            }
        }
    }

    private fun initComponents() {
        viewModel.getSuggestTask.observe(viewLifecycleOwner) { task ->
            if (task != null) {
                binding.suggestedTaskItemTitle.text = task.title
                viewModel.getGroup(task.groupId).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is MainViewModel.Status.Success -> {
                            val group =
                                (result.result as MainViewModel.Result.SingleGroup).value.name
                            Log.d("GROUP", group)
                            binding.suggestedTaskItemGroup.text = group
                        }
                        else -> {}
                    }
                }

                binding.suggestedTaskItemTimeElapsed.text =
                    DateUtils.formatElapsedTime(task.timeElapsed)

                context?.let {
                    val color = ContextCompat.getColor(
                        it,
                        if (task.timeElapsed != 0L) R.color.text_primary else R.color.text_faded
                    )
                    binding.suggestedTaskItemTimeElapsed.setTextColor(color)
                    binding.suggestedTaskItemTimeElapsedIcon.setColorFilter(color)
                }

                binding.cardviewSuggestedTaskItem.visibility = View.VISIBLE
                binding.suggestedTaskItemNoContent.visibility = View.INVISIBLE
            } else {
                binding.cardviewSuggestedTaskItem.visibility = View.INVISIBLE
                binding.suggestedTaskItemNoContent.visibility = View.VISIBLE
            }
        }


        viewModel.selectedStudyTechnique.value?.let {
            binding.studyTechniquesItem.text = viewModel.selectedStudyTechnique.value
        }
    }

    private fun bindEvents() {
        binding.cardviewSuggestedTaskItem.setOnClickListener {
            viewModel.clickedItemId.value = viewModel.getSuggestTask.value?.id
        }
        binding.suggestedTaskItemPlay.setOnClickListener {
            //viewModel.clickedTaskToPlay.value = true

            viewModel.getSuggestTask.value?.id?.let { it1 ->
                when (binding.studyTechniquesItem.text) {
                    "Pomodoro" -> {

                        viewModel.state.value = if (viewModel.isTimerRunning()) {
                            MainViewModel.State.STOPPED
                        } else {
                            MainViewModel.State.STARTED
                        }
                        HomeFragmentDirections.actionNavigationListToNavigationPomodoro().let {
                            findNavController().navigate(it)
                        }

                    }
                    "Free" -> {
                        viewModel.state.value = if (viewModel.isTimerRunning()) {
                            MainViewModel.State.STOPPED
                        } else {
                            MainViewModel.State.STARTED
                        }
                        HomeFragmentDirections.actionMenuItemListHomeToTimerFragment(it1).let {
                            findNavController().navigate(it)
                        }
                    }
                    else -> {
                        viewModel.state.value = if (viewModel.isTimerRunning()) {
                            MainViewModel.State.STOPPED
                        } else {
                            MainViewModel.State.STARTED
                        }
                        HomeFragmentDirections.actionNavigationListToNavigationPomodoro().let {
                            findNavController().navigate(it)
                        }
                    }
                }

            }
        }

        binding.cardviewStudyTechniquesItemSelector.setOnClickListener {
            viewModel.clickedStudyTechniqueSelect.value = true
        }

        binding.addTaskItem.setOnClickListener {
            viewModel.clickedAtAddTask.value = true
        }

        binding.floatingActionButton.setOnClickListener {
            viewModel.clickedAtAddTask.value = true
        }

        viewModel.selectedStudyTechnique.observe(this) {
            it?.let {
                binding.studyTechniquesItem.text = it
            }
        }
    }
}
