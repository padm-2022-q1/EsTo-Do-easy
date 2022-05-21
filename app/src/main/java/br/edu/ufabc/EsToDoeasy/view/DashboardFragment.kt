package br.edu.ufabc.EsToDoeasy.view

import android.graphics.Color
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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


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

    private fun initComponents() {
        binding.dashboardChartTimeSpentByGroup.apply {
            description.isEnabled = false
            isDrawHoleEnabled = false
            isHighlightPerTapEnabled = false
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)
            animateY(1400, Easing.EaseInOutQuad)
        }

        setTimeSpentByGroupData()
    }

    private fun setTimeSpentByGroupData() {
        val entries = listOf(
            PieEntry(1f, "PADM"),
            PieEntry(2f, "TEST"),
            PieEntry(10f, "Grupo 3"),
        )

        val colors = arrayListOf<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())

        val dataSet = PieDataSet(entries, "Time spent by group").apply {
            setDrawIcons(false)
            sliceSpace = 1f
            this.colors = colors
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(DefaultValueFormatter(0))
            setValueTextSize(11f)
        }

        binding.dashboardChartTimeSpentByGroup.apply {
            this.data = data
            invalidate()
        }
    }

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
