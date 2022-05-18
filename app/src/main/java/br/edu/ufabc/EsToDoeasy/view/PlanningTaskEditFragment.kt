package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskEditBinding
import br.edu.ufabc.EsToDoeasy.model.Difficulty
import br.edu.ufabc.EsToDoeasy.model.Priority
import br.edu.ufabc.EsToDoeasy.model.Status
import br.edu.ufabc.EsToDoeasy.model.Task
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class PlanningTaskEditFragment : Fragment() {
    private var difficulty: String = ""
    private var priority: String = ""
    private lateinit var binding: FragmentPlanningTaskEditBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PlanningTaskEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanningTaskEditBinding.inflate(inflater, container, false)
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
        inflater.inflate(R.menu.menu_task_details, menu)
        inflater.inflate(R.menu.menu_task_form, menu)
    }

    override fun onStart() {
        super.onStart()
        // TODO: fazer a tradução dos Radios para Difficulty e prioriry
        binding.selectStartDate.setOnClickListener{ it ->
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            // we have to implement setFragmentResultListener
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    binding.planningTaskDetailsDateStartEditText.setText(date)
                }
            }

            // show
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        binding.selectDueDate.setOnClickListener{ it ->
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            // we have to implement setFragmentResultListener
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    binding.planningTaskDetailsDateDueEditText.setText(date)
                }
            }
            // show
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        binding.planningDetailsActivityLevelRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            difficulty = checkedId.toString()
            Log.d("DiFF","$difficulty")
        }

        binding.planningDetailsPriorityLevelRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            priority = checkedId.toString()
        }
    }

    fun edit() {
        fun toDifficulty() = when (difficulty) {
            "Easy" -> {
                Difficulty.EASY
            }
            "Medium" -> {
                Difficulty.MEDIUM
            }
            else ->{
                Difficulty.HARD
            }
        }

        val task = Task(
            id = 0,
            userId = "",
            title = binding.planningTaskDetailsTaskName.text.toString(),
            details = binding.planningTaskDetailsMultLineTaskEditText.text.toString(),
            dateStarted = Task.parseDate(binding.planningTaskDetailsDateStartEditText.text.toString()),
            dateFinished = Task.parseDate("01/01/2000"),
            dateDue = Task.parseDate(binding.planningTaskDetailsDateDueEditText.text.toString()),
            timeElapsed = 0,
            groupId = 1, // TODO:
            difficulty = toDifficulty(),
            priority = Priority.LOW,
            status = Status.TODO,
            dependencies = listOf<Long>()
        )
        Log.d("edit", "task build",)
        viewModel.addTask(task).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    Log.d("add", "deu certo")
                    PlanningTaskEditFragmentDirections.editedTask()
                        .let {
                            findNavController().popBackStack()
                        }
                }

                is MainViewModel.Status.Failure -> {
                    Log.e("edit", "Failed to edit item", status.e)
                    Snackbar.make(binding.root, "Failed to edit item", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                edit()
            }
        }
        return true
    }
}