package com.example.escuteiros.ui.Perfil

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.escuteiros.Atualizar
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class EditarPerfilFragment : Fragment() {

    lateinit var  nome     : EditText
    lateinit var  email    : EditText
    lateinit var  password : EditText
    lateinit var  nin      : EditText
    lateinit var  morada   : EditText
    lateinit var  data     : EditText
    lateinit var  contacto : EditText

    lateinit var  imagem : ImageView

    lateinit var generoescolha : Spinner

    lateinit var buttonSave : Button
    lateinit var buttonVer : Button
    lateinit var buttonUpload : Button


    val generos = arrayOf("Masculino","Feminino")
    var escolhagenero : String = ""

    var nome1 : String = ""
    var email1 : String = ""
    var password1 : String = ""
    var nin1 : Int = 0
    var morada1 : String = ""
    var data1 : String = ""
    var genero1 : String = ""
    var contacto1 : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val menuItem1: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem1.isVisible = false

        val menuItem2: MenuItem = menu.findItem(R.id.itemVerPresenca)
        menuItem2.isVisible = false

        val menuItem: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem.isVisible = false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.editar_perfil_fragment, container, false)

        nome = rootView.findViewById(R.id.editnome)
        email = rootView.findViewById(R.id.email)
        password = rootView.findViewById(R.id.password)
        nin = rootView.findViewById(R.id.nin)
        morada = rootView.findViewById(R.id.morada)
        data = rootView.findViewById(R.id.data)
        contacto = rootView.findViewById(R.id.contacto)

        buttonSave = rootView.findViewById(R.id.editar_button)
        buttonVer = rootView.findViewById(R.id.verPassword)
        buttonUpload = rootView.findViewById(R.id.button)

        generoescolha = rootView.findViewById(R.id.genero)

        imagem = rootView.findViewById(R.id.Imagem)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        nome.setText(Dados[0].nome)
        email.setText(Dados[0].email)
        password.setText(Dados[0].password)
        nin.setText(Dados[0].nin.toString())
        morada.setText(Dados[0].morada)
        data.setText(Dados[0].data)
        contacto.setText(Dados[0].contacto.toString())

        if(Dados[0].imagem.trim().toString() != "null") {
            Picasso.get().load(Dados[0].imagem).resize(250, 250).into(imagem)
        }
        else
        {
            Picasso.get().load("https://i.imgur.com/V9CRLdo.png").resize(250, 250).into(imagem)
        }
        buttonUpload.setOnClickListener(){
            activity?.let{
                val intent = Intent (it, Atualizar::class.java)
                it.startActivity(intent)
            }
        }
        var ver : Boolean = true
        buttonVer.setOnClickListener()
        {
            if(ver == false){
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                ver = true
            } else{
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ver = false
            }
        }
        val adapter3 = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, generos)
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        generoescolha.adapter = adapter3
        generoescolha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhagenero = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        generoescolha.setSelection(adapter3.getPosition(Dados[0].genero))

        buttonSave.setOnClickListener() {
            if (nome.text.toString() == "") {
                nome1 = Dados[0].nome
            } else if (nome.text.toString() != "") {
                nome1 = nome.text.toString()
            }
            if (email.text.toString() == "") {
                email1 = Dados[0].email
            } else {
                email1 = email.text.toString()
            }
            if (password.text.toString() == "") {
                password1 = Dados[0].password
            } else {
                password1 = password.text.toString()
            }
            if (nin.text.toString() == "") {
                nin1 = Dados[0].nin.toString().toInt()
            } else {
                nin1 = nin.text.toString().toInt()
            }
            if (morada.text.toString() == "") {
                morada1 = Dados[0].morada
            } else {
                morada1 = morada.text.toString()
            }
            if (data.text.toString() == "") {
                data1 = Dados[0].data
            } else {
                data1 = data.text.toString()
            }
            if (contacto.text.toString() == "") {
                contacto1 = Dados[0].contacto
            } else {
                contacto1 = contacto.text.toString().toInt()
            }
            if (escolhagenero != Dados[0].genero.toString()) {
                genero1 = escolhagenero
            } else if (escolhagenero == Dados[0].genero.toString()) {
                genero1 = escolhagenero
            }

            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()

                val utilizador = Utilizadores(
                    Dados[0].id,
                    nome1,
                    email1,
                    password1,
                    nin1,
                    morada1,
                    contacto1,
                    genero1,
                    data1,
                    Dados[0].permissao,
                    Dados[0].id_seccao,
                    Dados[0].imagem,
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
    }

}