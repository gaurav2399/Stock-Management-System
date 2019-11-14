package com.gaurav.mystockmanager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.gaurav.mystockmanager.R
import com.gaurav.mystockmanager.helper.getDefaults
import kotlinx.android.synthetic.main.splash_screen.*


class Splash:Activity() {
    /** Duration of wait  */
    private val SPLASH_DISPLAY_LENGTH:Long = 1000

    /** Called when the activity is first created.  */
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.splash_screen)

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        val password = getDefaults("password",applicationContext)
        if(password == "") {
            Log.e("passowrd", "empty")
            Handler().postDelayed( {
                /* Create an Intent that will start the Menu-Activity. */
                val mainIntent = Intent(this@Splash, MainActivity::class.java)
                this@Splash.startActivity(mainIntent)
                this@Splash.finish()
            }, SPLASH_DISPLAY_LENGTH)
        }
        else {
            Log.e("passowrd", password)
            editText.visibility = View.VISIBLE
            button.visibility = View.VISIBLE
        }

        button.setOnClickListener {
            if(editText.text.toString() == ""){
                Toast.makeText(this,"Password must be filled",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(editText.text.toString() == password){
                val mainIntent = Intent(this@Splash, MainActivity::class.java)
                this@Splash.startActivity(mainIntent)
                this@Splash.finish()
            }else{
                Toast.makeText(this,"Wrong Password.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }


    }
}