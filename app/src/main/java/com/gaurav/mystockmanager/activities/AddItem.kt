package com.gaurav.mystockmanager.activities

import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import com.gaurav.mystockmanager.R
import kotlinx.android.synthetic.main.activity_add_item.*
import android.view.*
import android.widget.*
import com.gaurav.mystockmanager.data.MyStocks
import com.gaurav.mystockmanager.helper.DATABASE_NAME
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.gaurav.mystockmanager.helper.qty_type_list_liquid
import com.gaurav.mystockmanager.helper.qty_type_list_piece
import com.gaurav.mystockmanager.helper.qty_type_list_solid


class AddItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val context = this
        val myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)
        val myStocks = MyStocks(myDatabase)

        setSupportActionBar(add_item_toolbar as Toolbar)
        supportActionBar?.title = "Add Item"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val allItemTag:MutableList<String>? = ArrayList()
        val allSubItem:MutableList<String>? = ArrayList()
        val qty_mn_type_list = arrayOf("piece", "solid weight", "liquid weight")
        var cost_per_type:String? = null
        var qty_type:String? = null
        var alert_qty_type:String? = null
        var qty_mn_type = "piece"

        var qty_type_adapter: ArrayAdapter<String>?
        var alertQty: Float

        // setting item tag autocomplete
        var autoTagAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allItemTag)
        itemTag.threshold = 1
        itemTag.setAdapter(autoTagAdapter)
        myStocks.getItemComponent(allItemTag, 1)
        autoTagAdapter.notifyDataSetChanged()

        // setting sub item auocomplete
        var autoSubItemAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allSubItem)
        subItem.threshold = 1
        subItem.setAdapter(autoSubItemAdapter)
        myStocks.getItemComponent(allSubItem, 2)
        autoSubItemAdapter.notifyDataSetChanged()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qty_mn_type_list)
        qty_type_spinner?.adapter = arrayAdapter
        qty_type_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                qty_mn_type = p0?.getItemAtPosition(p2).toString()
                when (qty_mn_type) {
                    "piece" -> {
                        qty_type_adapter =
                            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, qty_type_list_piece)
                        qty_type_spec_spinner?.adapter = qty_type_adapter
                        spinner3?.adapter = qty_type_adapter
                        alert_qty_type_spinner?.adapter = qty_type_adapter
                    }
                    "solid weight" -> {
                        qty_type_adapter =
                            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, qty_type_list_solid)
                        qty_type_spec_spinner?.adapter = qty_type_adapter
                        spinner3?.adapter = qty_type_adapter
                        alert_qty_type_spinner?.adapter = qty_type_adapter
                    }
                    "liquid weight" -> {
                        qty_type_adapter =
                            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, qty_type_list_liquid)
                        qty_type_spec_spinner?.adapter = qty_type_adapter
                        spinner3?.adapter = qty_type_adapter
                        alert_qty_type_spinner?.adapter = qty_type_adapter
                    }
                }
            }
        }

        qty_type_spec_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                qty_type = p0?.getItemAtPosition(p2).toString()
            }
        }
        spinner3?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cost_per_type = p0?.getItemAtPosition(p2).toString()
            }
        }
        alert_qty_type_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                alert_qty_type = p0?.getItemAtPosition(p2).toString()
            }
        }


        // item saving
        save.setOnClickListener {
            if(itemTag.text.toString() == "" || detailItemQty.text.toString() == "" || detailItemCost.text.toString() == ""){
                Toast.makeText(context,"Item Tag, quantity and cost must be filled.",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // hide keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.applicationWindowToken, 0)

            var itemName = itemTag.text.toString()
            if(subItem.text.toString() != "")
                itemName = itemName.plus("_").plus(subItem.text.toString())
            if(itemSpecification.text.toString() != "")
                itemName = itemName.plus("_").plus(itemSpecification.text.toString())

            Log.e("item name",itemName)
            alertQty = if(alert_qty.text.toString() != "")
                alert_qty.text.toString().toFloat()
            else
                0f

            val status = myStocks.addItem(itemName,qty_mn_type,cost_per_type!!,qty_type!!,
                detailItemQty.text.toString().toFloat(),detailItemCost.text.toString().toFloat(),alertQty,alert_qty_type!!)

            when (status) {
                0 -> Toast.makeText(context,"Some error occurred",Toast.LENGTH_LONG).show()
                1 -> {
                    if(!allItemTag?.contains(itemTag.text.toString())!!) {
                        Log.e("not contains","yes")
                        Log.e("adding item", itemTag.text.toString())
                        allItemTag.add(itemTag.text.toString())
                        Log.e("new size",allItemTag.size.toString())
                        autoTagAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allItemTag)
                        itemTag.threshold = 1
                        itemTag.setAdapter(autoTagAdapter)
                    }
                    if(!allSubItem?.contains(subItem.text.toString())!!) {
                        Log.e("not contains","yes")
                        allSubItem.add(subItem.text.toString())
                        autoSubItemAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, allSubItem)
                        subItem.threshold = 1
                        subItem.setAdapter(autoSubItemAdapter)
                    }
                    setOriginal()
                    Toast.makeText(context,"Item inserted",Toast.LENGTH_LONG).show()
                }
                else -> Toast.makeText(context,"This item name already exists",Toast.LENGTH_LONG).show()

            }
        }

    }

    private fun setOriginal() {
        itemSpecification.setText("")
        detailItemQty.setText("")
        detailItemCost.setText("")
        alert_qty.setText("")
        qty_type_spec_spinner.setSelection(0)
        alert_qty_type_spinner.setSelection(0)
        spinner3.setSelection(0)
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

