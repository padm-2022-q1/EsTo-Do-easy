package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentScheduleBinding
import br.edu.ufabc.EsToDoeasy.model.AdjacencyList
import br.edu.ufabc.EsToDoeasy.model.EdgeType
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Calendar schedule fragment.
 */
class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getAllDueTasks().observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to fetch items", status.e)
                }
                is MainViewModel.Status.Success -> {
                    val tasks = (status.result as MainViewModel.Result.TaskList).value

                    updateRecyclerView(tasks)
                    //swapAdapter(TaskAdapter(tasks.sortedBy { it.deadline }), false)
//                        addItemDecoration(
//                            DividerItemDecoration(
//                                this.context,
//                                DividerItemDecoration.VERTICAL
//                            )
//                        )
//                        binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }
//        val dates = tasks.mapNotNull { it.dateFinished }
//        dates.forEach {
//            binding.calendarviewScheduled.date = it.time
//        }

    }

    private fun updateRecyclerView(tasks: List<Task>) {
        binding.recyclerviewScheduledTasksList.apply {
            adapter = ScheduledTaskAdapter(
                tasks.sortedBy { it.dateDue },
                viewModel
            )
        }
    }
}