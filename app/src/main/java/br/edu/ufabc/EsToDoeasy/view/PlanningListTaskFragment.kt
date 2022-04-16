package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskListBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Planning task list view.
 */

class PlanningListTaskFragment : Fragment(){
    private lateinit var binding: FragmentPlanningTaskListBinding
    private val viewModel: MainViewModel by activityViewModels()
//    private val args: PlanningDetailsFragmentArgs by navArgs()

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

        activity?.let {
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerviewPlanningTaskList.apply {
            adapter = PlanningAdapter(
                viewModel.getAllGroups(),
                viewModel
            )
        }
    }
}