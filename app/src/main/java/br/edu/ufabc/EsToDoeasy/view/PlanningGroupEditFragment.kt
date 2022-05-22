package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningGroupEditBinding
import br.edu.ufabc.EsToDoeasy.model.*
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.*

class PlanningGroupEditFragment : Fragment() {
    private lateinit var binding: FragmentPlanningGroupEditBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PlanningGroupEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanningGroupEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_edit, menu)
        inflater.inflate(R.menu.menu_task_form, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            when (item.itemId) {
                R.id.action_delete -> {
                    makeConfirmationDialog().show()
                }
                R.id.action_save -> {
                    edit()
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
    }

    private fun initComponents() {
        viewModel.getGroup(args.id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is MainViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to fetch items", result.e)
                }
                is MainViewModel.Status.Success -> {
                    val group = (result.result as MainViewModel.Result.SingleGroup).value
                    binding.planningGroupEditNameEditText.setText(group.name)
                }
                else -> {
                }
            }
        }
    }

    private fun edit() {

        val group = Group(
            id = args.id,
            userId = viewModel.getUserId(),
            name = binding.planningGroupEditNameEditText.text.toString()
        )

        Log.d("edit", "group build",)

        Log.d("edit", "group build $group.name",)

        viewModel.updateGroup(group).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    Log.d("edit", "deu certo")
                    PlanningGroupEditFragmentDirections.editedGroup()
                        .let {
                            findNavController().popBackStack()
                        }
                    Snackbar.make(binding.root, "Group edited", Snackbar.LENGTH_LONG).show()
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("edit", "Failed to edit group", status.e)
                    Snackbar.make(binding.root, "Failed to edit group", Snackbar.LENGTH_LONG).show()
                }
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
                    Snackbar.make(binding.root, "Group deleted", Snackbar.LENGTH_LONG).show()
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to delete group", it.e)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.group_delete_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}