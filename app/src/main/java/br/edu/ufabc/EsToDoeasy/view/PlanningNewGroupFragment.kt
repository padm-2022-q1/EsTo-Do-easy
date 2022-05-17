package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentNewGroupBinding
import br.edu.ufabc.EsToDoeasy.model.*
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class PlanningNewGroupFragment : Fragment(){
    private lateinit var binding: FragmentNewGroupBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_group, menu)
    }

    private fun addGroup() {
        val group = Group(
            id = "",
            userId = "h1MW2FdpRqOFBmPIF5ncDsRF48q1",
            name = binding.planningTaskDetailsNameStartEditText.text.toString(),
        )
        Log.d("add", "task build",)
        viewModel.addGroup(group).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    PlanningNewGroupFragmentDirections.createdNewGroup()
                        .let {
                            findNavController().navigate(it, navOptions {
                                popUpTo(R.id.created_new_group)
                            })
                        }
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("add", "Failed to add item", status.e)
                    Snackbar.make(binding.root, "Failed to add item", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                addGroup()
            }
        }
        return true
    }
}