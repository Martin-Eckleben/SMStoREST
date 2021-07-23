package ecke.smstorest.model

import android.content.Context

class Config {
    companion object {
        var endpoint:String? = null

        fun read(context:Context){
            val sharedPref = context.getSharedPreferences("config", Context.MODE_PRIVATE) ?: return
            this.endpoint = sharedPref.getString("endpoint", null)
        }

        fun configComplete():Boolean{
            if(this.endpoint!=null)
                return true
            return false
        }
    }
}