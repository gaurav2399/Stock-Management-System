package com.gaurav.mystockmanager.activities

import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.classes.PurchaseDateCostAdapter
import com.gaurav.mystockmanager.classes.SellDateCostAdapter
import com.gaurav.mystockmanager.classes.SellOrderItem
import com.gaurav.mystockmanager.data.MyStocks
import com.gaurav.mystockmanager.helper.DATABASE_NAME
import kotlinx.android.synthetic.main.activity_purchase_order.*
import kotlinx.android.synthetic.main.activity_sale_order.*

class SaleOrder : AppCompatActivity() {

    private val recyclerViewItems: MutableList<SellOrderItem> = ArrayList()
    lateinit var myDatabase:SQLiteDatabase
    lateinit var myStocks:MyStocks
    lateinit var myAdapter:SellDateCostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_order)

        setSupportActionBar(sale_order_toolbar as Toolbar)
        supportActionBar?.title = "Sale Order"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)
        myStocks = MyStocks(myDatabase)

        myStocks.getSellOrders(recyclerViewItems)

//        sellOrdersList.addItemDecoration(
//            DividerItemDecoration(sellOrdersList.context,
//                DividerItemDecoration.VERTICAL)
//        )

        myAdapter = SellDateCostAdapter(recyclerViewItems,this@SaleOrder)
        sellOrdersList.apply {
            layoutManager = LinearLayoutManager(this@SaleOrder)
            adapter = SellDateCostAdapter(recyclerViewItems,this@SaleOrder)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.sell_order_menu, menu)
        menu.findItem(R.id.dateText).isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.dateText -> {
                item.isChecked = true
                recyclerViewItems.clear()
                myStocks.getSellOrders(recyclerViewItems)
                sellOrdersList.apply {
                    layoutManager = LinearLayoutManager(this@SaleOrder)
                    adapter = SellDateCostAdapter(recyclerViewItems,this@SaleOrder)
                }
                true
            }
            R.id.graph -> {
                Toast.makeText(this,"Under Progress", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.rupees -> {
                item.isChecked = true
                recyclerViewItems.clear()
                myStocks.getSellOrders(recyclerViewItems)
                sellOrdersList.apply {
                    layoutManager = LinearLayoutManager(this@SaleOrder)
                    adapter = SellDateCostAdapter(recyclerViewItems,this@SaleOrder)
                }
                recyclerViewItems.sortWith(Comparator { o1, o2 -> o2.totalRs.compareTo(o1.totalRs) })
                myAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
