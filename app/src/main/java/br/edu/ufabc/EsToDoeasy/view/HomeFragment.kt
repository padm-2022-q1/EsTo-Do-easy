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
import br.edu.ufabc.EsToDoeasy.model.*
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            binding.recyclerviewNextTasksList.apply {
                if (tasks.isEmpty()) {
                    viewModel.getAll().observe(viewLifecycleOwner) { status ->
                        Log.d("Sort-P","${tasks.map{ it.id}}")
                        when (status) {
                            is MainViewModel.Status.Failure -> {
                                Log.e("VIEW", "Failed to fetch items", status.e)
                            }
                            is MainViewModel.Status.Success -> {
                                var currentTasks =
                                    (status.result as MainViewModel.Result.TaskList).value

                                var newTasks = listOf<Task>()

                                val graph = AdjacencyList<Task>()

                                for (task in currentTasks) { // task virou um vértice
                                    graph.createVertex(task)
                                }
                                for (task in currentTasks) { // test
                                    val neighs = currentTasks.filter { it.id in task.dependencies }
                                    for (neigh in neighs) {
                                        graph.add(EdgeType.DIRECTED, task, neigh, 0.0)
                                    }
                                }

                                when (viewModel.sortBy.value) {
                                    "Hardest first" -> {
                                        newTasks = graph.dfsUtil(currentTasks.sortedBy { it.difficulty.ordinal }.reversed())
                                            .filter { it.status == Status.TODO || it.status == Status.DOING }
                                        Log.d("sort-H", "${currentTasks.map { it.id }}")
                                    }
                                    "Easiest first" -> {
                                        newTasks = graph.dfsUtil(currentTasks.sortedBy { it.difficulty.ordinal })
                                            .filter { it.status == Status.TODO || it.status == Status.DOING }
                                        Log.d("sort-E", "${currentTasks.map { it.id }}")
                                    }
                                    else -> {
                                        newTasks = graph.dfsUtil(currentTasks.sortedBy { it.dateDue })
                                            .filter { it.status == Status.TODO || it.status == Status.DOING }
                                        Log.d("sort-D", "${currentTasks.map { it.id }}")
                                    }
                                }



                                viewModel.getSuggestTask.value = newTasks.first()

                                viewModel.tasks.value = newTasks

                                adapter = TaskAdapter(
                                    newTasks.subList(1, newTasks.size),
                                    viewModel,
                                    findNavController()
                                )
                                Log.d("VIEW", "Finished adapter")
                            }

                        }
                    }
                } else {
                    viewModel.getSuggestTask.value = tasks.first()
                    adapter = TaskAdapter(
                        tasks.subList(1, tasks.size),
                        viewModel,
                        findNavController()
                    )
                    Log.d("TASKS-R","$tasks")

                }
            }
        }
    }

    private fun initComponents() {
        if(viewModel.getCurrentUser().equals("")){
            binding.suggestedTaskItemTitle.text = "Login or Create a user First !"
        }

        viewModel.getSuggestTask.observe(viewLifecycleOwner) { task ->
            if (task != null) {
                Log.d("USER","$task")
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
                Log.d("USER","Logado sem Task")
                binding.cardviewSuggestedTaskItem.visibility = View.GONE
                binding.suggestedTaskItemNoContent.visibility = View.GONE
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
            viewModel.getSuggestTask.value?.id?.let { openTimer(it) }
        }

        binding.cardviewStudyTechniquesItemSelector.setOnClickListener {
            viewModel.clickedStudyTechniqueSelect.value = true
        }

        binding.cardviewTimerTechniquesItemSelector.setOnClickListener {
            viewModel.clickedSortBy.value = true
        }

        viewModel.sortBy.observe(this) {
            it?.let {
                if (binding.timerTechniquesItem.text.equals(it)){
                    viewModel.tasks.value = mutableListOf()
                    Log.d("TASK", "REGEREI AS TASKS :$it")
                }
                binding.timerTechniquesItem.text = it
            }
        }

        binding.floatingActionButton.setOnClickListener {
            viewModel.clickedAtAddTask.value = true
        }

        viewModel.selectedStudyTechnique.observe(this) {
            it?.let {
                Log.d("Timer", "$it")
                binding.studyTechniquesItem.text = it
            }
        }
    }

    private fun openTimer(id: Long) {
        when (viewModel.selectedStudyTechnique.value) {
            "Pomodoro" -> {
                viewModel.state.value = if (viewModel.isTimerRunning()) {
                    MainViewModel.State.STOPPED
                } else {
                    MainViewModel.State.STARTED
                }
                HomeFragmentDirections.actionNavigationListToNavigationPomodoro(id).let {
                    findNavController().navigate(it)
                }
            }
            "Free Timer" -> {
                viewModel.state.value = if (viewModel.isTimerRunning()) {
                    MainViewModel.State.STOPPED
                } else {
                    MainViewModel.State.STARTED
                }
                HomeFragmentDirections.actionMenuItemListHomeToTimerFragment(id).let {
                    findNavController().navigate(it)
                }
            }
            else -> {
                Log.e("VIEW", "Invalid study technique ${viewModel.selectedStudyTechnique.value}")
            }
        }
    }
}
