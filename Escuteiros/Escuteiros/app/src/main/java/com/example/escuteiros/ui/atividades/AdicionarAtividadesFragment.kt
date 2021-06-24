package com.example.escuteiros.ui.atividades

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.escuteiros.Models.Atividades
import com.example.escuteiros.Models.Calendario
import com.example.escuteiros.Models.Data
import com.example.escuteiros.Models.Inventario
import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AdicionarAtividadesFragment : Fragment() {

    var inventarioarray : MutableList<Inventario> = arrayListOf()

    lateinit var  nomeatividade : EditText
    lateinit var  descricacao : EditText
    lateinit var  preco : EditText
    lateinit var  datainicio : DatePicker
    lateinit var  datafim : DatePicker
    lateinit var  localizacaoinical : EditText
    lateinit var  localizacaofinal : EditText
    lateinit var  adicionar : Button

    lateinit var  tipoatividades : Spinner
    lateinit var  inventarios : Spinner

    var tipoAtividade = arrayOf("Raid","Caminhada","Acampamento","Missa","Jantares","Outros")
    var escolhaAtividade = ""

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
        val rootView = inflater.inflate(R.layout.adicionar_atividades_fragment, container, false)

        nomeatividade       = rootView.findViewById(R.id.nomeAtividade)
        descricacao         = rootView.findViewById(R.id.descicao)
        preco               = rootView.findViewById(R.id.custo)
        datainicio          = rootView.findViewById(R.id.date_PickerInicial)
        datafim             = rootView.findViewById(R.id.date_PickerFinal)
        localizacaoinical   = rootView.findViewById(R.id.local_inicio)
        localizacaofinal    = rootView.findViewById(R.id.local_fim)

        tipoatividades         = rootView.findViewById(R.id.tipo_atividade)
        inventarios         = rootView.findViewById(R.id.spinnerinventarios)
        adicionar           = rootView.findViewById(R.id.adicionar_atividade)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val dataAgora = sdf.format(Date())

        var escolhainventario : String = ""
        var contadorInventario = 0
        val request = Request.Builder().url("http://173.212.215.122:5000/api/inventario").build()
        val client = OkHttpClient()
        client.newCall(request).execute().use { response ->
            val jsStr: String = response.body!!.string()
            println(jsStr)
            val jsonArrayInventario = JSONArray(jsStr)

            for (index in 0 until jsonArrayInventario.length()) {
                val jsonArticle: JSONObject = jsonArrayInventario.get(index) as JSONObject
                val inventarios = Inventario.fromJson(jsonArticle)
                inventarioarray.add(inventarios)
                contadorInventario++
            }
        }

        var idInventario =  Array(contadorInventario) { 0 }
        var nomeInventario : Array<String> = Array(contadorInventario) {""}
        for (index in 0 until contadorInventario) {
            nomeInventario[index] = inventarioarray[index].nome_inventario.toString()
            idInventario[index] = inventarioarray[index].id_inventario!!
        }

        val adapter = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, nomeInventario)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        inventarios.adapter = adapter


        var y = 0
        for (index in 0 until contadorInventario){
            if(escolhainventario.equals(inventarioarray[index].nome_inventario.toString()))
            {
                y = index
            }
        }

        var dataInicio  = dataAgora
        var dataFinal   = dataAgora

        inventarios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhainventario = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val adapter2 = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, tipoAtividade)
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        tipoatividades.adapter = adapter2

        tipoatividades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhaAtividade = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        val today = Calendar.getInstance()
        datainicio.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            dataInicio = "$day-$month-$year".toString()
        }
        datafim.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            dataFinal = "$day-$month-$year".toString()
        }

        adicionar.setOnClickListener(){

            var nome : String = nomeatividade.text.toString()
            var descricacao1 : String = descricacao.text.toString()
            var preco1 : String = preco.text.toString()
            var localizacaoinical1 : String = localizacaoinical.text.toString()
            var localizacaofinal1 : String = localizacaofinal.text.toString()

            if (nome.isNullOrEmpty() || descricacao1.isNullOrEmpty() || dataInicio.isNullOrEmpty()
                || dataFinal.isNullOrEmpty() || preco1.isNullOrEmpty() || localizacaoinical1.isNullOrEmpty() || localizacaofinal1.isNullOrEmpty() || escolhainventario.isNullOrEmpty()||escolhaAtividade.isNullOrEmpty()) {
                Toast.makeText(activity, "Por favor preencha todos os campos!!" , Toast.LENGTH_SHORT).show()
            }
            else
            {
                GlobalScope.launch(Dispatchers.IO) {
                    val client = OkHttpClient()
                    val atividades = Atividades(
                        null,
                        2,
                        inventarioarray[y].id_inventario,
                        2,
                        Dados[0].id,
                        nomeatividade.text.toString(),
                        escolhaAtividade,
                        descricacao.text.toString(),
                        "null",
                        dataInicio,
                        dataFinal,
                        preco.text.toString().toInt(),
                        localizacaoinical.text.toString(),
                        localizacaofinal.text.toString()
                        )

                    val requestBody = RequestBody.create(
                        "application/json".toMediaTypeOrNull(),
                        atividades.toJson().toString()
                    )
                    Log.d("coudelaria", atividades.toJson().toString())
                    val request = Request.Builder()
                        .url("http://173.212.215.122:5000/api/atividades")
                        .post(requestBody)
                        .build()
                    client.newCall(request).execute().use { response ->
                        Log.d("coudelaria", response.message)
                        GlobalScope.launch (Dispatchers.Main){
                            if (response.message == "OK"){
                                Toast.makeText(activity, "Atividade criada com sucesso!!", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                        }
                    }
                }

            }
        }
    }

}