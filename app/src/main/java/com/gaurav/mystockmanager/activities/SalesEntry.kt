package com.gaurav.mystockmanager.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.data.MyStocks
import com.gaurav.mystockmanager.helper.*
import kotlinx.android.synthetic.main.activity_purchase_entry.*
import kotlinx.android.synthetic.main.activity_sales_entry.*
import kotlinx.android.synthetic.main.activity_sales_entry.calendar
import kotlinx.android.synthetic.main.activity_sales_entry.itemName
import java.text.SimpleDateFormat
import java.util.*

class SalesEntry : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_entry)

        val myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)
        val myStocks = MyStocks(myDatabase)
        var qty_mn_type = "solid weight"
        var qty_type = "gram"

        setSupportActionBar(sales_entry_toolbar as Toolbar)
        supportActionBar?.title = "Sales Entry"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val allItemName:MutableList<String>? = ArrayList()
        var qty_type_adapter: ArrayAdapter<String>?
        var dbDate:String?
        qty_type_adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_solid)
        sell_qty_spinner.adapter = qty_type_adapter

        val cal = Calendar.getInstance()
        sellDate.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        dbDate = sellDate.text.toString()
        val newDate = dbDate?.split(".")
        sellDate.text = newDate!![0] + " " + MONTHS[(newDate!![1].toInt())-1] + ", " + newDate!![2]

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            sellDate.text = sdf.format(cal.time)

            dbDate = sellDate.text.toString()
            val newDate = dbDate?.split(".")
            sellDate.text = newDate!![0] + " " + MONTHS[(newDate!![1].toInt())-1] + ", " + newDate!![2]
        }
        calendar.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        val autoTagAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allItemName)
        itemName.threshold = 1
        itemName.setAdapter(autoTagAdapter)
        myStocks.getItemComponent(allItemName,3)
        autoTagAdapter.notifyDataSetChanged()

        itemName.setOnItemClickListener { _, _, position, _ ->
            Log.e("select item",autoTagAdapter.getItem(position)?.toString())
            qty_mn_type = myStocks.getQuantityType(itemName.text.toString())
            Log.e("qty type",qty_mn_type)
            if(qty_mn_type == "")
                Toast.makeText(this,"Enter a valid item name or make a new item first", Toast.LENGTH_LONG).show()
            else {
                when (qty_mn_type) {
                    "piece" -> {
                        qty_type_adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_piece)
                        sell_qty_spinner.adapter = qty_type_adapter
                    }
                    "solid weight" -> {
                        qty_type_adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_solid)
                        sell_qty_spinner.adapter = qty_type_adapter
                    }
                    "liquid weight" -> {
                        qty_type_adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_liquid)
                        sell_qty_spinner.adapter = qty_type_adapter
                    }
                }

                sell_qty_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        qty_type = p0?.getItemAtPosition(p2).toString()
                    }
                }
            }
        }

        // saving entry
        sellEntrySave.setOnClickListener {
            if(itemName.text.toString() == "" ||
                sellQty.text.toString() == "" || sellPrice.text.toString() == "") {
                Toast.makeText(this, "Every field must be filled", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val res = myStocks.addSalesEntry(dbDate!!,itemName.text.toString(),
                sellQty.text.toString().toFloat(),sellPrice.text.toString().toFloat(),qty_type,qty_mn_type)
            if(res == FUNCTION_WORK_DONE || res == ALERT_NEEDED) {
                setOriginal()
                Toast.makeText(this, "Entry updated", Toast.LENGTH_LONG).show()
            }else if(res == NOT_AVAILABLE_MUCH_AMOUNT)
                Toast.makeText(this,"You have not this much amount of this item",Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this,"Some error has occurred",Toast.LENGTH_LONG).show()

            if (res == ALERT_NEEDED)
                Toast.makeText(this,"quantity goes down to alert quantity",Toast.LENGTH_LONG).show()
        }
    }

    private fun setOriginal() {
        itemName.setText("")
        sellQty.setText("")
        sellPrice.setText("")
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
