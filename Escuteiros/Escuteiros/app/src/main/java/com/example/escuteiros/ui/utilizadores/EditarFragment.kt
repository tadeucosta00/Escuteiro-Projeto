package com.example.escuteiros.ui.utilizadores

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.escuteiros.Models.Seccao
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

class EditarFragment : Fragment() {

    private var  UtilizadorJsonStr: String? = null

    var seccoes : MutableList<Seccao> = arrayListOf()
    lateinit var  nome     : EditText
    lateinit var  email    : EditText
    lateinit var  password : EditText
    lateinit var  nin      : EditText
    lateinit var  morada   : EditText
    lateinit var  data     : EditText
    lateinit var  contacto : EditText
    lateinit var  nomegrupo : EditText
    lateinit var  datainscricao : EditText

    lateinit var  imagem : ImageView

    lateinit var  seccaoescolha : Spinner
    lateinit var  permissaoescolha : Spinner
    lateinit var  generoescolha : Spinner
    lateinit var  inscricaoescolha : Spinner

    lateinit var buttonSave : Button
    lateinit var buttonVer : Button
    lateinit var buttonEliminar : Button

    var escolhaseccao : String = ""
    var escolhapermissao : String = ""
    var nomeSeccoes = arrayOf<String>("","","","")
    var permissoes = arrayOf<String>("Escuteiro","Dirigente","Administrador")
    var escolhagenero : String = ""
    val generos = arrayOf("Masculino","Feminino")
    val inscricao = arrayOf("Inscrito","Não Inscrito")
    var idSeccao : Int = 0
    var nome1 : String = ""
    var email1 : String = ""
    var password1 : String = ""
    var nin1 : Int = 0
    var morada1 : String = ""
    var data1 : String = ""
    var genero1 : String = ""
    var contacto1 : Int = 0
    var nomegrupo1 : String = ""
    var inscricao1 : String = ""
    var datainscricao1 : String = ""
    var escolhainscricao : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            UtilizadorJsonStr = it.getString("utilizadores")
        }
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem1: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem1.isVisible = false

        val menuItem: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem.isVisible = false

        val menuItem2: MenuItem = menu.findItem(R.id.itemVerPresenca)
        menuItem2.isVisible = false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.editar_fragment, container, false)
        seccaoescolha = rootView.findViewById(R.id.seccao)
        permissaoescolha = rootView.findViewById(R.id.cargo)
        generoescolha = rootView.findViewById(R.id.genero)
        inscricaoescolha = rootView.findViewById(R.id.inscricao)
        nome = rootView.findViewById(R.id.editnome)
        email = rootView.findViewById(R.id.email)
        password = rootView.findViewById(R.id.password)
        nin = rootView.findViewById(R.id.nin)
        morada = rootView.findViewById(R.id.morada)
        data = rootView.findViewById(R.id.data)
        contacto = rootView.findViewById(R.id.contacto)
        nomegrupo = rootView.findViewById(R.id.grupo)
        datainscricao = rootView.findViewById(R.id.dataInscricao)
        buttonSave = rootView.findViewById(R.id.editar_button)
        imagem = rootView.findViewById(R.id.Imagem)


        buttonVer = rootView.findViewById(R.id.verPassword)
        buttonEliminar = rootView.findViewById(R.id.eliminar_button)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val utilizadoresTemp = Utilizadores.fromJson(JSONObject(UtilizadorJsonStr))

        if(utilizadoresTemp.imagem != "null") {
            Picasso.get().load(utilizadoresTemp.imagem).resize(250, 250).into(imagem)
        }
        else
        {
            Picasso.get().load("https://i.imgur.com/V9CRLdo.png").resize(250, 250).into(imagem)
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

        nome.setText(utilizadoresTemp.nomeUtilizador)
        email.setText(utilizadoresTemp.email)
        password.setText(utilizadoresTemp.password)
        nin.setText(utilizadoresTemp.nin.toString())
        morada.setText(utilizadoresTemp.morada)
        data.setText(utilizadoresTemp.data)
        contacto.setText(utilizadoresTemp.contato.toString())
        nomegrupo.setText(utilizadoresTemp.nome_grupo.toString())
        datainscricao.setText(utilizadoresTemp.data_inscricao.toString())

        var escuteiro = "Escuteiro"
        var dirigente = "Dirigente"
        var admin = "Administrador"
        var permissao1 : Int = 1

        val request = Request.Builder().url("http://173.212.215.122:5000/api/seccao").build()
        val client = OkHttpClient()
        var contador = 0
        client.newCall(request).execute().use { response ->
            val jsStr: String = response.body!!.string()
            println(jsStr)
            val jsonArraySeccao = JSONArray(jsStr)

            for (index in 0 until jsonArraySeccao.length()) {
                val jsonArticle: JSONObject = jsonArraySeccao.get(index) as JSONObject
                val seccao = Seccao.fromJson(jsonArticle)
                seccoes.add(seccao)
                nomeSeccoes[index] = seccao.nome_seccao.toString()
                contador++
            }
        }
        //spinnerseccoes
        val adapter = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, nomeSeccoes)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        seccaoescolha.adapter = adapter
        seccaoescolha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhaseccao = parent.getItemAtPosition(position).toString()
                for(index in 0 until contador)
                {
                    if (escolhaseccao == seccoes[index].nome_seccao)
                    {
                        idSeccao = seccoes[index].id_seccao!!
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        if (utilizadoresTemp.id_secção == 1)
            seccaoescolha.setSelection(adapter.getPosition("Lobitos"))
        else if(utilizadoresTemp.id_secção == 2)
            seccaoescolha.setSelection(adapter.getPosition("Exploradores"))
        else if(utilizadoresTemp.id_secção == 3)
            seccaoescolha.setSelection(adapter.getPosition("Pioneiros"))
        else if(utilizadoresTemp.id_secção == 4)
            seccaoescolha.setSelection(adapter.getPosition("Caminheiros"))

        //spinnerpermissoes
        val adapter2 = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, permissoes)
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        permissaoescolha.adapter = adapter2
        permissaoescolha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhapermissao = parent.getItemAtPosition(position).toString()
                if (escolhaseccao.trim() == escuteiro)
                {
                    permissao1 = 1
                }
                else if (escolhaseccao.trim() == dirigente)
                {
                    permissao1 = 2
                }
                else if (escolhaseccao.trim() == admin)
                {
                    permissao1 = 3
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        if (utilizadoresTemp.permissao == 1)
            permissaoescolha.setSelection(adapter2.getPosition(escuteiro))
        else if(utilizadoresTemp.permissao == 2)
            permissaoescolha.setSelection(adapter2.getPosition(dirigente))
        else if(utilizadoresTemp.permissao == 3)
            permissaoescolha.setSelection(adapter2.getPosition(admin))

        //spinnergenero
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
        generoescolha.setSelection(adapter3.getPosition(utilizadoresTemp.genero))
        //spinnerinscricao
        val adapter4 = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, inscricao)
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        inscricaoescolha.adapter = adapter4
        inscricaoescolha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhainscricao = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        inscricaoescolha.setSelection(adapter4.getPosition(utilizadoresTemp.inscrito))
        buttonSave.setOnClickListener(){

        if(nome.text.toString() == ""){
            nome1 = utilizadoresTemp.nomeUtilizador.toString()
        }
        else if(nome.text.toString() != "")
        {
            nome1 = nome.text.toString()
        }
        if(email.text.toString() == ""){
            email1 = utilizadoresTemp.email.toString()
        }
        else
        {
            email1 = email.text.toString()
        }
        if(password.text.toString() == ""){
            password1 = utilizadoresTemp.password.toString()
        }
        else
        {
            password1 = password.text.toString()
        }
        if(nin.text.toString() == ""){
            nin1 = utilizadoresTemp.nin.toString().toInt()
        }
        else
        {
            nin1 = nin.text.toString().toInt()
        }
        if(morada.text.toString() == ""){
            morada1 = utilizadoresTemp.morada.toString()
        }
        else
        {
            morada1 = morada.text.toString()
        }
        if(data.text.toString() == ""){
            data1 = utilizadoresTemp.data.toString()
        }
        else
        {
            data1 = data.text.toString()
        }
        if(contacto.text.toString() == ""){
            contacto1 = utilizadoresTemp.contato.toString().toInt()
        }
        else
        {
            contacto1 = contacto.text.toString().toInt()
        }
        if(nomegrupo.text.toString() == ""){
            nomegrupo1 = utilizadoresTemp.nome_grupo.toString()
        }
        else
        {
            nomegrupo1 = nomegrupo.text.toString()
        }
        if(datainscricao.text.toString() == ""){
            datainscricao1 = utilizadoresTemp.data_inscricao.toString()
        }
        else
        {
            datainscricao1 = datainscricao.text.toString()
        }

        if (escolhagenero != utilizadoresTemp.genero.toString())
        {
            genero1 = escolhagenero
        }
        else if (escolhagenero == utilizadoresTemp.genero.toString())
        {
            genero1 = escolhagenero
        }
        if (escolhapermissao == escuteiro)
        {
             permissao1 = 1
        }
        else if (escolhapermissao == dirigente)
        {
             permissao1 = 2
        }
        else if (escolhapermissao == admin)
        {
             permissao1 = 3
        }

        for(index in 0 until contador)
        {
           if (escolhaseccao == seccoes[index].nome_seccao)
           {
               idSeccao = seccoes[index].id_seccao!!
           }
        }
        if (escolhainscricao != utilizadoresTemp.inscrito.toString())
        {
            inscricao1 = escolhainscricao
        }
        else if (escolhainscricao == utilizadoresTemp.inscrito.toString())
        {
            inscricao1 = escolhainscricao
        }

        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()

            val utilizador = Utilizadores(
                utilizadoresTemp.codId,
                nome1,
                email1,
                password1,
                nin1,
                morada1,
                contacto1,
                genero1,
                data1,
                permissao1,
                idSeccao,
                utilizadoresTemp.imagem,
                nomegrupo1,
                inscricao1,
                datainscricao1)

            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                utilizador.toJson().toString()
            )
            Log.d("coudelaria", utilizador.toJson().toString())
            val request = Request.Builder()
                .url("http://173.212.215.122:5000/api/utilizador/${utilizadoresTemp.codId}")
                .put(requestBody)
                .build()
            client.newCall(request).execute().use { response ->
                Log.d("coudelaria", response.message)
            }

        }

        }

        buttonEliminar.setOnClickListener()
        {
            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/utilizador/${utilizadoresTemp.codId}")
                    .delete()
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                    GlobalScope.launch (Dispatchers.Main){
                        if (response.message == "OK"){
                            Toast.makeText(activity, "Utilizador Apagado com sucesso! ", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }
}