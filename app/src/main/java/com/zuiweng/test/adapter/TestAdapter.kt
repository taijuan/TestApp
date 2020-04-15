package com.zuiweng.test.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zuiweng.test.R
import com.zuiweng.test.utils.loadImage
import io.zhuozhuo.remotetestlib.Message
import kotlinx.android.synthetic.main.holder_test.view.*

class TestAdapter : RecyclerView.Adapter<TestViewHolder>() {

    private val data = arrayListOf<Message>()
    fun addMessages(msgs: List<Message>) {
        val start = data.size
        data.addAll(msgs)
        notifyItemRangeInserted(start, msgs.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TestViewHolder(parent)

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.refreshData(data[position])
    }
}

class TestViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.holder_test, parent, false)
) {
    private lateinit var data: Message

    init {
        itemView.layoutParams = ViewGroup.LayoutParams(-1, -2)
    }

    fun refreshData(msg: Message) {
        this.data = msg
        itemView.textAndImageView.setType(msg.msgType)
        if (msg.msgType == Message.MessageTypeText) {
            itemView.textAndImageView.setText(msg.content)
        } else {
            msg.loadImage { bm, image ->
                if (this.data.content == image) {
                    itemView.textAndImageView.setBitmap(bm)
                }
            }
        }
    }
}