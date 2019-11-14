package com.gaurav.mystockmanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.helper.setDefaults
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_password.*

class ChangePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setSupportActionBar(changePassword_toolbar as Toolbar)
        supportActionBar?.title = "Change Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prePassword = intent.getStringExtra("password")
        Log.e("present password",prePassword)

        saveChange.setOnClickListener {
            if(presentPassword.text.toString() != prePassword && presentPassword.text.toString() != ""){
                Toast.makeText(this,"Enter the correct password",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(presentPassword.text.toString() == "" ||
                newPassword.text.toString() == "" ||
                    confirmPassword2.text.toString() == ""){
                Toast.makeText(this,"All the fields must be filled",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(newPassword.text.toString() != confirmPassword2.text.toString()){
                Toast.makeText(this,"Last two fields must be same",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setDefaults("password",newPassword.text.toString(),applicationContext)
            Toast.makeText(this,"Changes Applied",Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
