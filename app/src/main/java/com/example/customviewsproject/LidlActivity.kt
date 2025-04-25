package com.example.customviewsproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.customviewsproject.databinding.ActivityLidlBinding

class LidlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLidlBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLidlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.liveData.observe(this) {
            when (it) {
                is ApiResult.Loading ->{
                    binding.progressComponent.isVisible = false
                    binding.progressBar.isVisible = true
                }
                is ApiResult.Success -> {
                    binding.progressComponent.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.progressComponent.setProgressData(it.data)
                }
                is ApiResult.Error -> {
                    val errorMessage = it.exception.localizedMessage ?: "Unknown error!"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }
        if (viewModel.liveData.value == null) {
            viewModel.loadData()
        }
    }

}
