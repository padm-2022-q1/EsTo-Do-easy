package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskDetailsBinding
import br.edu.ufabc.EsToDoeasy.model.Difficulty
import br.edu.ufabc.EsToDoeasy.model.Priority
import br.edu.ufabc.EsToDoeasy.model.Status
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*


class PlanningTaskDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlanningTaskDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PlanningTaskDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanningTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val task = viewModel.get(args.id)

//        binding.planningTaskDetailsTaskName.setText(task.title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_details, menu)

    }

    override fun onStart() {
        super.onStart()
    }

    fun add() {
        val task = Task(
            id = "",
            userId = "h1MW2FdpRqOFBmPIF5ncDsRF48q1",
            title = binding.planningTaskDetailsTaskName.text.toString(),
            details = "",
            dateStarted = Task.parseDate(binding.planningTaskDetailsDateStartEditText.text.toString()),
            dateFinished = Task.parseDate(binding.planningTaskDetailsDateEndEditText.text.toString()),
            dateDue = Task.parseDate(binding.planningTaskDetailsDateDueEditText.text.toString()),
            timeElapsed = 0,
            groupId = "1", // TODO:
            difficulty = Difficulty.EASY,
            priority = Priority.LOW,
            status = Status.TODO,
            dependencies = listOf<String>()
        )
        Log.d("add", "task build",)
        viewModel.addTask(task).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    PlanningTaskDetailsFragmentDirections.actionPlanningTaskDetailsFragmentToMenuItemListProfile()
                        .let {
                            findNavController().navigate(
                                it,

                            )
                        }
                }

                is MainViewModel.Status.Failure -> {
                    Log.e("add", "Failed to add item", status.e)
                    Snackbar.make(binding.root, "Failed to add item", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                add()
            }
        }
        return true
    }
}