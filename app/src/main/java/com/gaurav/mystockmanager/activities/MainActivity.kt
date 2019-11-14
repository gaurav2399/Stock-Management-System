package com.gaurav.mystockmanager.activities

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.helper.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var myDatabase: SQLiteDatabase? = null
    private lateinit var password:String
    private lateinit var setPassword:MenuItem
    private lateinit var removePassword:MenuItem
    private lateinit var changePassword:MenuItem
    private var firstResume = true

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar as Toolbar)
        supportActionBar?.title = "My Stocks"

        myDatabase = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null)

        val wmbPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true)
        if (isFirstRun) {
            Log.e("first run","yes")
            myDatabase?.execSQL(CREATE_TABLE_ITEMS_LIST)
            myDatabase?.execSQL(CREATE_TABLE_PURCHASE_ENTRY)
            myDatabase?.execSQL(CREATE_TABLE_SALES_ENTRY)
            val editor = wmbPreference.edit()
            editor.putBoolean("FIRSTRUN", false)
            editor.apply()
        }

        password = getDefaults("password",applicationContext)!!

        addItem.setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            startActivity(intent)
        }

        addPurchaseEntry.setOnClickListener {
            val intent = Intent(this, PurchaseEntry::class.java)
            startActivity(intent)
        }

        addSalesEntry.setOnClickListener {
            val intent = Intent(this, SalesEntry::class.java)
            startActivity(intent)
        }

        purchaseOrder.setOnClickListener {
            val intent = Intent(this, PurchaseOrder::class.java)
            startActivity(intent)
        }

        saleOrder.setOnClickListener {
            val intent = Intent(this, SaleOrder::class.java)
            startActivity(intent)
        }

        stock.setOnClickListener {
            val intent = Intent(this, Stock::class.java)
            startActivity(intent)
        }
    }
    companion object{

        val context = this
        // creating items list table
        private const val CREATE_TABLE_ITEMS_LIST = ("CREATE TABLE "+ TABLE_ITEM_LIST + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ITEM_NAME + " TEXT," + KEY_QTY_MN_TYPE + " TEXT," + KEY_PER_COST_TYPE + " TEXT," + KEY_QTY_TYPE + " TEXT," + KEY_QTY +
                " FLOAT," + KEY_COST + " FLOAT," + KEY_ALERT_QTY + " FLOAT," + KEY_ALERT_QTY_TYPE + " TEXT);")

        // creating purchase entry table variable
        private const val CREATE_TABLE_PURCHASE_ENTRY = ("CREATE TABLE "+ TABLE_PURCHASE_ENTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_DATE + " TEXT," + KEY_VENDOR_NAME + " TEXT," + KEY_ITEM_NAME + " TEXT," + KEY_QTY_TYPE + " TEXT," + KEY_QTY + " FLOAT," +
                KEY_COST + " FLOAT," + KEY_QTY_MN_TYPE + " TEXT);")

        // creating sales entry table variable
        private const val CREATE_TABLE_SALES_ENTRY = ("CREATE TABLE "+ TABLE_SALES_ENTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_DATE + " TEXT," + KEY_ITEM_NAME + " TEXT," + KEY_QTY_TYPE + " TEXT," + KEY_QTY + " FLOAT," + KEY_COST + " FLOAT," +
                KEY_QTY_MN_TYPE + " TEXT);")

    }

    override fun onResume() {
        super.onResume()
        password = getDefaults("password",applicationContext)!!
        if(password != "" && !firstResume) {
            setPassword.isVisible = false
            removePassword.isVisible = true
            changePassword.isVisible = true
        }
        firstResume = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        setPassword = menu.findItem(R.id.setPassword)
        removePassword = menu.findItem(R.id.removePassword)
        changePassword = menu.findItem(R.id.changePassword)
        if(password != "") {
            setPassword.isVisible = false
            removePassword.isVisible = true
            changePassword.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.setPassword -> {
                val intent = Intent(this,PasswordActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.removePassword -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Do you really want to remove password?")
                alertDialog.setMessage("After this there is no security to your app.")
                alertDialog.setNegativeButton("Cancel") { p0, p1 -> p0.dismiss() }
                alertDialog.setPositiveButton("Remove") { p0, p1 ->
                    setDefaults("password","",applicationContext)
                    password = ""
                    setPassword.isVisible = true
                    removePassword.isVisible = false
                    changePassword.isVisible = false
                }
                alertDialog.create()
                alertDialog.show()
                true
            }
            R.id.changePassword -> {
                val intent = Intent(this,ChangePassword::class.java)
                intent.putExtra("password",password)
                startActivity(intent)
                true
            }
            R.id.alert -> {
                Toast.makeText(this,"ALert clicked",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
