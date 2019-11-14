package com.gaurav.mystockmanager.activities

import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.classes.DetailItemAdapter
import com.gaurav.mystockmanager.classes.OrderDetailItem
import com.gaurav.mystockmanager.data.MyStocks
import com.gaurav.mystockmanager.helper.DATABASE_NAME
import com.gaurav.mystockmanager.helper.MONTHS
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetail : AppCompatActivity() {

    private val detailItems: MutableList<OrderDetailItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        val myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)
        val myStocks = MyStocks(myDatabase)

        val date = intent.getStringExtra("date")
        val source = intent.getStringExtra("source")
        val split = date.split(".")
        val myDate = split[0] + " " + MONTHS[split[1].toInt()-1] + ", " + split[2]
        Log.e("date",date)
        Log.e("source",source)
        setSupportActionBar(detail_toolbar as Toolbar)
        if(source == "purchase") {
            supportActionBar?.title = "Purchase of $myDate"
            myStocks.getDetailOfDate(date,detailItems,"purchase")
        }
        else {
            supportActionBar?.title = "Sell of $myDate"
            myStocks.getDetailOfDate(date,detailItems,"sell")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        detailItemList.addItemDecoration(
//            DividerItemDecoration(detailItemList.context,
//                DividerItemDecoration.VERTICAL)
//        )

        detailItemList.apply {
            layoutManager = LinearLayoutManager(this@OrderDetail)
            adapter = DetailItemAdapter(detailItems,this@OrderDetail)
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



