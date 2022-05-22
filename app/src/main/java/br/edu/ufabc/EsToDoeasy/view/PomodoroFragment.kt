package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPomodoroBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import br.edu.ufabc.EsToDoeasy.viewmodel.PomodoroViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

/**
 * Tasks list view.
 */
class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val pomodoroViewModel: PomodoroViewModel by activityViewModels()
    private val args: PomodoroFragmentArgs by navArgs()

    private fun updateTime(timeElapsed: Long) {
        val setting = pomodoroViewModel.currentState()
        val timeRemaining = setting.timer - timeElapsed
        binding.progressCircleDeterminate.setStepCountText(DateUtils.formatElapsedTime(timeRemaining))

        val angle = (timeElapsed.toFloat() / setting.timer.toFloat()) * 360
        binding.progressCircleDeterminate.setPercentage(if (angle.roundToInt() <= 0) 1 else angle.roundToInt())

        if (timeRemaining <= 0) {
            nextState()
        }
    }

    private fun formatStart() { // FIX:
        context?.let { ContextCompat.getColor(it, R.color.bluedark) }
            ?.let { binding.pomodoroActionButton.setBackgroundColor(it) }
        binding.pomodoroActionButton.text = getString(R.string.return_pause)
    }

    private fun formatStop() {// FIX:
        context?.let { ContextCompat.getColor(it, R.color.black) }
            ?.let { binding.pomodoroActionButton.setBackgroundColor(it) }
        binding.pomodoroActionButton.text = getString(R.string.pomodoro_pause_focus)
    }

    private fun registerObservers() {
        viewModel.state.observe(this) {
            it?.let { state ->
                when (state) {
                    MainViewModel.State.STARTED -> formatStop()
                    MainViewModel.State.STOPPED -> formatStart()
                    MainViewModel.State.INITIAL -> {
                        viewModel.timeElapsed.value = 0
                        formatStart()
                    }
                }
            }
        }

        viewModel.timeElapsed.observe(this) {
            it?.let { timeElapsed -> updateTime(timeElapsed) }
        }

        pomodoroViewModel.restartState().observe(viewLifecycleOwner) { status ->
            when (status) {
                is PomodoroViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to restart state", status.e)
                }
                is PomodoroViewModel.Status.Success -> {
                    val result =
                        (status.result as PomodoroViewModel.Result.StateResult).value

                    val timerText = DateUtils.formatElapsedTime(result.timer)
                    binding.progressCircleDeterminate.setStepCountText(timerText)
                    binding.pomodoroBack.setBackgroundResource(result.color)
                    viewModel.timeElapsed.value?.let { updateTime(it) }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                    isEnabled = true
                    Snackbar.make(
                        binding.root,
                        "Finish the current Timer first",
                        Snackbar.LENGTH_LONG
                    ).show()

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_timer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done -> {
                finishTask()
            }
            else -> Log.e("VIEW", "Invalid option ${item.itemId} selected")
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        bindEvents()
        registerObservers()
    }


    private fun bindEvents() {
        binding.pomodoroActionButton.setOnClickListener {
            viewModel.state.value = if (viewModel.isTimerRunning()) {
                MainViewModel.State.STOPPED
            } else {
                MainViewModel.State.STARTED
            }
        }

        binding.pomodoroConfigure.setOnClickListener {
            viewModel.clickedAtConfigPomodoro.value = true
        }

        binding.pomodoroFinishButton.setOnClickListener {
            viewModel.tasks.value = mutableListOf()
            finish()
        }

        binding.pomodoroSkip.setOnClickListener {
            nextState()
        }
    }

    private fun finish() {
        viewModel.timeElapsed.value?.let { elapsed ->
            pomodoroViewModel.currentTimer.value?.let { current ->
                val time = elapsed + current

                // TODO: Check how to avoid nested observers.
                viewModel.updateTask(args.id, time / 60).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is MainViewModel.Status.Failure -> {
                            Log.e("VIEW", "Failed to fetch items", result.e)
                        }
                        is MainViewModel.Status.Success -> {
                            Log.d("UPDATE", "Update")

                            viewModel.addTaskTime(args.id, time / 60)
                                .observe(viewLifecycleOwner) { result ->
                                    when (result) {
                                        is MainViewModel.Status.Failure -> {
                                            Log.e("VIEW", "Failed to fetch items", result.e)
                                        }
                                        is MainViewModel.Status.Success -> {
                                            Log.d("UPDATE", "Send Time Tracker")

                                            Snackbar.make(
                                                binding.root,
                                                "Time elapsed ${DateUtils.formatElapsedTime(time)}",
                                                Snackbar.LENGTH_LONG
                                            ).show()

                                            viewModel.timeElapsed.value = 0L
                                            pomodoroViewModel.currentTimer.value = 0L
                                            PomodoroFragmentDirections.backToHome().let {
                                                findNavController().navigate(it)
                                            }

                                            viewModel.state.value =
                                                if (viewModel.isTimerRunning()) MainViewModel.State.INITIAL
                                                else MainViewModel.State.STARTED
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    private fun nextState() {
        viewModel.timeElapsed.value?.let { time ->
            pomodoroViewModel.nextState(time).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is PomodoroViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to proceed to next state", status.e)
                    }
                    is PomodoroViewModel.Status.Success -> {
                        val result = (status.result as PomodoroViewModel.Result.StateResult).value

                        val timerText = DateUtils.formatElapsedTime(result.timer)
                        binding.progressCircleDeterminate.setStepCountText(timerText)
                        binding.pomodoroBack.setBackgroundResource(result.color)

                        viewModel.timeElapsed.value = 0
                    }
                    else -> {
                        Log.i("VIEW", "Received status ${status}")
                    }
                }
            }
        }
    }

    private fun finishTask() {
        viewModel.finishTask(args.id).observe(viewLifecycleOwner) {
            when (it) {
                is MainViewModel.Status.Success -> {
                    finish()
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to finish task", it.e)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.task_details_finish_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }



}
