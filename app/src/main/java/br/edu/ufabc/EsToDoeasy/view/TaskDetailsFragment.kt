package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentTaskDetailsBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tasks details view.
 */
class TaskDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: TaskDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            when (item.itemId) {
                R.id.action_edit -> {
                    Log.d("VIEW", "Editing item ${args.id}")
                }

                R.id.action_delete -> {
                    showDeleteDialog()
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
                    viewModel.getGroup(task.id).observe(viewLifecycleOwner) {   result ->

                        when (result) {
                            is MainViewModel.Status.Success -> {

                                val group = (result.result as MainViewModel.Result.SingleGroup).value.name
                                Log.d("GROUP","$group")
                                binding.taskDetailsGroup.text = group.toString()
                            }
                        }

                    }
                    binding.taskDetailsDateStarted.text = formatter.format(task.dateStarted)
                    binding.taskDetailsDateFinished.text = formatter.format(task.dateFinished)
                    binding.taskDetailsDateDue.text = formatter.format(task.dateDue)
                    binding.taskDetailsPriority.text = task.priority.name
                    binding.taskDetailsDifficulty.text = task.difficulty.name
                }
            }
        }
        binding.taskDetailsTitle.text = task.title
        binding.taskDetailsGroup.text = ""
        binding.taskDetailsDateStarted.text = formatter.format(task.dateStarted)
        binding.taskDetailsDateFinished.text = formatter.format(task.dateFinished)
        binding.taskDetailsDateDue.text = formatter.format(task.dateDue)
        binding.taskDetailsPriority.text = task.priority.name
        binding.taskDetailsDifficulty.text = task.difficulty.name
    }

    private fun updateRecyclerView() {
        binding.recyclerviewNextTasksList.apply {

            viewModel.getTaskDependencies(args.id).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is MainViewModel.Status.Loading -> {
                        Log.d("VIEW", "Loading")
                    }
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to fetch items", result.e)
                    }
                    is MainViewModel.Status.Success -> {
                        val tasks = (result.result as MainViewModel.Result.TaskList).value
                        Log.d("TASKS", "$tasks")
                        adapter = DependenciesTaskAdapter(
                            tasks,
                            viewModel
                        )
                    }
                }
            }
        }
    }

    private fun showDeleteDialog() {
        activity?.let {
            AlertDialog.Builder(it).setTitle(getString(R.string.task_remove_dialog_title))
                .setMessage(getString(R.string.task_remove_dialog_content))
                .setPositiveButton(getString(R.string.task_remove_dialog_confirm)) { _, _ ->
                    delete()
                }
                .setNegativeButton(getString(R.string.task_remove_dialog_cancel)) { _, _ ->
                    Log.i("VIEW", "Task ${args.id} removal cancelled")
                }
                .create()
                .show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun delete() {
        val id = args.id
        viewModel.deleteTask(id).observe(viewLifecycleOwner) {
            when (it) {
                is MainViewModel.Status.Success -> {
                    findNavController().popBackStack()
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to delete item", it.e)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.task_details_delete_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
