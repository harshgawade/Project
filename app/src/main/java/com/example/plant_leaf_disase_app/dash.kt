package com.example.plant_leaf_disase_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class dash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)

        val bottom = findViewById<BottomNavigationView>(R.id.bottom)
        bottom.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.home -> {
                    val i = Intent(applicationContext, home::class.java)
                    startActivity(i)
                    true }

                R.id.shop ->
                {
                    val i = Intent(applicationContext, Displayproduct::class.java)
                    startActivity(i)
                    true
                }
                else -> {false}
            }
        }

    }
}