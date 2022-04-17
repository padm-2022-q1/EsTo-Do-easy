package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ufabc.EsToDoeasy.R


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileSettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_settings, container, false)
    }

    override fun onStart() {
        super.onStart()
    }
}