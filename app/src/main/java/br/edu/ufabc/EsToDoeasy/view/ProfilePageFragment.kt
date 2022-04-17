package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentProfilePageBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilePageFragment : Fragment() {
    private lateinit var binding : FragmentProfilePageBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        bindEvents()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilePageBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun bindEvents(){
        binding.profileSignOut.setOnClickListener{
            viewModel.clickedSignOutProfile.value = true
        }

        binding.profileSettingsButton.setOnClickListener{
            viewModel.clickedSettingsProfile.value = true
        }

        binding.profileAchievementsButton.setOnClickListener{
            viewModel.clickedAchievementProfile.value = true
        }

    }
}