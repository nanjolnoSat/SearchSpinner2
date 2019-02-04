package com.mishaki.searchspinner2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mishaki.searchspinner2.adapter.StringMarkSpinnerAdapter
import com.mishaki.searchspinner2.view.SearchSpinner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivityMsg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = ArrayList<String>()
        (0..10).mapTo(list){it.toString()}
        (0..10).mapTo(list){it.toString()}
        (0..10).mapTo(list){it.toString()}
        (0..10).mapTo(list){it.toString()}
        val adapter = StringMarkSpinnerAdapter()
        sss.setAdapter(adapter,list)
        sss.setOnSelectedListener(object :SearchSpinner.OnSelectedListener<String>{
            override fun onSelected(spinner: SearchSpinner<String>, position: Int) {
                position_btn.text = position.toString()
            }
        })
    }
}