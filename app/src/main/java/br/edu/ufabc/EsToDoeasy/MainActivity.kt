package br.edu.ufabc.EsToDoeasy

import android.os.Bundle
import android.view.View
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
        setSupportActionBar(binding.mainToolbar);

        initComponents()
        bindEvents()
    }

    private fun initComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.mainNavigation.visibility = View.VISIBLE

        val menu: BottomNavigationView = binding.mainNavigation
        val allBadge = menu.getOrCreateBadge(R.id.menu_item_list_home)
        allBadge.isVisible = true
        //allBadge.number = viewModel.getAllDueTasks().size

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
                    ProfilePageFragmentDirections.actionMenuItemListProfileSelf()
                navController.navigate(action)
                //binding.mainNavigation.visibility = View.GONE
            }
        }

        viewModel.clickedLoginLogin.observe(this) {
            it?.let {
                val action = LoginPageDirections.actionNavigationLoginProfileToMenuItemListProfile()
                navController.navigate(action)
                binding.mainNavigation.visibility = View.VISIBLE
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
                binding.mainNavigation.visibility = View.VISIBLE
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

        viewModel.clickedAddNewGroup.observe(this) {
            it?.let {
                val action = PlanningListGroupFragmentDirections.actionMenuItemListPlannerToPlanningNewGroupFragment()
                navController.navigate(action)
            }
        }

        viewModel.clickedAddNewTask.observe(this) {
            it?.let {
                val action = PlanningListTaskFragmentDirections.actionPlanningListTaskFragmentToAddNewTask()
                navController.navigate(action)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        binding.mainNavigation.visibility = View.VISIBLE
    }
}

