package com.example.autoreportgenerator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.ScanData
import com.example.autoreportgenerator.repo.HomeRepo
import com.example.autoreportgenerator.service.RetrofitChatService
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.HomeViewModelFactory
import com.example.autoreportgenerator.utils.OnClickListener
import com.example.autoreportgenerator.view.adapter.ScanAdapter
import com.example.autoreportgenerator.viewmodel.HomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(), OnClickListener {
    private lateinit var viewModel: HomeViewModel
    private lateinit var token: String
    private lateinit var userId: String
    private var isDoctor = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container)
        init(view)
        return view
    }

    private fun init(view: View) {
        val retrofitService = RetrofitService.getInstance()
        val chatRetrofitService = RetrofitChatService.getInstance()
        val homeRepository = HomeRepo(retrofitService, chatRetrofitService)
        val username = view.findViewById<TextView>(R.id.tv_name)
        val addScanBtn: FloatingActionButton = view.findViewById(R.id.btn_add_scan)
        val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        val tvNoHistory: TextView = view.findViewById(R.id.tv_no_history)

        isDoctor = false

        viewModel = ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory(homeRepository)
        )[HomeViewModel::class.java]

        val bundle = activity!!.intent.extras
        if (bundle != null) {
            username.text = bundle.getString("name").toString()
            token = bundle.getString("token").toString()
            userId = bundle.getString("userId").toString()
            isDoctor = bundle.getBoolean("isDoctor")
        }

        if (isDoctor) {
            viewModel.getAllScanData(token)
            //downloadButton.visibility = View.GONE
            addScanBtn.visibility = View.VISIBLE
        } else {
            viewModel.getAllReportData(token)
            //downloadButton.visibility = View.VISIBLE
            addScanBtn.visibility = View.GONE
        }

        viewModel.getAllListOfPatients(token)


        rv.layoutManager = LinearLayoutManager(activity)

        viewModel.homeScanData.observe(viewLifecycleOwner) { lists ->
            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false;
            if (lists.isNotEmpty()) {
                val adapter = ScanAdapter(lists, this)
                rv.adapter = adapter
                tvNoHistory.visibility = View.GONE
            } else {
                tvNoHistory.visibility = View.VISIBLE
            }
        }

        viewModel.homeUploadFile.observe(viewLifecycleOwner) { isFileUploaded ->
            if (!isFileUploaded)
                Toast.makeText(
                    activity,
                    "File was not uploaded successfully!! Please try again with valid inputs!",
                    Toast.LENGTH_LONG
                ).show()
        }

        viewModel.homeReportData.observe(viewLifecycleOwner) { lists ->
            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false;
            if (lists.isNotEmpty()) {
                val adapter = ScanAdapter(lists, this)
                rv.adapter = adapter
                tvNoHistory.visibility = View.GONE
            } else {
                tvNoHistory.visibility = View.VISIBLE
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            if (isDoctor)
                viewModel.getAllScanData(token)
            else
                viewModel.getAllReportData(token)
        }

        addScanBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("is_edit", false)
            bundle.putString("token", token)
            bundle.putString("user_id", userId)
            bundle.putBoolean("isDoctor", isDoctor)
            requireActivity().supportFragmentManager.let {
                ScanDialogPDFragment.newInstance(bundle)
                    .show(requireActivity().supportFragmentManager, "PatientDialog")
            }
        }

        logoutButton.setOnClickListener {
            requireActivity().finish()
        }
    }

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onItemClicked(scanData: ScanData) {

        val bundle = Bundle()
        bundle.putSerializable("data", scanData)
        bundle.putBoolean("is_edit", true)
        bundle.putString("token", token)
        bundle.putString("user_id", userId)
        bundle.putBoolean("isDoctor", isDoctor)
        requireActivity().supportFragmentManager.let {
            ScanDialogPDFragment.newInstance(bundle)
                .show(requireActivity().supportFragmentManager, "PatientDialog")
        }
    }
}