package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentTimerBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Tasks list view.
 */
class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: TimerFragmentArgs by navArgs()

    private fun updateTime(timeElapsed: Long) {
        val hours = timeElapsed / 3600
        val minutes = timeElapsed % 3600 / 60
        val seconds = timeElapsed % 60

        binding.progressCircleDeterminate.setStepCountText(
            getString(
                R.string.time_format,
                hours,
                minutes,
                seconds
            )
        )
    }

    private fun formatStart() { // FIX:
        context?.let { ContextCompat.getColor(it, R.color.danger) }
            ?.let { binding.pomodoroActionButton.setBackgroundColor(it) }
        binding.pomodoroActionButton.text = getString(R.string.return_pause)
    }

    private fun formatStop() {// FIX:
        context?.let { ContextCompat.getColor(it, R.color.black) }
            ?.let { binding.pomodoroActionButton.setBackgroundColor(it) }
        binding.pomodoroActionButton.text = getString(R.string.stop)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
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
            finish()
        }
    }

    private fun finish() {
        viewModel.timeElapsed.value?.let { time ->
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
                                            "Time elapsed $time",
                                            Snackbar.LENGTH_LONG
                                        )
                                            .show()
                                        viewModel.timeElapsed.value = 0L
                                        TimerFragmentDirections.actionTimerFragmentToMenuItemListHome()
                                            .let {
                                                findNavController().navigate(it)
                                            }

                                        viewModel.state.value =
                                            if (viewModel.isTimerRunning()) MainViewModel.State.STOPPED
                                            else MainViewModel.State.STARTED
                                    }
                                }
                            }

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

