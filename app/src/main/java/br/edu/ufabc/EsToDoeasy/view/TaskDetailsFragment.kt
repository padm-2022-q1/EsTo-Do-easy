package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
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
        val task = viewModel.get(args.id)
        val group = viewModel.getGroup(task.groupId)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        binding.taskDetailsTitle.text = task.title
        binding.taskDetailsGroup.text = "group.name"
        binding.taskDetailsDateStarted.text = formatter.format(task.dateStarted)
        binding.taskDetailsDateFinished.text = formatter.format(task.dateFinished)
        binding.taskDetailsDateDue.text = formatter.format(task.dateDue)
        binding.taskDetailsPriority.text = task.priority.name
        binding.taskDetailsDifficulty.text = task.difficulty.name
    }

    private fun updateRecyclerView() {
        binding.recyclerviewNextTasksList.apply {
            adapter = DependenciesTaskAdapter(
                viewModel.getDependencies(args.id),
                viewModel
            )
        }
    }
}
