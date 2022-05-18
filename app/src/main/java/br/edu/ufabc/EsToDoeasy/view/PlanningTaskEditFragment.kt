package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskEditBinding
import br.edu.ufabc.EsToDoeasy.model.*
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class PlanningTaskEditFragment : Fragment() {
    private var difficulty: String = ""
    private var priority: String = ""
    private lateinit var binding: FragmentPlanningTaskEditBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PlanningTaskEditFragmentArgs by navArgs()
    private var listGroups: List<Group> = listOf()

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

        initComponents()

        bindEvents()

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

    private fun initComponents() {
        viewModel.getTask(args.id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is MainViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to fetch items", result.e)
                }
                is MainViewModel.Status.Success -> {
                    val task = (result.result as MainViewModel.Result.SingleTask).value
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

                    binding.planningTaskDetailsTaskName.setText(task.title)

                    viewModel.getGroup(task.groupId).observe(viewLifecycleOwner) {   result ->
                        when (result) {
                            is MainViewModel.Status.Success -> {
                                val group = (result.result as MainViewModel.Result.SingleGroup).value.name
                                Log.d("GROUP","$group")
                                binding.autoCompleteTextViewProjectName.setText(group)
                            }
                        }
                    }

                    viewModel.getAllGroups().observe(viewLifecycleOwner) { status ->
                        when (status) {
                            is MainViewModel.Status.Success -> {
                                val groupsName = (status.result as MainViewModel.Result.GroupList).value.map { it.name }
                                val groupsID = (status.result as MainViewModel.Result.GroupList).value.map { it.name }
                                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_project_name_new_task, groupsName)
                                binding.autoCompleteTextViewProjectName.setAdapter(arrayAdapter)

                                for (i in groupsID){
                                    var count = 0
                                    if (i.toLong() == task.groupId){
                                        binding.autoCompleteTextViewProjectName.setSelection(count)

                                    }
                                    count++
                                }
                            }
                        }
                    }

                    binding.planningTaskDetailsDateStartEditText.setText(formatter.format(task.dateStarted))
                    binding.planningTaskDetailsDateDueEditText.setText(formatter.format(task.dateDue))

                    when (task.priority.name) {
                        binding.radioButtonPriorityLevelLow.text.toString().uppercase() ->
                            binding.planningDetailsPriorityLevelRadioGroup.check(
                                R.id.radio_button_priority_level_low
                            )
                        binding.radioButtonPriorityLevelMedium.text.toString().uppercase() ->
                            binding.planningDetailsPriorityLevelRadioGroup.check(
                                R.id.radio_button_priority_level_medium
                            )
                        binding.radioButtonPriorityLevelHigh.text.toString().uppercase() ->
                            binding.planningDetailsPriorityLevelRadioGroup.check(
                                R.id.radio_button_priority_level_high
                            )
                    }

                    when (task.difficulty.name) {
                        binding.radioButtonActivityLevelEasy.text.toString().uppercase() ->
                            binding.planningDetailsActivityLevelRadioGroup.check(
                                R.id.radio_button_activity_level_easy
                            )
                        binding.radioButtonActivityLevelMedium.text.toString().uppercase() ->
                            binding.planningDetailsActivityLevelRadioGroup.check(
                                R.id.radio_button_activity_level_medium
                            )
                        binding.radioButtonActivityLevelHard.text.toString().uppercase() ->
                            binding.planningDetailsActivityLevelRadioGroup.check(
                                R.id.radio_button_activity_level_hard
                            )
                    }
                }
            }
        }
    }

    private fun edit() {
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

        val groupName = binding.autoCompleteTextViewProjectName.text

        Log.d("edit", "Loading $groupName")

        val task = Task(
            id = args.id,
            userId = "",
            title = binding.planningTaskDetailsTaskName.text.toString(),
            details = binding.planningTaskDetailsMultLineTaskEditText.text.toString(),
            dateStarted = Task.parseDate(binding.planningTaskDetailsDateStartEditText.text.toString()),
            dateFinished = Task.parseDate("01/01/2000"),
            dateDue = Task.parseDate(binding.planningTaskDetailsDateDueEditText.text.toString()),
            timeElapsed = 0,
            groupId = listGroups.filter { it.name == groupName.toString() }.first().id, // TODO:
            difficulty = getDifficulty(binding.planningDetailsActivityLevelRadioGroup.checkedRadioButtonId),
            priority = getPriority(binding.planningDetailsPriorityLevelRadioGroup.checkedRadioButtonId),
            status = Status.TODO,
            dependencies = listOf<Long>()
        )

        Log.d("edit", "task build",)

        viewModel.updateTask(task).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    Log.d("edit", "deu certo")
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
                    binding.autoCompleteTextViewProjectName.setAdapter(arrayAdapter)
                }
            }
        }
    }
}