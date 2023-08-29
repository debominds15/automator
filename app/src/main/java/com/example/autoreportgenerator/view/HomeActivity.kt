package com.example.autoreportgenerator.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.LoginUser
import com.example.autoreportgenerator.model.RegistrationUser
import com.example.autoreportgenerator.model.ScanRequest
import com.example.autoreportgenerator.repo.HomeRepo
import com.example.autoreportgenerator.repo.LoginRepo
import com.example.autoreportgenerator.service.RetrofitService
import com.example.autoreportgenerator.utils.HomeViewModelFactory
import com.example.autoreportgenerator.utils.LoginViewModelFactory
import com.example.autoreportgenerator.viewmodel.HomeViewModel
import com.example.autoreportgenerator.viewmodel.LoginViewModel

class HomeActivity : AppCompatActivity()  {
    private lateinit var viewModel: HomeViewModel
    private lateinit var token: String
    private lateinit var userId: String
    private  var isDoctor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val username: TextView = findViewById(R.id.tv_welcome)
        val success: TextView = findViewById(R.id.tv_success)
        val genButton: Button = findViewById(R.id.genButton)
        val downloadButton: Button = findViewById(R.id.downloadButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        val bundle = intent.extras
        if (bundle != null){
            username.text = "Welcome ${bundle.getString("name").toString()}"
            token = bundle.getString("token").toString()
            userId = bundle.getString("userId").toString()
            isDoctor = bundle.getBoolean("isDoctor")
        }

        val retrofitService = RetrofitService.getInstance()
        val homeRepository = HomeRepo(retrofitService)

        viewModel = ViewModelProvider(this,
            HomeViewModelFactory(homeRepository)
        )[HomeViewModel::class.java]

        if(isDoctor){
            genButton.visibility = View.VISIBLE
            downloadButton.visibility = View.GONE
        }else{
            genButton.visibility = View.GONE
            downloadButton.visibility = View.VISIBLE
        }

        genButton.setOnClickListener {
            val user = ScanRequest(
                userId =  userId,
                patientName =  "Sumit Kumar",
                height =  "165 cm",
                weight =  "69 kg",
                bmi =  "10.7",
                scannedBy =  "Dr. Viru",
                isFirstVisit =  true,
                scannedOn =  "13/02/2023",
                fhr =  "25",
                ga =  "26",
                mvp =  "27",
                placentaLocation =  "Right-Exterior",
                summary =  "This person seems to be alien. Be alert."
            )
            viewModel.postScan(user, token)
        }

        downloadButton.setOnClickListener {
            viewModel.download(token)
        }

        logoutButton.setOnClickListener {
            finish()
        }

        viewModel.homeState.observe(this) { state ->
            if (state) {
                success.visibility = View.VISIBLE
                success.text = "Data uploaded successfully"
            }
            else
                success.visibility = View.GONE

        }
    }
}