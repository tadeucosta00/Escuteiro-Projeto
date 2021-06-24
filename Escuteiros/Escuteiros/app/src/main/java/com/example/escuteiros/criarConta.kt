package com.example.escuteiros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import com.example.escuteiros.Models.Utilizadores
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*

class criarConta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)
        var  nome : EditText
        lateinit var  email : EditText
        lateinit var  password : EditText
        lateinit var  nin : EditText
        lateinit var  morada : EditText
        lateinit var  genero : Spinner
        lateinit var  contacto : EditText
        lateinit var  data : DatePicker

        lateinit var  buttonSave : Button
        lateinit var  verpass : Button

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val dataAgora = sdf.format(Date())

        val generos = arrayOf("Masculino","Feminino")
        var escolhagenero  = "Masculino"
        var datanascimento : String  = dataAgora

        var intent = Intent(this, Login::class.java).apply {
        }

        nome = findViewById<EditText>(R.id.editnome)
        email = findViewById<EditText>(R.id.email)
        password = findViewById<EditText>(R.id.password)
        nin = findViewById<EditText>(R.id.nin)
        morada = findViewById<EditText>(R.id.morada)
        genero = findViewById(R.id.genero)
        data = findViewById<DatePicker>(R.id.date_PickerInicial)
        contacto = findViewById<EditText>(R.id.contacto)
        buttonSave = findViewById<Button>(R.id.editar_button)
        verpass = findViewById(R.id.verPassword)



        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        genero.adapter = adapter

        genero.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                escolhagenero =  parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }

        val today = Calendar.getInstance()
        data.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            datanascimento = "$day-$month-$year".toString()
        }

        var ver : Boolean = true
        verpass.setOnClickListener(){
            if(ver == false){
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                ver = true
            } else{
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ver = false
            }
        }

        buttonSave.setOnClickListener {

            var nome1 : String = nome.text.toString()
            var email1 : String = email.text.toString()
            var password1 : String = password.text.toString()
            var nin1 : String = nin.text.toString()
            var morada1 : String = morada.text.toString()
            var contacto1 : String = contacto.text.toString()

            if (nome1.isNullOrEmpty() || email1.isNullOrEmpty() || password1.isNullOrEmpty() || nin1.isNullOrEmpty() || morada1.isNullOrEmpty() || contacto1.isNullOrEmpty())
            {
                Toast.makeText(this, "Por favor preencha todos os campos!!" , Toast.LENGTH_SHORT).show()
            }
            else
            {
                val client = OkHttpClient()
                val utilizador = Utilizadores(
                    null,
                    nome1,
                    email1,
                    password1,
                    nin1.toInt(),
                    morada1 ,
                    contacto1.toInt(),
                    escolhagenero ,
                    datanascimento,
                    1,
                    null,
                    "null",
                    "null",
                    "Inscrito",
                    "01-01-1970"
                    )

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    utilizador.toJson().toString()
                )
                Log.d("coudelaria", utilizador.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/utilizador")
                    .post(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                }
                Toast.makeText(this, "Registo efetuado com sucesso!" , Toast.LENGTH_SHORT).show()
            startActivity(intent)
            }
        }
    }


}
