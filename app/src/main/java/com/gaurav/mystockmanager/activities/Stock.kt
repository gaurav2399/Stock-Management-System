package com.gaurav.mystockmanager.activities

import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.classes.ItemStock
import com.gaurav.mystockmanager.classes.ItemStockAdapter
import com.gaurav.mystockmanager.classes.PurchaseDateCostAdapter
import com.gaurav.mystockmanager.classes.PurchaseOrderItem
import com.gaurav.mystockmanager.data.MyStocks
import com.gaurav.mystockmanager.helper.DATABASE_NAME
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_purchase_order.*
import kotlinx.android.synthetic.main.activity_stock.*

class Stock : AppCompatActivity() {

    private val recyclerViewItems: MutableList<ItemStock> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        setSupportActionBar(stock_toolbar as Toolbar)
        supportActionBar?.title = "Stock"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)
        val myStocks = MyStocks(myDatabase)

        myStocks.getItemStock(recyclerViewItems)

//        itemStockList.addItemDecoration(
//            DividerItemDecoration(itemStockList.context,
//                DividerItemDecoration.VERTICAL)
//        )

        itemStockList.apply {
            layoutManager = LinearLayoutManager(this@Stock)
            adapter = ItemStockAdapter(recyclerViewItems,this@Stock)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
