package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentDashboardBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Dashboard view.
 */
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initComponents()
        bindEvents()
    }

    private fun initComponents() {}

    private fun bindEvents() {
        context?.let {
            val activeColor = ContextCompat.getColor(it, R.color.purple_700)
            val inactiveColor = ContextCompat.getColor(it, R.color.purple_200)

            binding.reportButtonDiary.setOnClickListener {
                binding.reportButtonDiary.setBackgroundColor(activeColor)
                binding.reportButtonWeekly.setBackgroundColor(inactiveColor)
                binding.reportButtonMonthly.setBackgroundColor(inactiveColor)
            }

            binding.reportButtonWeekly.setOnClickListener {
                binding.reportButtonDiary.setBackgroundColor(inactiveColor)
                binding.reportButtonWeekly.setBackgroundColor(activeColor)
                binding.reportButtonMonthly.setBackgroundColor(inactiveColor)
            }

            binding.reportButtonMonthly.setOnClickListener {
                binding.reportButtonDiary.setBackgroundColor(inactiveColor)
                binding.reportButtonWeekly.setBackgroundColor(inactiveColor)
                binding.reportButtonMonthly.setBackgroundColor(activeColor)
            }
        }
    }
}
