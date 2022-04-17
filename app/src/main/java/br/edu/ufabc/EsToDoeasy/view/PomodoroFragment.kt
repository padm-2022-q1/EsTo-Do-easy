package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    /**
     * Filter criteria for tasks listing.
     */
    enum class FilterCriteria { ALL, FAVORITE, ARCHIVED }

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
    }

    private fun bindEvents() {

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
