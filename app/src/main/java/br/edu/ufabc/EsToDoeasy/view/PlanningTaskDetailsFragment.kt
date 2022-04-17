package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPlanningTaskDetailsBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

class PlanningTaskDetailsFragment : Fragment(){
    private lateinit var binding: FragmentPlanningTaskDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PlanningTaskDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanningTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val task = viewModel.get(args.id)

        binding.planningDetailsTitle.text = task.title
    }

}