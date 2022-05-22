package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentStudyTechniquesSelectBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Study techniques list view.
 */
class SortBySelectFragment : Fragment() {
    private lateinit var binding: FragmentStudyTechniquesSelectBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyTechniquesSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {

        binding.recyclerviewStudyTechniquesList.apply {
            adapter = SortByAdapter(
                listOf("Default Sort", "Easiest first","Hardest first"),
                viewModel
            )
        }
    }
}
