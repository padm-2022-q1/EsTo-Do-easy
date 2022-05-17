package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.databinding.FragmentTaskDetailsBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tasks details view.
 */
class TaskDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: TaskDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initComponents()

        updateRecyclerView()
    }

    private fun initComponents() {

        viewModel.getTask(args.id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is MainViewModel.Status.Loading -> {
                    Log.d("VIEW", "Loading")
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to fetch items", result.e)
                }
                is MainViewModel.Status.Success -> {
                    val task = (result.result as MainViewModel.Result.SingleTask).value
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

                    binding.taskDetailsTitle.text = task.title
                    Log.d("GROUP","Entrei")
                    viewModel.getGroup(task.id).observe(viewLifecycleOwner) {   result ->
                        Log.d("GROUP","Entrei")
                        when (result) {
                            is MainViewModel.Status.Success -> {

                                val group = (result.result as MainViewModel.Result.SingleGroup).value.name
                                Log.d("GROUP","$group")
                                binding.taskDetailsGroup.text = group.toString()
                            }
                        }
                        Log.d("GROUP","Entrei")
                    }
                    binding.taskDetailsDateStarted.text = formatter.format(task.dateStarted)
                    binding.taskDetailsDateFinished.text = formatter.format(task.dateFinished)
                    binding.taskDetailsDateDue.text = formatter.format(task.dateDue)
                    binding.taskDetailsPriority.text = task.priority.name
                    binding.taskDetailsDifficulty.text = task.difficulty.name
                }
            }
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerviewNextTasksList.apply {

            viewModel.getTaskDependencies(" NkExZ9uEoHjieNhGaobk").observe(viewLifecycleOwner) { result ->
                when (result) {
                    is MainViewModel.Status.Loading -> {
                        Log.d("VIEW", "Loading")
                    }
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to fetch items", result.e)
                    }
                    is MainViewModel.Status.Success -> {
                        val tasks = (result.result as MainViewModel.Result.TaskList).value
                        Log.d("TASKS","$tasks")
                        adapter = DependenciesTaskAdapter(
                            tasks,
                            viewModel
                        )
                    }
                }
            }
        }
    }
}
