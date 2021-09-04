package com.celestial.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.celestial.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

val TAG = MainActivity::class.java.name

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root

        setContentView(view)

    }
}