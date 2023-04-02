package com.hyparz.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hyparz.R
import com.hyparz.data.model.bean.Results
import com.hyparz.utils.CommonUtils
import kotlinx.android.synthetic.main.row_notification.view.*

/**
 * 
 * Description : This is CommentAdapter
 */
class NotificationAdapter(val context: Context, var items: ArrayList<Results>) :
    RecyclerView.Adapter<ViewHolder>() {

    private var customClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(notificationRow: Results, position: Int)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                 
        
        holder.tvNotification.text = items[position].name.title+" "+items[position].name.first+" "+items[position].name.last
        
        if (items[position].picture.large != null && !items[position].picture.large.equals("")) {
            CommonUtils.showProfile(
                context,
                items[position].picture.large ,
                holder.userIV
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val userIV = view.userIV
    val timeTV = view.timeTV
    val tvNotification = view.tv_notification
}