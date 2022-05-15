package br.edu.ufabc.EsToDoeasy.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentProfilePageBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class ProfilePageFragment : Fragment() {
    private lateinit var binding: FragmentProfilePageBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                fillText()
                launchHome()
                Snackbar.make(
                    binding.root, "Login Successful",
                    Snackbar.LENGTH_LONG
                ).show()

            } else {
                Snackbar.make(
                    binding.root, "Failed to sign-in. Please try again.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
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
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        fillText()
        return binding.root
    }


    private fun launchAuthUi() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        launcher.launch(intent)
    }

    private fun launchProfile() {

        ProfilePageFragmentDirections.actionMenuItemListProfileToMenuItemListHome().let {
            findNavController().navigate(it)
        }
        Snackbar.make(
            binding.root, "Logout Successful",
            Snackbar.LENGTH_LONG
        ).show()

    }

    private fun launchHome() =
        ProfilePageFragmentDirections.actionMenuItemListProfileToMenuItemListHome().let {
            findNavController().navigate(it)
        }

    private fun fillText() {
        binding.profileEmailProfile.text = getCurrentEmail()
        binding.profileUsernameProfile.text = getCurrentName()
    }

    private fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid
        ?: ""
    //throw Exception("No user is signed in")

    private fun getCurrentEmail(): String = FirebaseAuth.getInstance().currentUser?.email
        ?: "Emailess"
    //throw Exception("No user is signed in")

    private fun getCurrentName(): String = FirebaseAuth.getInstance().currentUser?.displayName
        ?: "Nameless"

    private fun bindEvents() {

        binding.profileSignOut.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            launchProfile()
            //launchAuthUi()
            //viewModel.clickedSignOutProfile.value = true
        }

        binding.profileSignIn.setOnClickListener {
            launchAuthUi()
        }
        binding.profileSettingsButton.setOnClickListener {
            viewModel.clickedSettingsProfile.value = true
        }

        binding.profileAchievementsButton.setOnClickListener {
            viewModel.clickedAchievementProfile.value = true
        }
        if (getCurrentUser() == "")
            binding.profileSignIn.visibility = View.VISIBLE
        else binding.profileSignIn.visibility = View.INVISIBLE
    }


}