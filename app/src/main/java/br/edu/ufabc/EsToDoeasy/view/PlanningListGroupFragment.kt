package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningGroupListBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Planning group list view.
 */
class PlanningListGroupFragment : Fragment() {
    private lateinit var binding: FragmentPlanningGroupListBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanningGroupListBinding.inflate(inflater, container, false)
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
        binding.recyclerviewPlanningList.apply {
            viewModel.getAllGroups().observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to fetch items", status.e)
                    }
                    is MainViewModel.Status.Success -> {
                        val groups = (status.result as MainViewModel.Result.GroupList).value
                        adapter = PlanningAdapter(
                            groups,
                            viewModel
                        )
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
        }
    }
    private fun bindEvents() {
        viewModel.clickedItemId.observe(viewLifecycleOwner) {
            it?.let {
                val action =
                    PlanningListGroupFragmentDirections.actionPlanningListFragmentToPlanningListTaskFragment()
                navController.navigate(action)
            }
        }

        binding.addTaskGroupItem.setOnClickListener {
            viewModel.clickedAddNewGroup.value = true
        }
    }
}