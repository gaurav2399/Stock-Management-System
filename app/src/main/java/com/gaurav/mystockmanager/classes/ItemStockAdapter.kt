package com.gaurav.mystockmanager.classes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gaurav.mystockmanager.R

class ItemStockAdapter(private val list: MutableList<ItemStock>, val context: Context)
    : RecyclerView.Adapter<DetailItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DetailItemViewHolder(inflater, parent,context)
    }

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        val item: ItemStock = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

}

class DetailItemViewHolder(inflater: LayoutInflater, parent: ViewGroup, context: Context) :

    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_stock, parent, false)) {

    private var name: TextView? = null
    private var qty: TextView? = null
    private var alertText:TextView? = null
    private var alertIcon:ImageView? = null

    init {
        name = itemView.findViewById(R.id.itemName)
        qty = itemView.findViewById(R.id.itemQty)
        alertText = itemView.findViewById(R.id.alertText)
        alertIcon = itemView.findViewById(R.id.alert_icon)
    }

    fun bind(item: ItemStock) {
        name?.text = item.itemName
        qty?.text = item.qty + " " + item.qtyType
        if(item.alert == 1) {
            alertIcon?.visibility = View.VISIBLE
            alertText?.visibility = View.VISIBLE
        }
    }

}