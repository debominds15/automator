package com.example.autoreportgenerator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.autoreportgenerator.R

class ViewerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_scan, container)
    }

    companion object {
        fun getInstance(): ViewerFragment {
            return ViewerFragment()
        }
    }
}