package com.example.autoreportgenerator.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.ScanData
import com.example.autoreportgenerator.model.ScanRequest
import com.example.autoreportgenerator.repo.HomeRepo
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.HomeViewModelFactory
import com.example.autoreportgenerator.viewmodel.HomeViewModel


class ScanDialogPDFragment : DialogFragment() {
    private lateinit var viewModel: HomeViewModel

    companion object {

        const val TAG = "DialogPDFragment"
        private var isEdit = false
        private var token = ""
        private var userId = ""
        private lateinit var scanData: ScanData


        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        const val pdfUrl = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"


        fun newInstance(bundle: Bundle): ScanDialogPDFragment {
            //val args = Bundle()
            //args.putString(KEY_TITLE, title)
            //args.putString(KEY_SUBTITLE, subTitle)
            val fragment = ScanDialogPDFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_scan, container, false)
        setUpData()
        setupView(view)
        return view
    }

    private fun setUpData() {
        isEdit = arguments?.getBoolean("is_edit", false) ?: false
        token = arguments?.getString("token", "") ?: ""
        userId = arguments?.getString("user_id", "") ?: ""
        scanData = (arguments?.getSerializable("data") ?: ScanData()) as ScanData
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
        val retrofitService = RetrofitService.getInstance()
        val homeRepository = HomeRepo(retrofitService)

        val patientNameET = view.findViewById<EditText>(R.id.patientNameEditText)
        val patientAgeET = view.findViewById<EditText>(R.id.patientAgeEditText)
        val doctorNameET = view.findViewById<EditText>(R.id.doctorEditText)
        val heightET = view.findViewById<EditText>(R.id.heightEditText)
        val weightET = view.findViewById<EditText>(R.id.weightEditText)
        val bmiNET = view.findViewById<EditText>(R.id.bmiEditText)
        val scannedOnET = view.findViewById<EditText>(R.id.scannedOnEditText)
        val fhrNameET = view.findViewById<EditText>(R.id.fhrEditText)
        val gaNameET = view.findViewById<EditText>(R.id.gaEditText)
        val dvpNameET = view.findViewById<EditText>(R.id.dvpEditText)
        val placentaLocNameET = view.findViewById<EditText>(R.id.placentaLocEditText)
        val summaryNameET = view.findViewById<EditText>(R.id.summaryLocEditText)
        val submitBtn = view.findViewById<Button>(R.id.btn_submit)

        viewModel = ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory(homeRepository)
        )[HomeViewModel::class.java]

        if (isEdit) {
            Toast.makeText(requireActivity(), "Edit is on..", Toast.LENGTH_LONG).show()
            scanData.apply {
                patientNameET.text = patientName?.toEditable()
                patientAgeET.text = patientAge?.toEditable()
                doctorNameET.text = scannedBy?.toEditable()
                heightET.text = height?.toEditable()
                weightET.text = weight?.toEditable()
                bmiNET.text = bmi?.toEditable()
                scannedOnET.text = scannedOn?.toEditable()
                fhrNameET.text = fhr?.toEditable()
                gaNameET.text = ga?.toEditable()
                dvpNameET.text = mvp?.toEditable()
                placentaLocNameET.text = placentaLocation?.toEditable()
                summaryNameET.text = summary?.toEditable()
            }

        } else
            Toast.makeText(requireActivity(), "Edit is off..", Toast.LENGTH_LONG).show()

        submitBtn.setOnClickListener {
            val scan = ScanRequest(
                userId = userId,
                patientName = patientNameET.text.toString(),
                patientAge = patientAgeET.text.toString(),
                height = heightET.text.toString(),
                weight = weightET.text.toString(),
                bmi = bmiNET.text.toString(),
                scannedBy = doctorNameET.text.toString(),
                isFirstVisit = true,
                scannedOn = scannedOnET.text.toString(),
                fhr = fhrNameET.text.toString(),
                ga = gaNameET.text.toString(),
                mvp = dvpNameET.text.toString(),
                placentaLocation = placentaLocNameET.text.toString(),
                summary = summaryNameET.text.toString()
            )
            viewModel.generateReportUploadScanData(scan, token)
            dismiss()
        }
    }

    private fun setupClickListeners(view: View) {
        /*view.btnPositive.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
        view.btnNegative.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }*/
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


}