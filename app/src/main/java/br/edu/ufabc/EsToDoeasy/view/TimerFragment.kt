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
import br.edu.ufabc.EsToDoeasy.databinding.FragmentTimerBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
/**
 * Tasks list view.
 */
class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    private val viewModel: MainViewModel by activityViewModels()

    private fun updateTime(timeElapsed: Long) {
        val hours = timeElapsed / 3600
        val minutes = timeElapsed % 3600 / 60
        val seconds = timeElapsed % 60

        binding.text.text = getString(R.string.time_format, hours, minutes, seconds)
    }

    private fun formatStart() { // FIX:
        context?.let { ContextCompat.getColor(it, R.color.danger) }
            ?.let { binding.pomodoroActionButton.setBackgroundColor(it) }
        binding.pomodoroActionButton.text = getString(R.string.return_pause)
    }

    private fun formatStop() {// FIX:\
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
        binding = FragmentTimerBinding.inflate(inflater, container, false)
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
    }
}
