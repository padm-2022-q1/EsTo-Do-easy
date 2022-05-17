package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskListBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Planning task list view.
 */
class PlanningListTaskFragment : Fragment() {
    private lateinit var binding: FragmentPlanningTaskListBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PlanningListTaskFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanningTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bindEvents()

        activity?.let {
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerviewPlanningTaskList.apply {

            viewModel.getAllTasksByGroup(args.id).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is MainViewModel.Status.Loading -> {
                        Log.d("VIEW", "Loading")
                    }
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to fetch items", result.e)
                    }
                    is MainViewModel.Status.Success -> {
                        val tasks = (result.result as MainViewModel.Result.TaskList).value
                        Log.d("TASKS", "PlanningListTaskFragments $tasks")
                        adapter = PlanningTaskAdapter(
                            tasks,
                            viewModel
                        )
                    }
                }
            }
        }
    }

    private fun bindEvents() {
        binding.addTaskItem.setOnClickListener{
            viewModel.clickedAddNewTask.value = true
        }
    }
}