package ecke.smstorest.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import ecke.smstorest.model.Config
import org.json.JSONObject

class SMSReceiver : BroadcastReceiver() {

    companion object {

        // Set up the network to use HttpURLConnection as the HTTP client.
        private val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(NoCache(), network).apply {
            start()
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        // Get SMS map from Intent
        if (context == null || intent == null || intent.action == null) {
            return
        }

        if (intent.action != (Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            return
        }

        Config.read(context)
        if(!Config.configComplete()){
            Toast.makeText(
                context,
                "received sms but config is not complete",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // val contentResolver = context.contentResolver
        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (message in smsMessages) {

            // show incoming message as toast
            Toast.makeText(
                context,
                "Message from ${message.displayOriginatingAddress} : body ${message.messageBody}",
                Toast.LENGTH_SHORT
            ).show()

            // send REST
            val assembledMessage = "Message from: "+message.displayOriginatingAddress + " \n" + message.messageBody
//            val messageOut = Json.encodeToString(
//                RestMessage(assembledMessage)
//            )

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                Config.endpoint,
                JSONObject("{'text':'$assembledMessage'}"),
                { response ->
                    Toast.makeText(
                        context,
                        "successfully sent REST",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                { error ->
                    Toast.makeText(
                        context,
                        "failed sending REST",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            requestQueue.add(jsonObjectRequest)
        }
    }
}