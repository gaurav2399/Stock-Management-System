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
import java.text.SimpleDateFormat
import java.util.*


class PurchaseEntry : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_entry)

        val myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)
        val myStocks = MyStocks(myDatabase)

        setSupportActionBar(purchse_entry_toolbar as Toolbar)
        supportActionBar?.title = "Purchase Entry"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var qty_type_adapter: ArrayAdapter<String>?
        val allItemName:MutableList<String>? = ArrayList()
        val allVendor:MutableList<String> = ArrayList()
        var qty_type = "gram"
        var dbDate:String?
        var qty_mn_type = "solid weight"
        qty_type_adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_solid)
        purchase_qty_spinner.adapter = qty_type_adapter

        // setting all item autocomplete
        val autoTagAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allItemName)
        itemName.threshold = 1
        itemName.setAdapter(autoTagAdapter)
        myStocks.getItemComponent(allItemName,3)
        autoTagAdapter.notifyDataSetChanged()

        // setting vendor autocomplete
        var allVendorAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allVendor)
        vendorName.threshold = 1
        vendorName.setAdapter(allVendorAdapter)
        myStocks.getAllVendor(allVendor)
        allVendorAdapter.notifyDataSetChanged()

        // managing qty type
        itemName.setOnItemClickListener { _, _, position, _ ->
            Log.e("select item",autoTagAdapter.getItem(position)?.toString())
            qty_mn_type = myStocks.getQuantityType(itemName.text.toString())
            Log.e("qty type",qty_mn_type)
            if(qty_mn_type == "")
                Toast.makeText(this,"Enter a valid item name or make a new item first",Toast.LENGTH_LONG).show()
            else {
                when (qty_mn_type) {
                    "piece" -> {
                        qty_type_adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_piece)
                        purchase_qty_spinner.adapter = qty_type_adapter
                    }
                    "solid weight" -> {
                        qty_type_adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_solid)
                        purchase_qty_spinner.adapter = qty_type_adapter
                    }
                    "liquid weight" -> {
                        qty_type_adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_type_list_liquid)
                        purchase_qty_spinner.adapter = qty_type_adapter
                    }
                }

                purchase_qty_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        qty_type = p0?.getItemAtPosition(p2).toString()
                    }
                }
            }
        }

        // managing date
        val cal = Calendar.getInstance()
        purchaseDate.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        dbDate = purchaseDate.text.toString()
        val newDate = dbDate?.split(".")
        purchaseDate.text = newDate!![0] + " " + MONTHS[(newDate!![1].toInt())-1] + ", " + newDate!![2]

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            purchaseDate.text = sdf.format(cal.time)

            dbDate = purchaseDate.text.toString()
            val newDate = dbDate?.split(".")
            purchaseDate.text = newDate!![0] + " " + MONTHS[(newDate!![1].toInt())-1] + ", " + newDate!![2]
        }
        calendar.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // saving entry
        purchaseEntrySave.setOnClickListener {
            if(itemName.text.toString() == "" || vendorName.text.toString() == "" ||
                    purchaseQty.text.toString() == "" || purchasePrice.text.toString() == "") {
                Toast.makeText(this, "Every field must be filled", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!(allVendor.contains(vendorName.text.toString()))) {
                Log.e("not contains","yes")
                allVendor.add(vendorName.text.toString())
                allVendorAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allVendor)
                vendorName.threshold = 1
                vendorName.setAdapter(allVendorAdapter)
            }

            Log.e("db date",dbDate)
            myStocks.addPurchaseEntry(dbDate!!,itemName.text.toString(),vendorName.text.toString(),
                purchaseQty.text.toString().toFloat(),purchasePrice.text.toString().toFloat(),qty_type,qty_mn_type)
            setOriginal()
            Toast.makeText(this,"Entry updated",Toast.LENGTH_LONG).show()
        }

    }

    private fun setOriginal() {
        itemName.setText("")
        vendorName.setText("")
        purchaseQty.setText("")
        purchasePrice.setText("")
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
