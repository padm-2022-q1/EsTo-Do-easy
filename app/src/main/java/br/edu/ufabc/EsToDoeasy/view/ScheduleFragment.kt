package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentScheduleBinding
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

        val tasks = viewModel.getAllDueTasks()

        val dates = tasks.mapNotNull { it.dateFinished }
        dates.forEach {
            binding.calendarviewScheduled.date = it.time
        }


        activity?.let {
            updateRecyclerView(tasks)
        }
    }

    private fun updateRecyclerView(tasks: List<Task>) {
        binding.recyclerviewScheduledTasksList.apply {
            adapter = ScheduledTaskAdapter(
                // TODO: Change to due date.
                tasks.sortedBy { it.dateFinished },
                viewModel
            )
        }
    }
}