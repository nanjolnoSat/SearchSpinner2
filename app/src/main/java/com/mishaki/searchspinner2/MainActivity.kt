package com.mishaki.searchspinner2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mishaki.libsearchspinner.adapter.StringMarkSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = ArrayList<String>()
        (0..10).mapTo(list) { it.toString() }
        (0..10).mapTo(list) { it.toString() }
        (0..10).mapTo(list) { it.toString() }
        (0..10).mapTo(list) { it.toString() }
        val adapter = StringMarkSpinnerAdapter()
        sss.setAdapter(adapter, list)
        sss.setOnSelectedListener { position -> position_btn.text = position.toString() }
    }
}