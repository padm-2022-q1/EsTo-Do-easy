package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPomodoroBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Tasks list view.
 */
class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val time: Long = 25*60*1000


    private fun updateTime(timeElapsed: Long) {

        val minutes = timeElapsed % 3600 / 60
        val seconds = timeElapsed % 60
        binding.text.text = getString(R.string.time_format_pomodoro, 24-minutes, 59-seconds)

        binding.progressCircleDeterminate.progress = (25*60) - (timeElapsed).toInt()
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
            it?.let { timeElapsed -> updateTime(timeElapsed)}
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
            // check the current state of this page through the time
            val status = binding.text.text

            if (status.equals("24:59")) {
                binding.text.text = getString(R.string.pomodoro_shortbreak)
                binding.pomodoroBack.setBackgroundResource(R.color.green)
                binding.pomodoroActionButton.text = getString(R.string.skip_break_label)
            } else if (status.equals("4:59")) {
                binding.text.text = getString(R.string.pomodoro_longbreak)
                binding.pomodoroActionButton.text = getString(R.string.skip_break_label)
                binding.pomodoroBack.setBackgroundResource(R.color.purple)
            } else if (status.equals("14:59")) {
                binding.text.text = getString(R.string.pomodoro_end_session)
                binding.pomodoroActionButton.text = getString(R.string.pomodoro_start_new_session)
                binding.pomodoroBack.setBackgroundResource(R.color.yellow)
            } else {
                binding.text.text = getString(R.string.pomodoro_focus)
                binding.pomodoroActionButton.text = getString(R.string.pomodoro_pause_focus)
                binding.pomodoroBack.setBackgroundResource(R.color.orange)
            }


        }
    }
}
