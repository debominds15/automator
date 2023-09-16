package com.example.autoreportgenerator.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.PatientData
import com.example.autoreportgenerator.model.ScanData
import com.example.autoreportgenerator.model.ScanRequest
import com.example.autoreportgenerator.model.chatgpt.GeneratedSummary
import com.example.autoreportgenerator.model.chatgpt.PatientInputParams
import com.example.autoreportgenerator.repo.HomeRepo
import com.example.autoreportgenerator.service.RetrofitChatService
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.HomeViewModelFactory
import com.example.autoreportgenerator.utils.Util
import com.example.autoreportgenerator.viewmodel.HomeViewModel
import com.mahdiasd.filepicker.FileModel
import com.mahdiasd.filepicker.FilePicker
import com.mahdiasd.filepicker.FilePickerListener
import com.mahdiasd.filepicker.PickerMode


class ScanDialogPDFragment : DialogFragment() {
    private lateinit var viewModel: HomeViewModel

    companion object {

        const val TAG = "DialogPDFragment"
        private var isEdit = false
        private var token = ""
        private var userId = ""
        private var isDoctor = false
        private var patientId = ""
        private var patientName = ""
        private var jsonFilePath = ""
        private lateinit var jsonData: PatientInputParams
        private lateinit var scanData: ScanData
        private val READ_STORAGE_PERMISSION_REQUEST = 101


        fun newInstance(bundle: Bundle): ScanDialogPDFragment {
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
        isDoctor = arguments?.getBoolean("isDoctor", false) ?: false
        scanData = (arguments?.getSerializable("data") ?: ScanData()) as ScanData
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
        val chatRetrofitService = RetrofitChatService.getInstance()
        val homeRepository = HomeRepo(retrofitService, chatRetrofitService)

        //val patientNameET = view.findViewById<EditText>(R.id.patientNameEditText)
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
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val patientSpinner = view.findViewById<Spinner>(R.id.role_spinner)
        val ivFile = view.findViewById<ImageView>(R.id.iv_file)
        val selectedFilePath = view.findViewById<TextView>(R.id.tv_select)
        val generateSummary = view.findViewById<ImageView>(R.id.iv_gen_summary)
        val generateSummaryLayout = view.findViewById<RelativeLayout>(R.id.layout_gen_summary)

        viewModel = ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory(homeRepository)
        )[HomeViewModel::class.java]

        ivFile.setOnClickListener {
            FilePicker(requireActivity(), requireActivity().supportFragmentManager)
                .setMode(PickerMode.File)
                .setListener(object : FilePickerListener {

                    override fun selectedFiles(files: List<FileModel>?, uris: List<Uri>?) {
                        selectedFilePath.text = "Path: ${files?.get(0)?.path}"
                        jsonFilePath = "Path: ${files?.get(0)?.path}"
                        readJsonFile(jsonFilePath)
                    }
                })
                .show()
        }

        generateSummary.setOnClickListener {
            if (selectedFilePath.text.isNotEmpty() && selectedFilePath.text != "Select file")
                viewModel.sendDataToGenerateSummary(jsonData)
            else
                Toast.makeText(
                    requireActivity(),
                    "Please select a json file first!",
                    Toast.LENGTH_LONG
                ).show()
        }

        viewModel.homePatientsData.observe(viewLifecycleOwner) { lists ->
            //progressBar.visibility = View.GONE
            if (lists.isNotEmpty()) {
                var patientLists = arrayListOf<String>()
                if (isDoctor) {
                    patientLists = arrayListOf("Select patient")
                    patientLists.addAll(lists.map {
                        "ID: ${it.Id}, ${
                            it.name.toString().uppercase()
                        }"
                    })
                } else {
                    val patient: PatientData? = lists.find { userId == it.Id }
                    patientLists.add("ID: ${patient?.Id}, ${patient?.name.toString().uppercase()}")
                }

                val adapter: ArrayAdapter<*> =
                    ArrayAdapter(requireActivity(), R.layout.item_selected_spinner, patientLists)
                adapter.setDropDownViewResource(R.layout.item_dropdown_spinner)
                patientSpinner.adapter = adapter
                patientSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            patientName = parent.getItemAtPosition(position).toString()
                            patientId = if (position > 0) lists[position - 1].Id ?: "" else ""
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            } else {
                Toast.makeText(requireActivity(), "Could not fetch patients!", Toast.LENGTH_LONG)
                    .show()
            }
        }

        viewModel.homeState.observe(viewLifecycleOwner) { isValidated ->
            progressBar.visibility = View.GONE
            if (isValidated) {
                viewModel.getAllScanData(token)
                dismiss()
            }
        }

        viewModel.homeDeleteOldFile.observe(viewLifecycleOwner) { isDeleted ->
            if (!isDeleted) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    requireActivity(),
                    "Old report file was not found!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.homeEvent.observe(viewLifecycleOwner) { response ->
            progressBar.visibility = View.GONE

            when (response) {

                is HomeViewModel.HomeEvent.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is HomeViewModel.HomeEvent.Success -> {
                    progressBar.visibility = View.GONE
                    when (response.homeResponse) {
                        is GeneratedSummary -> {
                            val summary =
                                "${response.homeResponse.choices[0].message?.content ?: "Summary not found"}\n\n\n Report Condition: ${
                                    viewModel.getReportNormalcyData(
                                        jsonData
                                    )
                                }"
                            summaryNameET.text = summary.toEditable()
                            fhrNameET.text = jsonData.FetalHeartRate?.toEditable()
                            gaNameET.text = jsonData.gestationalAge.toString().toEditable()
                            dvpNameET.text = jsonData.dVPinCM.toString().toEditable()
                            placentaLocNameET.text = jsonData.PlacentaLocation?.toEditable()
                        }
                        else -> {

                        }
                    }
                }
                else -> {

                }
            }
        }

        if (isDoctor) {
            submitBtn.visibility = View.VISIBLE
            generateSummaryLayout.visibility = View.VISIBLE
            selectedFilePath.visibility = View.VISIBLE
            ivFile.visibility = View.VISIBLE
        } else {
            submitBtn.visibility = View.GONE
            generateSummaryLayout.visibility = View.GONE
            selectedFilePath.visibility = View.GONE
            ivFile.visibility = View.GONE
            patientSpinner.isEnabled = false
        }

        if (isEdit) {
            Toast.makeText(requireActivity(), "Edit is on..", Toast.LENGTH_LONG).show()
            scanData.apply {
                //patientNameET.text = patientName?.toEditable()
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
            progressBar.visibility = View.VISIBLE
            val scan = ScanRequest(
                userId = userId,
                patientId = patientId,
                patientName = patientName,
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
                summary = summaryNameET.text.toString(),
                publicId = scanData.publicId,
                pdfUrl = scanData.pdfUrl
            )
            if (isEdit)
                viewModel.updateReportUploadScanData(scan, scanData.Id ?: "", token)
            else
                viewModel.generateReportUploadScanData(scan, token)
        }
    }

    private fun readJsonFile(path: String) {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST
            )
        } else {
            // Permission is already granted; you can read the JSON file here
            jsonData = Util.readAndParseJSON("/Temp/result.json", requireContext())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now read the JSON file
                jsonData =
                    Util.readAndParseJSON(/*jsonFilePath*/"/Temp/result.json", requireContext())
            } else {
                Toast.makeText(requireActivity(), "Permission is denied!", Toast.LENGTH_LONG).show()
                // Permission denied, handle accordingly
            }
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


}