package com.example.escuteiros

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.escuteiros.Models.ProcurarImagem
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.ui.home.Dados
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class Atualizar : AppCompatActivity() {

    private lateinit var loadingView: AlertDialog
    private var imagePicker: ProcurarImagem = ProcurarImagem(this)
    private lateinit var selectedImage: Bitmap
    private lateinit var select_image: Button
    lateinit var upload_image: Button
    lateinit var selected_image: ImageView
    private var imgurUrl: String = ""

    private val CLIENT_ID = "56bc21f156afc2f"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atualizar)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        loadingView = builder.create()

        select_image = findViewById<Button>(R.id.select_image)
        upload_image = findViewById<Button>(R.id.upload_image)
        selected_image = findViewById(R.id.selected_image)
        select_image.setOnClickListener {
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save in External Storage(Micro SD)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                imagePicker.askPermissions()
            } else {
                imagePicker.pickImage()
            }
        }

        upload_image.setOnClickListener {
            uploadImageToImgur(selectedImage)
        }
    }
    private fun uploadImageToImgur(image: Bitmap) {
        loadingView.show()
        getBase64Image(image, complete = { base64Image ->
            GlobalScope.launch(Dispatchers.Default) {
                val url = URL("https://api.imgur.com/3/image")

                val boundary = "Boundary-${System.currentTimeMillis()}"

                val httpsURLConnection =
                    withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.setRequestProperty("Authorization", "Client-ID $CLIENT_ID")
                httpsURLConnection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )

                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                var body = ""
                body += "--$boundary\r\n"
                body += "Content-Disposition:form-data; name=\"image\""
                body += "\r\n\r\n$base64Image\r\n"
                body += "--$boundary--\r\n"


                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(body)
                    outputStreamWriter.flush()
                }
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                val data = jsonObject.getJSONObject("data")
                loadingView.dismiss()
                Log.d("TAG", "Link is : ${data.getString("link")}")
                imgurUrl = data.getString("link")

                trocarimagem()
            }
        })
    }
    private fun trocarimagem() {

        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()

            val utilizador = Utilizadores(
                Dados[0].id,
                Dados[0].nome,
                Dados[0].email,
                Dados[0].password,
                Dados[0].nin,
                Dados[0].morada,
                Dados[0].contacto,
                Dados[0].genero,
                Dados[0].data,
                Dados[0].permissao,
                Dados[0].id_seccao,
                imgurUrl,
                Dados[0].nome_grupo,
                Dados[0].inscrito,
                Dados[0].data_inscricao)

            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                utilizador.toJson().toString()
            )
            Log.d("coudelaria", utilizador.toJson().toString())
            val request = Request.Builder()
                .url("http://173.212.215.122:5000/api/utilizador/${Dados[0].id}")
                .put(requestBody)
                .build()
            client.newCall(request).execute().use { response ->
                Log.d("coudelaria", response.message)

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val results: Bitmap? = imagePicker.onActivityResult(requestCode, resultCode, data)
        if (results != null) {
            selectedImage = results
            selected_image.setImageBitmap(selectedImage)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun getBase64Image(image: Bitmap, complete: (String) -> Unit) {
        GlobalScope.launch {
            val outputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val b = outputStream.toByteArray()
            complete(Base64.encodeToString(b, Base64.DEFAULT))
        }
    }
}