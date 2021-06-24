package com.example.escuteiros.ui.inventario

import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.escuteiros.Models.Inventario
import com.example.escuteiros.Models.Item
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


class AdicionarItemInventarioFragment : Fragment() {

    private var  InventarioJsonStr: String? = null


    var itensarray : MutableList<Item> = arrayListOf()
    var inventarioarray : MutableList<Inventario> = arrayListOf()

    var itensnomes : Array<String> = Array(N) {""}

    lateinit var  itens : Spinner
    lateinit var  guardar : Button
    lateinit var  eliminar : Button
    lateinit var textViewNomeInventario : TextView

    var escolhaitem : String = ""
    var escolhainventario : String =""
    var y  = 0

    var listView : ListView? = null
    lateinit var adapter : ItensInventarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            InventarioJsonStr = it.getString("inventarios")
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
        val rootView = inflater.inflate(R.layout.adicionar_item_inventario_fragment, container, false)
        listView = rootView.findViewById<ListView>(R.id.listitens)
        adapter = ItensInventarioAdapter()
        listView?.adapter = adapter
        itens = rootView.findViewById(R.id.spinner2)
        textViewNomeInventario = rootView.findViewById(R.id.textViewNomeInventario)
        guardar = rootView.findViewById(R.id.button3)
        eliminar = rootView.findViewById(R.id.button6)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val inventariosTemp = Inventario.fromJson(JSONObject(InventarioJsonStr))
        textViewNomeInventario.text = inventariosTemp.nome_inventario
        var i = 0
        val request = Request.Builder().url("http://173.212.215.122:5000/api/item").build()
        val client = OkHttpClient()
        var contador = 0
        client.newCall(request).execute().use { response ->
            val jsStr: String = response.body!!.string()
            println(jsStr)
            val jsonArrayItem = JSONArray(jsStr)

            for (index in 0 until jsonArrayItem.length()) {
                val jsonArticle: JSONObject = jsonArrayItem.get(index) as JSONObject
                val item = Item.fromJson(jsonArticle)
                itensarray.add(item)
                contador++
                if(itensarray[index].id_inventario == inventariosTemp.id_inventario)
                {
                    itensnomes[i] = itensarray[index].nome_item.toString()
                    i++
                }
            }
        }
        var iditens =  Array(contador) { 0 }
        var nomeitens : Array<String> = Array(contador) {""}
        for (index in 0 until contador) {
            nomeitens[index] = itensarray[index].nome_item.toString()
            iditens[index] = itensarray[index].id_item!!
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
            for (index in 0 until contador){
                if(escolhaitem.equals(itensarray[index].nome_item.toString()))
                {
                    y = index
                }
            }

            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val itens = Item(
                    iditens[y],
                    itensarray[y].descricao_item,
                    nomeitens[y],
                    inventariosTemp.id_inventario
                )
                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    itens.toJson().toString()
                )
                Log.d("coudelaria", itens.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/item/${iditens[y]}")
                    .put(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                }
            }
        }

        eliminar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/inventario/${inventariosTemp.id_inventario}")
                    .delete()
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                    GlobalScope.launch (Dispatchers.Main){
                        if (response.message == "OK"){
                            Toast.makeText(activity, "Inventario Apagado com sucesso! ", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

    }
    inner class ItensInventarioAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return itensnomes.size
        }

        override fun getItem(position: Int): Any {
            return itensnomes[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_itens_inventarios, parent, false)
            val imagem = rowView.findViewById<ImageView>(R.id.imagem)
            val textViewNomeItem = rowView.findViewById<TextView>(R.id.textViewNomeItem)


            imagem.setImageResource(R.mipmap.ic_itensinventarios_foreground)
            textViewNomeItem.text =  itensnomes[position]

            return rowView
        }
    }
}