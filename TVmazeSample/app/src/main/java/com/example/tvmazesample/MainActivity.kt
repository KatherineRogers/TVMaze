package com.example.tvmazesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fm = supportFragmentManager
        var onnowfrag = fm.findFragmentByTag("onnowfrag")
        if(onnowfrag == null){
            onnowfrag = OnNowFragment()
            fm.beginTransaction().add(R.id.frame, onnowfrag, "onnowfrag").commit()
        }
    }


}