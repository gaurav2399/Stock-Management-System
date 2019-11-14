package com.gaurav.mystockmanager.activities

import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.gaurav.mystockmanager.classes.PurchaseOrderItem
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.classes.PurchaseDateCostAdapter
import com.gaurav.mystockmanager.classes.VendorDateCostAdapter
import com.gaurav.mystockmanager.classes.VendorOrderItem
import com.gaurav.mystockmanager.data.MyStocks
import com.gaurav.mystockmanager.helper.DATABASE_NAME
import kotlinx.android.synthetic.main.activity_purchase_order.*
import android.R.attr.data
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import android.R.attr.name






class PurchaseOrder : AppCompatActivity() {

    private val recyclerViewItems: MutableList<PurchaseOrderItem> = ArrayList()
    private val recyclerViewItems2: MutableList<VendorOrderItem> = ArrayList()
    lateinit var myDatabase:SQLiteDatabase
    lateinit var myStocks:MyStocks
    lateinit var myAdapter:PurchaseDateCostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_order)

        myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)
        myStocks = MyStocks(myDatabase)

        setSupportActionBar(purchase_order_toolbar as Toolbar)
        supportActionBar?.title = "Purchase Order"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        myStocks.getPurchaseOrders(recyclerViewItems)

//        purchaseOrdersList.addItemDecoration(DividerItemDecoration(purchaseOrdersList.context,DividerItemDecoration.VERTICAL))

        myAdapter = PurchaseDateCostAdapter(recyclerViewItems,this@PurchaseOrder)
        purchaseOrdersList.apply {
            layoutManager = LinearLayoutManager(this@PurchaseOrder)
            adapter = myAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.purchase_order_menu, menu)
        menu.findItem(R.id.dateText).isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.graph -> {
                Toast.makeText(this,"Under Progress", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.vendorDate -> {
                item.isChecked = true
                recyclerViewItems2.clear()
                myStocks.getPurchaseOrderVendor(recyclerViewItems2)
                Log.e("going size",recyclerViewItems2.size.toString())
                purchaseOrdersList.apply {
                    layoutManager = LinearLayoutManager(this@PurchaseOrder)
                    adapter = VendorDateCostAdapter(recyclerViewItems2,this@PurchaseOrder)
                }
                true
            }
            R.id.dateText -> {
                item.isChecked = true
                recyclerViewItems.clear()
                myStocks.getPurchaseOrders(recyclerViewItems)
                purchaseOrdersList.apply {
                    layoutManager = LinearLayoutManager(this@PurchaseOrder)
                    adapter = PurchaseDateCostAdapter(recyclerViewItems,this@PurchaseOrder)
                }
                true
            }
            R.id.rupees -> {
                item.isChecked = true
                recyclerViewItems.clear()
                myStocks.getPurchaseOrders(recyclerViewItems)
                purchaseOrdersList.apply {
                    layoutManager = LinearLayoutManager(this@PurchaseOrder)
                    adapter = PurchaseDateCostAdapter(recyclerViewItems,this@PurchaseOrder)
                }
                recyclerViewItems.sortWith(Comparator { o1, o2 -> o2.totalRs.compareTo(o1.totalRs) })
                myAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
