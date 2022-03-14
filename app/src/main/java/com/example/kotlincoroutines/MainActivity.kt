package com.example.kotlincoroutines

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

/**
 * Created by Arshad.......
 */
class MainActivity : AppCompatActivity() {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)

    companion object {
        private var TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_search = findViewById<Button>(R.id.button_search)
        val progress_bar = findViewById<ProgressBar>(R.id.progress_bar)
        button_search.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            coroutineScope.launch(Dispatchers.Main) {
                SendMail()
            }
        }

    }

    private suspend fun SendMail() {
        val progress_bar = findViewById<ProgressBar>(R.id.progress_bar)
        val mail_id_text_view = findViewById<TextView>(R.id.mail_id_text_view)
        val msg_text_view = findViewById<TextView>(R.id.msg_text_view)
        val sent_success_text_view = findViewById<TextView>(R.id.sent_success_text_view)
        val mailId = coroutineScope.async(Dispatchers.IO) { getMailFromDB() }
        val mailMsg = coroutineScope.async(Dispatchers.IO) { getMessageFromDB() }
        mail_id_text_view.text = String.format(
            getString(R.string.email_id_string),
            mailId.await()
        )
        msg_text_view.text = String.format(
            getString(R.string.email_msg),
            mailMsg.await()

        )
        val msg = coroutineScope.async(Dispatchers.IO) {
            sendMsgFromApi(mailId.await(), mailMsg.await())
        }
        Log.d(TAG, "Done Sending msg " + msg.await())
        sent_success_text_view.text = String.format(
            getString(R.string.msg_success), msg.await()
        )
        progress_bar.visibility = View.GONE
    }

}


private suspend fun sendMsgFromApi(mailId: String, msg: String): String {
    //send from API
    delay(2000)
    return "$msg to $mailId"
}

private suspend fun getMailFromDB(): String {
    delay(3000) //query from DB will be here
    return "arshadk08967@gmail.com"
}

private suspend fun getMessageFromDB(): String {
    delay(3000)
    return "This is an example for coroutines"
}



