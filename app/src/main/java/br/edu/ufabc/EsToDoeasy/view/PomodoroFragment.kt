package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPomodoroBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import br.edu.ufabc.EsToDoeasy.viewmodel.PomodoroViewModel
import kotlin.math.roundToInt

/**
 * Tasks list view.
 */
class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val pomodoroViewModel: PomodoroViewModel by activityViewModels()

    private fun updateTime(timeElapsed: Long) {
        val setting = pomodoroViewModel.currentState()
        val timeRemaining = setting.timer - timeElapsed
        binding.progressCircleDeterminate.setStepCountText(DateUtils.formatElapsedTime(timeRemaining))

        val angle = (timeElapsed.toFloat() / setting.timer.toFloat()) * 360
        binding.progressCircleDeterminate.setPercentage(angle.roundToInt())
    }

    private fun formatStart() { // FIX:
        context?.let { ContextCompat.getColor(it, R.color.danger) }
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.pomodoroSkip.setOnClickListener {
            pomodoroViewModel.nextState().observe(viewLifecycleOwner) { status ->
                when (status) {
                    is PomodoroViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to proceed to next state", status.e)
                    }
                    is PomodoroViewModel.Status.Success -> {
                        val result = (status.result as PomodoroViewModel.Result.StateResult).value

                        val timerText = DateUtils.formatElapsedTime(result.timer)
                        binding.progressCircleDeterminate.setStepCountText(timerText)
                        binding.pomodoroBack.setBackgroundResource(result.color)
                        binding.pomodoroActionButton.text = getString(result.action)
                    }
                }
            }
        }
    }
}
