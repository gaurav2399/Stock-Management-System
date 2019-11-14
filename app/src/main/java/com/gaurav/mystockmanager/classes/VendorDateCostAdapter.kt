package com.gaurav.mystockmanager.classes

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.helper.MONTHS

class VendorDateCostAdapter(private val list: MutableList<VendorOrderItem>, val context: Context)
    : RecyclerView.Adapter<VendorItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VendorItemViewHolder(inflater, parent,context)
    }

    override fun onBindViewHolder(holder: VendorItemViewHolder, position: Int) {
        val item: VendorOrderItem = list[position]
        holder.bind(item,context)
    }

    override fun getItemCount(): Int = list.size

}

class VendorItemViewHolder(inflater: LayoutInflater, parent: ViewGroup, context: Context) :

    RecyclerView.ViewHolder(inflater.inflate(R.layout.vendor_date, parent, false)) {

    private var date: TextView? = null
    private var vendor: TextView? = null
    private var cost: TextView? = null
    lateinit var myDate:String

    init {
        date = itemView.findViewById(R.id.date)
        cost = itemView.findViewById(R.id.cost)
        vendor = itemView.findViewById(R.id.vendor)
//        itemView.setOnClickListener{
//            Log.e("item clicked","yes")
//            val intent = Intent(context, OrderDetail::class.java)
//            Log.e("my date",myDate)
//            intent.putExtra("date",myDate)
//            intent.putExtra("source","purchase")
//            context.startActivity(intent)
//        }
    }

    fun bind(item: VendorOrderItem,context: Context) {
        myDate = item.date
        val split = item.date.split(".")
        val myDate = split[0] + " " + MONTHS[split[1].toInt()-1] + ", " + split[2]
        date?.text = myDate
        vendor?.text = item.vendor
        cost?.text = item.cost + " rs"
    }

}