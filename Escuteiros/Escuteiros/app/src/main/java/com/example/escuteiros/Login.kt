package com.example.escuteiros

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.preference.PreferenceManager
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.escuteiros.Models.Data
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.ui.home.Dados
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject


class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        var botao = findViewById<Button>(R.id.login)
        var botao2 = findViewById<Button>(R.id.criarconta)
        var checkBox = findViewById<CheckBox>(R.id.checkBox)
        var checked : Boolean = false
        var numero :Int
        var utilizadores : MutableList<Utilizadores> = arrayListOf()
        val client = OkHttpClient()
        var intent = Intent(this, criarConta::class.java).apply {
        }
        var intent2 = Intent(this, Menu::class.java).apply {
        }
        val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = manager.activeNetworkInfo

        if (null == activeNetwork) {
            val dialogBuilder = AlertDialog.Builder(this)
            val intent = Intent(this, MainActivity::class.java)
            // set message of alert dialog
            dialogBuilder.setMessage("Por favor verifique a sua ligação a Internet")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Tentar novamente", DialogInterface.OnClickListener { dialog, id ->
                    recreate()
                })
                // negative button text and action
                .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Não existe conexão")
            alert.setIcon(R.mipmap.ic_logo)
            // show alert dialog
            alert.show()
        }
        else{

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var textViewemail = findViewById<EditText>(R.id.email)
        var textViewpassword = findViewById<EditText>(R.id.password)


        val sharedPreferences = getSharedPreferences("Escuteiros", Context.MODE_PRIVATE)

        val request = Request.Builder().url("http://173.212.215.122:5000/api/utilizador").build()

        client.newCall(request).execute().use { response ->
            val jsStr : String = response.body!!.string()
            println(jsStr)
            val jsonArrayUtilizadores = JSONArray(jsStr)
            numero = jsonArrayUtilizadores.length()
            for ( index in  0 until jsonArrayUtilizadores.length()) {
                val jsonArticle : JSONObject = jsonArrayUtilizadores.get(index) as JSONObject
                val utlizador = Utilizadores.fromJson(jsonArticle)
                utilizadores.add(utlizador)
            }
        }


        botao.setOnClickListener() {

            var verificador: Boolean = false

            if (textViewemail.text.toString() == "" && textViewpassword.text.toString() == "") {
                Toast.makeText(this, "Por favor preencha todos os campos!!" , Toast.LENGTH_SHORT).show()
            }
            else
            {
                for (index in 0 until numero)
                {
                    if (textViewemail.text.trim().toString() == utilizadores[index].email && textViewpassword.text.trim().toString() == utilizadores[index].password)
                    {
                        val editor = sharedPreferences.edit()
                        editor.putString("EMAIL",utilizadores[index].email.toString())
                        editor.putString("PASSWORD",utilizadores[index].password.toString())
                        if (checkBox.isChecked)
                        {
                            checked = true
                        }
                        editor.putBoolean("CHECKBOX",checked)
                        editor.apply()

                        verificador = true
                        Toast.makeText(this, "Bem-Vindo " + utilizadores[index].nomeUtilizador, Toast.LENGTH_SHORT).show()
                        Dados.clear()
                        Dados.add(Data(utilizadores[index].codId.toString().toInt(), utilizadores[index].email.toString(), utilizadores[index].password.toString(),utilizadores[index].nomeUtilizador.toString(),utilizadores[index].permissao.toString().toInt(),utilizadores[index].imagem.toString(),utilizadores[index].morada.toString(),utilizadores[index].contato.toString().toInt(),utilizadores[index].genero.toString(),utilizadores[index].data.toString(),utilizadores[index].nin.toString().toInt(),utilizadores[index].id_secção.toString().toInt(),utilizadores[index].nome_grupo.toString(), utilizadores[index].inscrito.toString(),utilizadores[index].data_inscricao.toString()))
                        startActivity(intent2)
                        finish()
                    }
                }
            if (verificador == false)
            {
                Toast.makeText(this, "Creedencias Invalidas!! ", Toast.LENGTH_SHORT).show()
            }
        }
        }
        var checked1 = sharedPreferences.getBoolean("CHECKBOX",false)
        if(checked1 == true){
        var emailguardado = sharedPreferences.getString("EMAIL","")
        var passwordguardada = sharedPreferences.getString("PASSWORD","")

        for (index in 0 until numero)
        {
            if (emailguardado == utilizadores[index].email && passwordguardada == utilizadores[index].password)
            {
                Toast.makeText(this, "Bem-Vindo " + utilizadores[index].nomeUtilizador, Toast.LENGTH_SHORT).show()
                Dados.clear()
                Dados.add(Data(utilizadores[index].codId.toString().toInt(), utilizadores[index].email.toString(), utilizadores[index].password.toString(),utilizadores[index].nomeUtilizador.toString(),utilizadores[index].permissao.toString().toInt(),utilizadores[index].imagem.toString(),utilizadores[index].morada.toString(),utilizadores[index].contato.toString().toInt(),utilizadores[index].genero.toString(),utilizadores[index].data.toString(),utilizadores[index].nin.toString().toInt(),utilizadores[index].id_secção.toString().toInt(),utilizadores[index].nome_grupo.toString(), utilizadores[index].inscrito.toString(),utilizadores[index].data_inscricao.toString()))
                startActivity(intent2)
                finish()
            }
        }
        }

        botao2.setOnClickListener()
        {
            startActivity(intent)
        }
        }
    }

}






