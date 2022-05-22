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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import java.util.*


/**
 * Dashboard view.
 */
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val chartColors = arrayListOf(ColorTemplate.getHoloBlue()).apply {
        for (c in ColorTemplate.VORDIPLOM_COLORS) add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) add(c)
        for (c in ColorTemplate.PASTEL_COLORS) add(c)
    }

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
        binding.dashboardChartTopTasksTimeSpent.apply {
            description.isEnabled = false
            isHighlightPerTapEnabled = true
            legend.isEnabled = false
            setDrawValueAboveBar(true)
            setMaxVisibleValueCount(100)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
            }
            axisLeft.apply {
                axisMinimum = 0f
                isEnabled = false
            }
            axisRight.apply {
                axisMinimum = 0f
                isEnabled = false
            }
        }

        binding.dashboardChartTimeSpentByGroup.apply {
            description.isEnabled = false
            isDrawHoleEnabled = false
            isHighlightPerTapEnabled = true
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)
            rotationAngle = 180f
            legend.isEnabled = false
        }
    }

    private fun setTopTimeSpentTasksData(times: TaskTimes) {
        var space = 0f

        val values = times.groupBy { it.task }
            .map { g ->
                Pair(
                    g.key,
                    g.value.map { it.timeElapsed }.reduce { acc, v -> acc + v }.toFloat()
                )
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(5)
        val entries = values.map { BarEntry(space++, it.second) }

        Log.d("VIEW", values.toString())

        val dataSet =
            BarDataSet(entries, getString(R.string.chart_top_tasks_time_spent_label)).apply {
                setDrawIcons(false)
                this.colors = chartColors
            }

        val data = BarData(dataSet).apply {
            setValueFormatter(DefaultValueFormatter(0))
            setValueTextSize(11f)
        }

        binding.dashboardChartTopTasksTimeSpent.apply {
            this.data = data
            animateY(1400, Easing.EaseInOutQuad)
            xAxis.valueFormatter = IndexAxisValueFormatter(values.map {
                it.first.ifBlank { getString(R.string.chart_unknown) }
            })
            invalidate()
        }
    }

    private fun setTimeSpentByGroupData(times: TaskTimes) {
        val values = times.groupBy { it.group }
            .map { g ->
                Pair(
                    g.key.ifBlank { getString(R.string.chart_unknown) },
                    g.value.map { it.timeElapsed }.reduce { acc, v -> acc + v }.toFloat()
                )
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
        val entries = values.map { PieEntry(it.second, it.first) }

        val dataSet =
            PieDataSet(entries, getString(R.string.chart_time_spent_per_group_label)).apply {
                setDrawIcons(false)
                sliceSpace = 1f
                this.colors = chartColors
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
                    setTopTimeSpentTasksData(times)
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
