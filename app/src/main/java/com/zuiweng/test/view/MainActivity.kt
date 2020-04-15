package com.zuiweng.test.view

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuiweng.test.R
import com.zuiweng.test.adapter.TestAdapter
import com.zuiweng.test.viewmodel.TestViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = TestAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val testViewModel = defaultViewModelProviderFactory.create(TestViewModel::class.java)
        testViewModel.refreshData.observe(this, Observer {
            adapter.addMessages(it)
            recyclerView.post {
                recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        })
        testViewModel.loadData()
    }
}



