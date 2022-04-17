package br.edu.ufabc.EsToDoeasy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.edu.ufabc.EsToDoeasy.databinding.ActivityMainBinding
import br.edu.ufabc.EsToDoeasy.view.*
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * The main activity.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        bindEvents()
    }

    private fun initComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val menu: BottomNavigationView = binding.mainNavigation
        val allBadge = menu.getOrCreateBadge(R.id.menu_item_list_home)
        allBadge.isVisible = true
        allBadge.number = viewModel.getAll().size

        val plannerBadge = menu.getOrCreateBadge(R.id.menu_item_list_planner)
        plannerBadge.isVisible = true
        plannerBadge.number = viewModel.getAll().size

//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.menu_item_list_home, R.id.menu_item_list_planner,R.id.menu_item_list_schedule
//
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)

        menu.setupWithNavController(navController)

    }

    private fun bindEvents() {
        // details
        viewModel.clickedItemId.observe(this) {
            it?.let {
                val action = HomeFragmentDirections.tasksShowDetails(it)
                navController.navigate(action)
            }
        }

        viewModel.clickedScheduledTaskId.observe(this) {
            it?.let {
                val action = ScheduleFragmentDirections.showTaskDetails(it)
                navController.navigate(action)
            }
        }

        // play
        viewModel.clickedTaskToPlay.observe(this) {
            it?.let {
                val action = HomeFragmentDirections.actionNavigationListToNavigationPomodoro()
                navController.navigate(action)
            }
        }

        viewModel.clickedStudyTechniqueSelect.observe(this) {
            it?.let {
                if (it) {
                    val action =
                        HomeFragmentDirections.actionNavigationListToNavigationStudySelect()
                    navController.navigate(action)
                } else {
                    navController.popBackStack()
                }
            }
        }


        viewModel.clickedAtDetails.observe(this) {
            it?.let {
                val action = TaskDetailsFragmentDirections.actionTaskDetailsToTaskDetails(it)
                navController.navigate(action)
            }
        }

        viewModel.clickedAtConfigPomodoro.observe(this) {
            it?.let {
                val action = PomodoroFragmentDirections.actionNavigationPomodoroToSettingPomodoro()
                navController.navigate(action)
            }
        }

        viewModel.clickedSignOutProfile.observe(this) {
            it?.let {
                val action =
                    ProfilePageFragmentDirections.actionMenuItemListProfileToNavigationLoginProfile()
                navController.navigate(action)
            }
        }

        viewModel.clickedLoginLogin.observe(this) {
            it?.let {
                val action = LoginPageDirections.actionNavigationLoginProfileToMenuItemListProfile()
                navController.navigate(action)
            }
        }

        viewModel.clickedSettingsProfile.observe(this) {
            it?.let {
                val action =
                    ProfilePageFragmentDirections.actionMenuItemListProfileToNavigationSettingsProfile()
                navController.navigate(action)
            }
        }

        viewModel.clickedSkipLogin.observe(this) {
            it?.let {
                val action = LoginPageDirections.actionNavigationLoginProfileToMenuItemListProfile()
                navController.navigate(action)
            }
        }

        viewModel.clickedGroupId.observe(this) {
            it?.let {
                val action =
                    PlanningListGroupFragmentDirections.actionPlanningListFragmentToPlanningListTaskFragment(
                        it
                    )
                navController.navigate(action)
            }
        }

        viewModel.clickedPlanningTaskId.observe(this) {
            it?.let {
                val action =
                    PlanningListTaskFragmentDirections.actionPlanningListTaskFragmentToPlanningTaskDetailsFragment(
                        it
                    )
                navController.navigate(action)
            }
        }

        viewModel.clickedAchievementProfile.observe(this) {
            it?.let {
                val action =
                    ProfilePageFragmentDirections.actionMenuItemListProfileToNavigationAchievementsProfile()
                navController.navigate(action)
            }
        }

        viewModel.clickedDashboardDaily.observe(this) {
            it?.let {
                val action =
                    ProfilePageFragmentDirections.actionMenuItemListProfileToNavigationAchievementsProfile()
                navController.navigate(action)
            }
        }

        viewModel.clickedAtAddTask.observe(this) {
            it?.let {
                val action = HomeFragmentDirections.actionNavigationListToNavigationPlanner()
                navController.navigate(action)
            }
        }
    }
}

