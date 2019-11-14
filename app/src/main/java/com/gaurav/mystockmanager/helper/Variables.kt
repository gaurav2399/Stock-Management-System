package com.gaurav.mystockmanager.helper

import android.util.Log
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


const val DATABASE_NAME = "MyShop.db"
const val TABLE_ITEM_LIST = "ItemsList"
const val TABLE_PURCHASE_ENTRY = "PurchaseEntry"
const val TABLE_SALES_ENTRY = "SalesEntry"
const val KEY_ID = "Id"
const val KEY_ITEM_NAME = "ItemName"
const val KEY_QTY_MN_TYPE = "Qty_main_type"
const val KEY_QTY_TYPE = "Qty_type"
const val KEY_PER_COST_TYPE = "per_cost_type"
const val KEY_QTY = "qty"
const val KEY_COST = "cost"
const val KEY_VENDOR_NAME = "vendor_name"
const val KEY_ALERT_QTY = "alert_qty"
const val KEY_ALERT_QTY_TYPE = "alert_qty_type"
const val KEY_DATE = "date"

const val FUNCTION_WORK_DONE = 1
const val FUNCTION_WORK_NOT_DONE = 0
const val ITEM_EXISTS_ALREADY = 2
const val NOT_AVAILABLE_MUCH_AMOUNT = 3
const val ALERT_NEEDED = 4

val MONTHS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

val qty_type_list_piece = arrayOf("piece")
val qty_type_list_solid = arrayOf("gram", "kilogram", "milligram", "pound", "tonne")
val qty_type_list_liquid = arrayOf("litres", "millilitres", "gallon")

fun convertValue(qty: Float, qtyType: String, itemQtyType: String?): Float {
    Log.e("reach in convert value", "yes")
    when (qtyType) {
        "gram" -> {
            return when (itemQtyType) {
                "kilogram" -> qty / 1000
                "milligram" -> qty * 1000
                "pound" -> qty / 453.592f
                "tonne" -> qty / 1000000
                "gram" -> qty
                else -> qty
            }
        }
        "kilogram" -> {
            return when (itemQtyType) {
                "kilogram" -> qty
                "milligram" -> qty * 1000000
                "pound" -> qty * 2.205f
                "tonne" -> qty / 1000
                "gram" -> qty * 1000
                else -> qty
            }
        }
        "milligram" -> {
            return when (itemQtyType) {
                "kilogram" -> qty / 1000000
                "milligram" -> qty
                "pound" -> qty / 453592.37f
                "tonne" -> qty / 1000000000
                "gram" -> qty / 1000
                else -> qty
            }
        }
        "pound" -> {
            return when (itemQtyType) {
                "kilogram" -> qty / 2.205f
                "milligram" -> qty * 453592.37f
                "pound" -> qty
                "tonne" -> qty / 2204.623f
                "gram" -> qty * 453.592f
                else -> qty
            }
        }
        "tonne" -> {
            return when (itemQtyType) {
                "kilogram" -> qty * 1000
                "milligram" -> qty * 1000000000
                "pound" -> qty * 2204.623f
                "tonne" -> qty
                "gram" -> qty * 1000000
                else -> qty
            }
        }
        "litres" -> {
            return when (itemQtyType) {
                "litres" -> qty
                "millilitres" -> qty * 1000
                "gallon" -> qty / 3.785f
                else -> qty
            }
        }
        "millilitres" -> {
            return when (itemQtyType) {
                "litres" -> qty / 1000
                "millilitres" -> qty
                "gallon" -> qty / 3785.412f
                else -> qty
            }
        }
        "gallon" -> {
            return when (itemQtyType) {
                "litres" -> qty * 3.785f
                "millilitres" -> qty * 3785.412f
                "gallon" -> qty
                else -> qty
            }
        }
        else -> qty
    }
    return qty
}

fun setDefaults(key: String, value: String, context: Context) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = preferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getDefaults(key: String, context: Context): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getString(key, "")
}