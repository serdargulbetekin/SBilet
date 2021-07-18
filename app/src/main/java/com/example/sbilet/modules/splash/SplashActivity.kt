package com.example.sbilet.modules.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sbilet.modules.main.MainActivity
import com.example.sbilet.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewModel.progressErrorPair.observe(this, { (shouldShowProgress, isError) ->
            if (shouldShowProgress) {
                viewBinding.progressBar.visibility = View.VISIBLE
            } else {
                if (isError) {
                    viewBinding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show()
                } else {
                    viewBinding.progressBar.visibility = View.GONE
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

    }
}