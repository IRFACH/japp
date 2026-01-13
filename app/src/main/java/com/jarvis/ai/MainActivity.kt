package com.jarvis.ai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText = findViewById<EditText>(R.id.inputText)
        val sendBtn = findViewById<Button>(R.id.sendBtn)
        val outputText = findViewById<TextView>(R.id.outputText)

        sendBtn.setOnClickListener {
            val text = inputText.text.toString()
            sendToJarvis(text, outputText)
        }
    }

    private fun sendToJarvis(text: String, output: TextView) {
        val json = JSONObject()
        json.put("text", text)

        val body = RequestBody.create(
            MediaType.parse("application/json"),
            json.toString()
        )

        val request = Request.Builder()
            .url("http://127.0.0.1:8000/command")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    output.text = "Error: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body()?.string()
                runOnUiThread {
                    output.text = res ?: "No response"
                }
            }
        })
    }
}
