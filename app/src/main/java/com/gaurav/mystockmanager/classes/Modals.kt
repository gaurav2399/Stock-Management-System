package com.gaurav.mystockmanager.classes

data class PurchaseOrderItem(val date:String, val totalRs:Float, val vendorWiseCost: HashMap<String, Float>)

data class SellOrderItem(val date:String, val totalRs:Float)

data class OrderDetailItem(val itemName:String, val qty:Float, val cost:Float, val qtyType:String)

data class ItemStock(val itemName: String, val qty:String, val qtyType: String, val alert:Int, val addValue:Float)

data class VendorOrderItem(val date:String, val vendor:String, val cost: String)