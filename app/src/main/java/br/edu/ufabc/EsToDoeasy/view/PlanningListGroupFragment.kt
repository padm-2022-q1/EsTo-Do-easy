package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningGroupListBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Planning list view.
 */
class PlanningListGroupFragment : Fragment() {
    private lateinit var binding: FragmentPlanningGroupListBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Filter criteria for tasks listing.
     */

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

        initComponents()
        bindEvents()

//        activity?.let {
//            updateRecyclerView(FilterCriteria.ALL)
//        }
    }

    private fun updateRecyclerView() {
        binding.recyclerviewPlanningList.apply {
            adapter = PlanningAdapter(
                viewModel.getAllGroups(),
                viewModel
            )
        }
    }

    private fun initComponents() {
        val menu = binding.mainNavigation

        val allBadge = menu.getOrCreateBadge(R.id.menu_item_list_planner)
        allBadge.isVisible = true
        allBadge.number = viewModel.getAllGroups().size
    }

    private fun bindEvents() {
        binding.mainNavigation.setOnItemSelectedListener {
            updateRecyclerView()

            true
        }
    }

}