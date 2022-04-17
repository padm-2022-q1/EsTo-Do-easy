package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentLoginPageBinding
import br.edu.ufabc.EsToDoeasy.databinding.FragmentProfilePageBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [LoginPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginPage : Fragment() {
    private lateinit var binding : FragmentLoginPageBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginPageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bindEvents()
    }

    private fun bindEvents(){
        binding.loginSkipLogin.setOnClickListener{
            viewModel.clickedSkipLogin.value = true
        }

        binding.loginLoginButton.setOnClickListener{
            viewModel.clickedLoginLogin.value = true
        }

    }

}