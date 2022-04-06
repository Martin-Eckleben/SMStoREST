package ecke.smstorest.controller

import ecke.smstorest.R
import android.content.Context
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

import kotlinx.android.synthetic.main.activity_main.endpoint_input
import kotlinx.android.synthetic.main.activity_main.statusText
import kotlinx.android.synthetic.main.activity_main.imageView

import ecke.smstorest.model.Config
import android.content.Intent
import ecke.smstorest.service.ContinuityService


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // if configs are set -> fill input fields
        Config.read(this)
        Config.endpoint?.let {
            endpoint_input.setText(Config.endpoint)
        }
        checkConfigComplete()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                "android.permission.RECEIVE_SMS",
                "android.permission.READ_SMS",
                "android.permission.INTERNET"
            ),
            1
        )

        startService(Intent(this, ContinuityService::class.java))
    }

    fun saveSettings(view: View) {
        val sharedPref = getSharedPreferences("config", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("endpoint", endpoint_input.text.toString())
            apply()
        }

        // show as toast
        Toast.makeText(
            this,
            "saved!",
            Toast.LENGTH_SHORT
        ).show()

        checkConfigComplete()
    }

    private fun checkConfigComplete(){
        Config.read(this)
        if(!Config.configComplete()){
            statusText.text = "complete config"
            imageView.setImageResource(R.drawable.redlight)
        }
        else{
            statusText.text = "listening"
            imageView.setImageResource(R.drawable.greenlight)
        }
    }
}