package br.edu.ufabc.EsToDoeasy.view

import br.edu.ufabc.EsToDoeasy.databinding.FragmentSelectDependenciesBinding
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskListBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * Planning task list view.
 */
class FragmentSelectDependencies : Fragment() {
    private lateinit var binding: FragmentSelectDependenciesBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PlanningListTaskFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectDependenciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_task, menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        try {
//            when (item.itemId) {
//                R.id.action_delete -> {
//                    makeConfirmationDialog().show()
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("MENU", "Error executing ${item.itemId}", e)
//            Snackbar.make(
//                binding.root,
//                getString(R.string.task_details_menu_operation_error),
//                Snackbar.LENGTH_LONG
//            ).show()
//        }
//        return true
//    }

    override fun onStart() {
        super.onStart()

        //bindEvents()

        activity?.let {
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerviewSelectDependenciesTaskList.apply {
            viewModel.getAll().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to fetch items", result.e)
                    }
                    is MainViewModel.Status.Success -> {
                        val tasks = (result.result as MainViewModel.Result.TaskList).value
                        Log.d("TASKS", "PlanningListTaskFragments $tasks")
                        adapter = SelectDependenciesAdapter(
                            tasks,
                            viewModel
                        )
                    }
                }
            }
        }
    }

//    private fun bindEvents() {
//        binding.floatingActionButton.setOnClickListener {
//            PlanningListTaskFragmentDirections.addNewTask().let {
//                findNavController().navigate(it)
//            }
//        }
//    }


    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}