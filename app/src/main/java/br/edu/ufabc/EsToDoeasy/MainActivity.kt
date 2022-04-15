package br.edu.ufabc.EsToDoeasy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import br.edu.ufabc.EsToDoeasy.databinding.ActivityMainBinding
import br.edu.ufabc.EsToDoeasy.view.HomeFragmentDirections
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

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
    }

    private fun bindEvents() {
        viewModel.clickedItemId.observe(this) {
            it?.let {
                val action = HomeFragmentDirections.tasksShowDetails(it)
                navController.navigate(action)
            }
        }
    }
}
