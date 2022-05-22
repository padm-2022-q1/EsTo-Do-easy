package br.edu.ufabc.EsToDoeasy.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentDashboardBinding
import br.edu.ufabc.EsToDoeasy.model.TaskTimes
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import java.util.*


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
            isHighlightPerTapEnabled = true
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)
            animateY(1400, Easing.EaseInOutQuad)
            legend.isEnabled = false
        }
    }

    private fun setTimeSpentByGroupData(times: TaskTimes) {
        val entries = times.groupBy { it.taskId }.map { g ->
            PieEntry(
                g.value.map { it.timeElapsed }.reduce { acc, v -> acc + v }.toFloat(),
                g.key.toString()
            )
        }.filter { it.value > 0 }

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
            animateY(1400, Easing.EaseInOutQuad)
            invalidate()
        }
    }

    private fun bindEvents() {
        binding.reportButtonDaily.setOnClickListener {
            viewModel.dashboardFilter.value = MainViewModel.DashboardFilter.DAILY
        }

        binding.reportButtonWeekly.setOnClickListener {
            viewModel.dashboardFilter.value = MainViewModel.DashboardFilter.WEEKLY
        }

        binding.reportButtonMonthly.setOnClickListener {
            viewModel.dashboardFilter.value = MainViewModel.DashboardFilter.MONTHLY
        }

        context?.let {
            val activeColor = ContextCompat.getColor(it, R.color.purple_700)
            val inactiveColor = ContextCompat.getColor(it, R.color.purple_200)

            viewModel.dashboardFilter.observe(viewLifecycleOwner) { filter ->
                binding.reportButtonDaily.setBackgroundColor(if (filter == MainViewModel.DashboardFilter.DAILY) activeColor else inactiveColor)
                binding.reportButtonWeekly.setBackgroundColor(if (filter == MainViewModel.DashboardFilter.WEEKLY) activeColor else inactiveColor)
                binding.reportButtonMonthly.setBackgroundColor(if (filter == MainViewModel.DashboardFilter.MONTHLY) activeColor else inactiveColor)

                val startDate = when (filter) {
                    MainViewModel.DashboardFilter.DAILY -> dailyDate()
                    MainViewModel.DashboardFilter.WEEKLY -> weeklyDate()
                    MainViewModel.DashboardFilter.MONTHLY -> monthlyDate()
                }
                val endDate = when (filter) {
                    MainViewModel.DashboardFilter.DAILY -> addDate(startDate, Calendar.DATE, 1)
                    MainViewModel.DashboardFilter.WEEKLY -> addDate(
                        startDate,
                        Calendar.WEEK_OF_YEAR,
                        1
                    )
                    MainViewModel.DashboardFilter.MONTHLY -> addDate(startDate, Calendar.MONTH, 1)
                }

                getData(startDate, endDate)
            }
        }
    }

    private fun getData(startDate: Date, endDate: Date) {
        viewModel.getAllTaskTimes(startDate, endDate).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to fetch times", status.e)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.dashboard_data_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                is MainViewModel.Status.Success -> {
                    val times = (status.result as MainViewModel.Result.TimeList).value
                    setTimeSpentByGroupData(times)
                }
            }
        }
    }

    private fun addDate(date: Date, unit: Int, value: Int): Date =
        Calendar.getInstance().apply {
            time = date
            add(unit, value)
        }.time

    private fun dailyDate(): Date =
        Calendar.getInstance().apply {
            time = Date()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

    private fun weeklyDate(): Date =
        Calendar.getInstance().apply {
            time = dailyDate()
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }.time

    private fun monthlyDate(): Date =
        Calendar.getInstance().apply {
            time = dailyDate()
            set(Calendar.DAY_OF_MONTH, 1)
        }.time
}
