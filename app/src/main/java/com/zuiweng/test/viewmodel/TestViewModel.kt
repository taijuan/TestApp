package com.zuiweng.test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zuiweng.test.utils.app
import com.zuiweng.test.utils.log
import io.zhuozhuo.remotetestlib.DataCenter
import io.zhuozhuo.remotetestlib.Message

class TestViewModel : ViewModel() {
    val refreshData = MutableLiveData<List<Message>>()

    init {
        DataCenter.init(app)
    }

    fun loadData() {
        DataCenter.register {
            it.forEach {
                if(it.msgType== Message.MessageTypeText){
                    it.content.log()
                }
            }
            refreshData.postValue(it)
        }
    }

    override fun onCleared() {

    }
}