package com.gaurav.mystockmanager.classes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.gaurav.mystockmanager.R

class DetailItemAdapter(private val list: MutableList<OrderDetailItem>, val context: Context)
    : RecyclerView.Adapter<StockItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StockItemViewHolder(inflater, parent,context)
    }

    override fun onBindViewHolder(holder: StockItemViewHolder, position: Int) {
        val item: OrderDetailItem = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

}

class StockItemViewHolder(inflater: LayoutInflater, parent: ViewGroup, context: Context) :

    RecyclerView.ViewHolder(inflater.inflate(R.layout.detail_item_cost, parent, false)) {

    private var name: TextView? = null
    private var totalCost: TextView? = null
    private var totalQty: TextView? = null

    init {
        name = itemView.findViewById(R.id.detailItemName)
        totalCost = itemView.findViewById(R.id.detailItemCost)
        totalQty = itemView.findViewById(R.id.detailItemQty)
    }

    fun bind(item: OrderDetailItem) {
        name?.text = item.itemName
        totalCost?.text = item.cost.toString() + " rs"
        totalQty?.text = item.qty.toString() + " " + item.qtyType
    }

}