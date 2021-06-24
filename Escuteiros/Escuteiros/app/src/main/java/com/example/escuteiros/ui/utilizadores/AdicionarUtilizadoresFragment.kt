package com.example.escuteiros.ui.utilizadores

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.escuteiros.Models.Seccao
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class AdicionarUtilizadoresFragment : Fragment() {

    var seccoes : MutableList<Seccao> = arrayListOf()

    lateinit var  nome     : EditText
    lateinit var  email    : EditText
    lateinit var  password : EditText
    lateinit var  nin      : EditText
    lateinit var  morada   : EditText
    lateinit var  data     : DatePicker
    lateinit var  contacto : EditText
    lateinit var  grupo    : EditText
    lateinit var  datainscricao : DatePicker

    lateinit var  seccaoescolha : Spinner
    lateinit var  permissaoescolha : Spinner
    lateinit var  generoescolha : Spinner
    lateinit var  inscricaoescolha : Spinner

    lateinit var buttonSave : Button

    var escolhaseccao : String = ""
    var escolhapermissao : String = ""
    var escolhainscricao : String = ""
    var nomeSeccoes = arrayOf<String>("","","","")
    var permissoes = arrayOf<String>("Escuteiro","Dirigente","Administrador")
    var escolhagenero : String = ""
    val generos = arrayOf("Masculino","Feminino")
    val inscricao = arrayOf("Inscrito","Não Inscrito")
    var idSeccao : Int = 0
    var datanascimento : String = ""
    var datainscricao1 : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val rootView = inflater.inflate(R.layout.adicionar_utilizadores_fragment, container, false)
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
        grupo = rootView.findViewById(R.id.Grupo)
        datainscricao = rootView.findViewById(R.id.data_inscricao)
        buttonSave = rootView.findViewById(R.id.criar_button)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val today = Calendar.getInstance()
        data.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            datanascimento = "$day-$month-$year".toString()
        }

        val today1 = Calendar.getInstance()
        data.init(today1.get(Calendar.YEAR), today1.get(Calendar.MONTH),
            today1.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            datainscricao1 = "$day-$month-$year".toString()
        }

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
        //spinnerpermissoes
        var permissao : Int = 1
        val adapter2 = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, permissoes)
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        permissaoescolha.adapter = adapter2
        permissaoescolha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhapermissao = parent.getItemAtPosition(position).toString()
                if (escolhapermissao.trim() == "Escuteiro")
                {
                    permissao = 1
                }
                else if (escolhapermissao.trim() == "Dirigente")
                {
                    permissao = 2
                }
                else if (escolhapermissao.trim() == "Administrador")
                {
                    permissao = 3
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
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

        buttonSave.setOnClickListener()
        {
                val client = OkHttpClient()
                val utilizador = Utilizadores(
                    null,
                    nome.text.toString(),
                    email.text.toString(),
                    password.text.toString() ,
                    nin.text.toString().toInt() ,
                    morada.text.toString() ,
                    contacto.text.toString().toInt(),
                    escolhagenero ,
                    datanascimento,
                    permissao,
                    idSeccao,
                    "null",
                    grupo.text.toString(),
                    escolhainscricao,
                    datainscricao1
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
        }
    }
}
