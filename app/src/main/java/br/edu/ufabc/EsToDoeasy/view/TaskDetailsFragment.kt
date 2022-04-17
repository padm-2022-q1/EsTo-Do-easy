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
        updateRecyclerView()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val task = viewModel.get(args.id)

        binding.taskDetailsGroup.text = viewModel.getAllGroups()[1].name
        binding.taskDetailsDateCreated.text = task.dateStarted.toString()
        binding.taskDetailsDateUpdated.text = task.dateFinished.toString()
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
