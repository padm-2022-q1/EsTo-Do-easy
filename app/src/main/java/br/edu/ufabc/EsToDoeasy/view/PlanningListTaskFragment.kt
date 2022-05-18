package br.edu.ufabc.EsToDoeasy.view

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
class PlanningListTaskFragment : Fragment() {
    private lateinit var binding: FragmentPlanningTaskListBinding
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
        binding = FragmentPlanningTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_task, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            when (item.itemId) {
                R.id.action_delete -> {
                    makeConfirmationDialog().show()
                }
            }
        } catch (e: Exception) {
            Log.e("MENU", "Error executing ${item.itemId}", e)
            Snackbar.make(
                binding.root,
                getString(R.string.task_details_menu_operation_error),
                Snackbar.LENGTH_LONG
            ).show()
        }
        return true
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
        binding.floatingActionButton.setOnClickListener {
            PlanningListTaskFragmentDirections.addNewTask(args.id).let {
                findNavController().navigate(it)
            }
        }
    }

    private fun makeConfirmationDialog() = MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.title_delete_group_confirmation))
        .setMessage(getString(R.string.message_delete_title_confirmation))
        .setNegativeButton(getString(R.string.cancel_delete_group_label)) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(getString(R.string.confirm_delete_group_label)) { dialog, _ ->
            delete()
            dialog.dismiss()
        }


    private fun delete() {
        val id = args.id
        viewModel.deleteGroup(id).observe(viewLifecycleOwner) {
            when (it) {
                is MainViewModel.Status.Success -> {
                    findNavController().popBackStack()
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to delete item", it.e)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.group_delete_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}