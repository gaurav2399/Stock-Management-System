package com.gaurav.mystockmanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.helper.setDefaults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        setSupportActionBar(password_toolbar as Toolbar)
        supportActionBar?.title = "Set Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val context = applicationContext

        savePassword.setOnClickListener {
            if(password2.text.toString() == "" || confirmPassword.text.toString() == ""){
                Toast.makeText(this,"Fill both the fields",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(password2.text.toString() != confirmPassword.text.toString()) {
                Toast.makeText(this, "Both password field must be same", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            setDefaults("password",password2.text.toString(),context)
            Toast.makeText(this, "Changes Applied.", Toast.LENGTH_LONG).show()
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
