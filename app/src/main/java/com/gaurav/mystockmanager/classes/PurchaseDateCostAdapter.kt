package com.gaurav.mystockmanager.classes

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.activities.OrderDetail
import com.gaurav.mystockmanager.helper.MONTHS


class PurchaseDateCostAdapter(private val list: MutableList<PurchaseOrderItem>, val context: Context)
    : RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater, parent,context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: PurchaseOrderItem = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

}

class ItemViewHolder(inflater: LayoutInflater, parent: ViewGroup,context: Context) :

    RecyclerView.ViewHolder(inflater.inflate(R.layout.date_cost_layout, parent, false)) {

    private var date: TextView? = null
    private var totalCost: TextView? = null
    lateinit var myDate:String

    init {
        date = itemView.findViewById(R.id.listItemDate)
        totalCost = itemView.findViewById(R.id.listItemCost)
        itemView.setOnClickListener{
            Log.e("item clicked","yes")
            val intent = Intent(context,OrderDetail::class.java)
            Log.e("my date",myDate)
            intent.putExtra("date",myDate)
            intent.putExtra("source","purchase")
            context.startActivity(intent)
        }
    }

    fun bind(item: PurchaseOrderItem) {
        myDate = item.date
        val split = item.date.split(".")
        val myDate = split[0] + " " + MONTHS[split[1].toInt()-1] + ", " + split[2]
        date?.text = myDate
        totalCost?.text = item.totalRs.toString() + " rs"
    }

}