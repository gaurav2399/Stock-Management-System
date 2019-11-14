package com.gaurav.mystockmanager.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.gaurav.mystockmanager.classes.*
import com.gaurav.mystockmanager.helper.*

class MyStocks(private val db:SQLiteDatabase) {

    fun addSalesEntry(date:String, itemName: String, qty:Float, cost: Float,
                      qty_type: String, qty_mn_type: String): Int{
        val values = ContentValues()
        values.apply {
            put(KEY_ITEM_NAME, itemName)
            put(KEY_QTY_MN_TYPE, qty_mn_type)
            put(KEY_DATE, date)
            put(KEY_QTY_TYPE, qty_type)
            put(KEY_QTY, qty)
            put(KEY_COST, cost)
        }
        return try {
            db.insert(TABLE_SALES_ENTRY,null, values)
            Log.e("sales entry inserted","yes")
            return updateItem(itemName,qty,qty_type,2)
        }catch (e:Exception){
            Log.e("error occurred",e.message)
            FUNCTION_WORK_NOT_DONE
        }
    }

    fun addPurchaseEntry(date:String, itemName: String, vendor_name:String, qty:Float, cost: Float,
                         qty_type: String, qty_mn_type: String): Int{
        Log.e("i purchase entry","yes")
        val values = ContentValues()
        values.apply {
            put(KEY_ITEM_NAME, itemName)
            put(KEY_QTY_MN_TYPE, qty_mn_type)
            put(KEY_DATE, date)
            put(KEY_QTY_TYPE, qty_type)
            put(KEY_QTY, qty)
            put(KEY_COST, cost)
            put(KEY_VENDOR_NAME, vendor_name)
        }
        return try {
            db.insert(TABLE_PURCHASE_ENTRY,null, values)
            updateItem(itemName,qty,qty_type,1)
            Log.e("purchase entry inserted","yes")
            FUNCTION_WORK_DONE
        }catch (e:Exception){
            Log.e("error occurred",e.message)
            FUNCTION_WORK_NOT_DONE
        }
    }

    fun addItem(itemName:String, qty_mn_type:String, per_cost_type:String, qty_type:String,
                qty:Float, cost:Float, alert_qty:Float, alert_qty_type:String): Int{
        val values = ContentValues()
        values.apply {
            put(KEY_ITEM_NAME, itemName)
            put(KEY_QTY_MN_TYPE, qty_mn_type)
            put(KEY_QTY_TYPE, qty_type)
            put(KEY_QTY, qty)
            put(KEY_PER_COST_TYPE, per_cost_type)
            put(KEY_COST, cost)
            put(KEY_ALERT_QTY, alert_qty)
            put(KEY_ALERT_QTY_TYPE, alert_qty_type)
        }
        return if(!checkNameExists(itemName,db)){
            try {
                db.insert(TABLE_ITEM_LIST,null, values)
                Log.e("item inserted","yes")
                FUNCTION_WORK_DONE
            }catch (e:Exception){
                Log.e("error item insertion",e.message)
                FUNCTION_WORK_NOT_DONE
            }
        }else
            ITEM_EXISTS_ALREADY
    }

    fun deleteItem(itemName: String){
        if(checkNameExists(itemName,db)) {
            db.delete(TABLE_ITEM_LIST, "$KEY_ITEM_NAME = '$itemName'", null)
            Log.e("item deletion", "done")
        }
    }

