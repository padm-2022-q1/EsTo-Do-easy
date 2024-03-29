package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskNewBinding
import br.edu.ufabc.EsToDoeasy.model.*
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class PlanningTaskNewFragment : Fragment() {
    private var difficulty: String = ""
    private var priority: String = ""
    private lateinit var binding: FragmentPlanningTaskNewBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var listGroups: List<Group> = listOf()
    private val args: PlanningTaskNewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanningTaskNewBinding.inflate(inflater, container, false)
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
        inflater.inflate(R.menu.menu_task_form, menu)
    }

    override fun onStart() {
        super.onStart()

        bindEvents()

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
        binding.addDependencies.setOnClickListener{
            PlanningTaskNewFragmentDirections.actionPlanningTaskNewFragmentToFragmentSelectDependencies()
                .let {
                    findNavController().navigate(it)
                }
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

    private fun add() {
        fun getPriority (id: Int) = when (id) {
            binding.radioButtonPriorityLevelLow.id -> Priority.LOW
            binding.radioButtonPriorityLevelMedium.id -> Priority.MEDIUM
            else -> Priority.HIGH
        }

        fun getDifficulty (id: Int) = when (id) {
            binding.radioButtonActivityLevelEasy.id -> Difficulty.EASY
            binding.radioButtonActivityLevelMedium.id -> Difficulty.MEDIUM
            else -> Difficulty.HARD
        }

        val groupName = binding.planningNewTaskAutoCompleteTextViewProjectName.text

        val task = Task(
            id = 0,
            userId = "",
            title = binding.planningTaskDetailsTaskName.text.toString(),
            details = binding.planningTaskDetailsMultLineTaskEditText.text.toString(),
            dateStarted = Task.parseDate(binding.planningTaskDetailsDateStartEditText.text.toString()),
            dateFinished = Task.parseDate("01/01/2000"),
            dateDue = Task.parseDate(binding.planningTaskDetailsDateDueEditText.text.toString()),
            timeElapsed = 0,
            groupId = listGroups.filter { it.name == groupName.toString() }.first().id,
            difficulty = getDifficulty(binding.planningDetailsActivityLevelRadioGroup.checkedRadioButtonId),
            priority = getPriority(binding.planningDetailsPriorityLevelRadioGroup.checkedRadioButtonId),
            status = Status.TODO,
            dependencies = viewModel.selectedDependencies.value?.toList() as List<Long>
        )

        viewModel.selectedDependencies.value!!.clear()
        Log.d("add", "task build",)

        viewModel.addTask(task).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    Log.d("add", "task criada com sucesso")
                    PlanningTaskEditFragmentDirections.actionPlanningTaskDetailsFragmentToMenuItemListProfile()
                        .let {
                            findNavController().popBackStack()
                        }
                    Snackbar.make(binding.root, "Task created successfully", Snackbar.LENGTH_LONG).show()
                }

                is MainViewModel.Status.Failure -> {
                    Log.e("add", "Failed to add task", status.e)
                    Snackbar.make(binding.root, "Failed to add task", Snackbar.LENGTH_LONG).show()
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

    private fun bindEvents(){
        viewModel.getAllGroups().observe(viewLifecycleOwner, ) { status ->
            when (status) {
                is MainViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to fetch items", status.e)
                }
                is MainViewModel.Status.Success -> {
                    val groups = (status.result as MainViewModel.Result.GroupList).value.map { it.name }
                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_project_name_new_task, groups)
                    listGroups = (status.result as MainViewModel.Result.GroupList).value
                    binding.planningNewTaskAutoCompleteTextViewProjectName.setAdapter(arrayAdapter)

                    binding.planningNewTaskAutoCompleteTextViewProjectName.setText(status.result.value.filter { it.id == args.id }.first().name)

                }
            }
        }
    }
}