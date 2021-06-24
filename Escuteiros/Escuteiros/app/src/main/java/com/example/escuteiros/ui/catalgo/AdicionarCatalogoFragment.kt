package com.example.escuteiros.ui.catalgo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.escuteiros.Models.Catalgo
import com.example.escuteiros.Models.Item
import com.example.escuteiros.Models.Seccao
import com.example.escuteiros.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

class AdicionarCatalogoFragment : Fragment() {

    var itensarray : MutableList<Item> = arrayListOf()
    var catalogosarray : MutableList<Catalgo> = arrayListOf()

    lateinit var  nome : EditText
    lateinit var  itens : Spinner
    lateinit var  instruncoes : EditText
    lateinit var  guardar : Button
    lateinit var  imagem : ImageView
    var escolhaitem : String = ""
    var iditem : Int = 0
    var contadorcatalogos = 0


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
        val rootView = inflater.inflate(R.layout.adicionar_catalogo_fragment, container, false)
        nome = rootView.findViewById(R.id.titulo)
        itens = rootView.findViewById(R.id.spinner)
        instruncoes = rootView.findViewById(R.id.Descricao)
        guardar = rootView.findViewById(R.id.Adicionar)
        imagem = rootView.findViewById(R.id.imageView2)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val request = Request.Builder().url("http://173.212.215.122:5000/api/item").build()
        val client = OkHttpClient()
        var y  = 1
        var i  = 0

            imagem.setImageResource(R.mipmap.ic_catalogo_list_foreground)

        client.newCall(request).execute().use { response ->
            val jsStr: String = response.body!!.string()
            println(jsStr)
            val jsonArrayItem = JSONArray(jsStr)

            for (index in 0 until jsonArrayItem.length()) {
                val jsonArticle: JSONObject = jsonArrayItem.get(index) as JSONObject
                val item = Item.fromJson(jsonArticle)
                itensarray.add(item)
                i++
            }
        }

        var nomeitens : Array<String> = Array(i) {""}
        var iditens =  Array(i) { 0 }

        for (index in 0 until i) {
            nomeitens[index] = itensarray[index].nome_item.toString()
            iditens[index] = itensarray[index].id_item.toString().toInt()
        }

        val adapter = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_spinner_item, nomeitens)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        itens.adapter = adapter
        itens.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                escolhaitem = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        guardar.setOnClickListener()
        {
            verificacatalogos()
            println(contadorcatalogos)
            var verficador : Boolean = false
            for (index in 0 until i) {
                if(escolhaitem.equals(itensarray[index].nome_item.toString()))
                {
                    y = index
                }
            }

            for (index in 0 until contadorcatalogos) {
                if(catalogosarray[index].id_item == itensarray[y].id_item)
                {
                    verficador = true
                }
            }

            if(verficador == false){
                GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val catalgo = Catalgo(
                    null,
                    itensarray[y].id_item,
                    instruncoes.text.toString(),
                    nome.text.toString()
                )

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    catalgo.toJson().toString()
                )
                Log.d("coudelaria", catalgo.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/catalogo")
                    .post(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                }
             }
            }
            else
            {
                Toast.makeText(getActivity(),"Ja existe um Catalogo para esse artigo!",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun verificacatalogos()
    {
        val request = Request.Builder().url("http://173.212.215.122:5000/api/catalogo").build()
        val client = OkHttpClient()

        client.newCall(request).execute().use { response ->
            val jsStr: String = response.body!!.string()
            println(jsStr)
            val jsonArrayCatalogo = JSONArray(jsStr)

            for (index in 0 until jsonArrayCatalogo.length()) {
                val jsonArticle: JSONObject = jsonArrayCatalogo.get(index) as JSONObject
                val catalgo = Catalgo.fromJson(jsonArticle)
                catalogosarray.add(catalgo)
                contadorcatalogos++

            }
        }
    }
}