    private fun updateItem(itemName: String, qty: Float, qty_type: String,mode: Int):Int{
        Log.e("reach in update","yes")
        val query = "SELECT * FROM $TABLE_ITEM_LIST WHERE $KEY_ITEM_NAME = '$itemName'"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            Log.e("have items","yes")
            while(cursor.moveToNext()){
                Log.e("reach in while","yes")
                var itemQty = cursor.getString(cursor.getColumnIndex(KEY_QTY)).toFloat()
                val itemQtyType = cursor.getString(cursor.getColumnIndex(KEY_QTY_TYPE))
                val alertQty = cursor.getString(cursor.getColumnIndex(KEY_ALERT_QTY)).toFloat()
                val alertQtyType = cursor.getString(cursor.getColumnIndex(KEY_ALERT_QTY_TYPE))
                var newValue = convertValue(qty, qty_type, itemQtyType)
                if(mode == 1) {
                    newValue += itemQty
                    val strSQL = "UPDATE $TABLE_ITEM_LIST SET $KEY_QTY = $newValue WHERE $KEY_ITEM_NAME = '$itemName'"
                    db.execSQL(strSQL)
                    return FUNCTION_WORK_DONE
                }else{
                    if(itemQty<newValue)
                        return NOT_AVAILABLE_MUCH_AMOUNT
                    else {
                        itemQty -= newValue
                        val convertQty = convertValue(itemQty,itemQtyType,alertQtyType)
                        val strSQL = "UPDATE $TABLE_ITEM_LIST SET $KEY_QTY = $itemQty WHERE $KEY_ITEM_NAME = '$itemName'"
                        db.execSQL(strSQL)
                        if(convertQty<alertQty)
                            return ALERT_NEEDED
                        return FUNCTION_WORK_DONE
                    }
                }
            }
            cursor.close()
        }
        return FUNCTION_WORK_NOT_DONE
    }

    fun getItemComponent(result:MutableList<String>?,mode: Int){
        val query = "SELECT $KEY_ITEM_NAME FROM $TABLE_ITEM_LIST"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            while(cursor.moveToNext()){
                val itemName = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME))
                if(mode == 3) {
                    if(!(result?.contains(itemName)!!))
                        result.add(itemName)
                }
                val str = itemName.split("_")
                if(mode == 1) {
                    if(!(result?.contains(str[0])!!))
                        result.add(str[0])
                }
                if(str.size >= 2 && mode == 2) {
                    if (!(result?.contains(str[1])!!))
                        result.add(str[1])
                }
            }
            cursor.close()
        }
    }

    fun getQuantityType(id:String):String{
        val query = "SELECT $KEY_QTY_MN_TYPE FROM $TABLE_ITEM_LIST WHERE $KEY_ITEM_NAME = '$id'"
        val cursor = db.rawQuery(query, null)
        return if(cursor.count<=0) {
            cursor.close()
            Log.e("message","no record exists")
            ""
        }else{
            Log.e("message","record exists")
            cursor.moveToFirst()
            cursor.getString(cursor.getColumnIndex(KEY_QTY_MN_TYPE))
        }
    }

    fun getAllVendor(result:MutableList<String>){
        val query = "SELECT $KEY_VENDOR_NAME FROM $TABLE_PURCHASE_ENTRY"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            while(cursor.moveToNext()){
                val itemName = cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME))
                if(!(result.contains(itemName))) {
                    Log.e("not contains","yes")
                    result.add(itemName)
                }
            }
            cursor.close()
        }
    }

    fun getItemStock(recylerViewItems: MutableList<ItemStock>){
        val query = "SELECT * FROM $TABLE_ITEM_LIST ORDER BY $KEY_ITEM_NAME ASC"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            while(cursor.moveToNext()){
                val itemName = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME))
                val itemQty = cursor.getFloat(cursor.getColumnIndex(KEY_QTY))
                val alertQty = cursor.getFloat(cursor.getColumnIndex(KEY_ALERT_QTY))
                val alertQtyType = cursor.getString(cursor.getColumnIndex(KEY_ALERT_QTY_TYPE))
                val itemQtyType = cursor.getString(cursor.getColumnIndex(KEY_QTY_TYPE))
                val alert = convertValue(alertQty,alertQtyType,itemQtyType)
                var addValue = 0f
                var redA1ert:Int
                if(itemQty<alert){
                    redA1ert = 1
                    addValue = alert - itemQty
                } else
                    redA1ert = 0
                val item = ItemStock(itemName,itemQty.toString(), itemQtyType, redA1ert, addValue)
                recylerViewItems.add(item)
            }
            cursor.close()
        }
    }

    fun getSellOrders(recylerViewItems: MutableList<SellOrderItem>){
        val query = "SELECT * FROM $TABLE_SALES_ENTRY ORDER BY $KEY_DATE DESC"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            var prev = ""
            var totalCost = 0f
            var first = true
            while(cursor.moveToNext()) {
                val itemDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                Log.e("item date", itemDate)
                if(itemDate != prev && !first) {
                    Log.e("prev date",prev)
                    Log.e("total cost",totalCost.toString())
                    val item = SellOrderItem(prev, totalCost)
                    recylerViewItems.add(item)
                    prev = itemDate
                    totalCost = 0f
                    totalCost += cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                }
                else{
                    if(first) {
                        first = false
                        prev = itemDate
                    }
                    totalCost += cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                }
            }
            Log.e("prev date",prev)
            Log.e("total cost",totalCost.toString())
            val item = SellOrderItem(prev, totalCost)
            recylerViewItems.add(item)

            cursor.close()
        }
    }

    fun getPurchaseOrders(recylerViewItems: MutableList<PurchaseOrderItem>){
        val query = "SELECT * FROM $TABLE_PURCHASE_ENTRY ORDER BY $KEY_DATE DESC"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            var vendorWiseAmount: HashMap<String, Float> = HashMap()
            var prev = ""
            var totalCost = 0f
            var first = true
            while(cursor.moveToNext()) {
                val itemDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                //Log.e("item date", itemDate)
                if(itemDate != prev && !first) {
                    Log.e("prev date",prev)
                    Log.e("total cost",totalCost.toString())
                    for((key,value) in vendorWiseAmount){
                        Log.e("vendor",key)
                        Log.e("cost",value.toString())
                    }
                    val item = PurchaseOrderItem(prev, totalCost, vendorWiseAmount)
                    recylerViewItems.add(item)
                    prev = itemDate
                    totalCost = 0f
                    totalCost += cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    val vendor = cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME))
                    val purchaseCost = cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    var it:Float?
                    vendorWiseAmount = HashMap()
                    it = if(vendorWiseAmount[vendor] == null)
                        0f
                    else
                        vendorWiseAmount[vendor]
                    vendorWiseAmount[vendor] = it!! + purchaseCost
                }
                else{
                    if(first) {
                        first = false
                        prev = itemDate
                    }
                    totalCost += cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    val vendor = cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME))
                    val purchaseCost = cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    var it:Float?
                    it = if(vendorWiseAmount[vendor] == null)
                        0f
                    else
                        vendorWiseAmount[vendor]
                    vendorWiseAmount[vendor] = it!! + purchaseCost
                }
            }
            Log.e("prev date",prev)
            Log.e("total cost",totalCost.toString())
            for((key,value) in vendorWiseAmount){
                Log.e("vendor",key)
                Log.e("cost",value.toString())
            }
            val item = PurchaseOrderItem(prev, totalCost, vendorWiseAmount)
            recylerViewItems.add(item)

            cursor.close()
        }
    }

    fun getPurchaseOrderVendor(recylerViewItems: MutableList<VendorOrderItem>){
        val query = "SELECT * FROM $TABLE_PURCHASE_ENTRY ORDER BY $KEY_DATE DESC"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            var vendorWiseAmount: HashMap<String, Float> = HashMap()
            var prev = ""
            var first = true
            while(cursor.moveToNext()) {
                val itemDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                //Log.e("item date", itemDate)
                if(itemDate != prev && !first) {
                    Log.e("prev date",prev)
                    for((key,value) in vendorWiseAmount){
                        Log.e("vendor",key)
                        Log.e("cost",value.toString())

                        val item = VendorOrderItem(prev, key, value.toString())
                        recylerViewItems.add(item)
                    }

                    prev = itemDate
                    val vendor = cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME))
                    val purchaseCost = cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    var it:Float?
                    vendorWiseAmount = HashMap()
                    it = if(vendorWiseAmount[vendor] == null)
                        0f
                    else
                        vendorWiseAmount[vendor]
                    vendorWiseAmount[vendor] = it!! + purchaseCost
                }
                else{
                    if(first) {
                        first = false
                        prev = itemDate
                    }
                    val vendor = cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME))
                    val purchaseCost = cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    var it:Float?
                    it = if(vendorWiseAmount[vendor] == null)
                        0f
                    else
                        vendorWiseAmount[vendor]
                    vendorWiseAmount[vendor] = it!! + purchaseCost
                }
            }
            Log.e("prev date",prev)
            for((key,value) in vendorWiseAmount){
                Log.e("vendor",key)
                Log.e("cost",value.toString())

                val item = VendorOrderItem(prev, key, value.toString())
                recylerViewItems.add(item)
            }

            cursor.close()
        }
    }

    fun getDetailOfDate(date:String, detailItems: MutableList<OrderDetailItem>,source:String){
        val query:String = if(source == "purchase")
            "SELECT * FROM $TABLE_PURCHASE_ENTRY WHERE $KEY_DATE = '$date' ORDER BY $KEY_ITEM_NAME ASC"
        else
            "SELECT * FROM $TABLE_SALES_ENTRY WHERE $KEY_DATE = '$date' ORDER BY $KEY_ITEM_NAME ASC"
        val cursor = db.rawQuery(query, null)
        if(cursor.count>0){
            var prev = ""
            var totalCost = 0f
            var totalQty = 0f
            var qtyType = "pcs"
            var first = true
            while(cursor.moveToNext()){
                val itemName = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME))
                val qty = cursor.getString(cursor.getColumnIndex(KEY_QTY))
                val cost = cursor.getString(cursor.getColumnIndex(KEY_COST))
                Log.e("main item",itemName)
                Log.e("qty",qty)
                Log.e("cost",cost)


                if(itemName != prev && !first) {
                    Log.e("prev item",prev)
                    Log.e("total qty",totalQty.toString())
                    Log.e("total cost",totalCost.toString())
                    Log.e("qty type",qtyType)
                    val item = OrderDetailItem(prev, totalQty, totalCost,qtyType)
                    detailItems.add(item)
                    prev = itemName
                    totalCost = 0f
                    totalQty = 0f
                    totalCost += cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    val qty_mn_type = cursor.getString(cursor.getColumnIndex(KEY_QTY_MN_TYPE))
                    val qmty = cursor.getFloat(cursor.getColumnIndex(KEY_QTY))
                    val qty_type = cursor.getString(cursor.getColumnIndex(KEY_QTY_TYPE))
                    val convertQty:String = when(qty_mn_type){
                        "solid weight" -> "kilograms"
                        "liquid weight" -> "litres"
                        else -> "piece"
                    }
                    qtyType = convertQty

                    val insertQty = convertValue(qmty, qty_type, convertQty)
                    totalQty += insertQty
                }
                else{
                    if(first) {
                        first = false
                        prev = itemName
                    }
                    totalCost += cursor.getFloat(cursor.getColumnIndex(KEY_COST))
                    val qty_mn_type = cursor.getString(cursor.getColumnIndex(KEY_QTY_MN_TYPE))
                    val qnty = cursor.getFloat(cursor.getColumnIndex(KEY_QTY))
                    val qty_type = cursor.getString(cursor.getColumnIndex(KEY_QTY_TYPE))
                    val convertQty:String = when(qty_mn_type){
                        "solid weight" -> "kilograms"
                        "liquid weight" -> "litres"
                        else -> "piece"
                    }
                    val insertQty = convertValue(qnty, qty_type, convertQty)
                    totalQty += insertQty
                    qtyType = convertQty
                }
            }
            Log.e("prev item",prev)
            Log.e("total qty",totalQty.toString())
            Log.e("total cost",totalCost.toString())
            Log.e("qty type",qtyType)
            val item = OrderDetailItem(prev, totalQty, totalCost,qtyType)
            detailItems.add(item)
            cursor.close()
        }
    }

    // return false if no record exists
    private fun checkNameExists(itemName: String,db: SQLiteDatabase): Boolean{
        val query = "SELECT * FROM $TABLE_ITEM_LIST WHERE $KEY_ITEM_NAME = '$itemName'"
        val cursor = db.rawQuery(query, null)
        Log.e("cursor cout",cursor.count.toString())
        return if(cursor.count<=0) {
            cursor.close()
            Log.e("message","no record exists")
            false
        }else{
            Log.e("message","record exists")
            true
        }
    }

}