package com.example.escuteiros.ui.inventario

import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.escuteiros.Models.Inventario
import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


class InventarioFragment : Fragment() {

    var inventarios : MutableList<Inventario> = arrayListOf()
    var listView : ListView? = null
    lateinit var adapter : InventariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        var permissao : Int = 0

        val menuItem1: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem1.isVisible = false

        val menuItem2: MenuItem = menu.findItem(R.id.itemVerPresenca)
        menuItem2.isVisible = false

        permissao = Dados[0].permissao
        if (permissao == 1)
        {
            val menuItem: MenuItem = menu.findItem(R.id.itemAdd)
            menuItem.isVisible = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.inventario_fragment, container, false)
        listView = rootView.findViewById<ListView>(R.id.listviewInventarios)
        adapter = InventariosAdapter()
        listView?.adapter = adapter
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/inventario").build()
        client.newCall(request).execute().use { response ->
            val jsStr : String = response.body!!.string()
            println(jsStr)
            inventarios.clear()
            val jsonArrayInventario = JSONArray(jsStr)
            for ( index in  0 until jsonArrayInventario.length()) {
                val jsonArticle : JSONObject = jsonArrayInventario.get(index) as JSONObject
                val inventario = Inventario.fromJson(jsonArticle)
                inventarios.add(inventario)
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId){
            R.id.itemAdd -> {
                val action = InventarioFragmentDirections.actionNavInventarioToNavInventarioAdicionar()
                this.view?.findNavController()?.navigate(action)
                return true
            }
        }
        return false
    }

    inner class InventariosAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return inventarios.size
        }

        override fun getItem(position: Int): Any {
            return inventarios[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_inventario, parent, false)

            val textViewNomeInventario = rowView.findViewById<TextView>(R.id.textViewNomeInventario)
            val imagem = rowView.findViewById<ImageView>(R.id.listImage)
            textViewNomeInventario.text = "Nome: " + inventarios[position].nome_inventario.toString()
            imagem.setImageResource(R.mipmap.inventarioescuteiro_foreground)
            rowView.setOnClickListener {
                val action = InventarioFragmentDirections.actionNavInventarioToNavInventarioAdicionarItem(inventarios[position].toJson().toString())
                rowView.findNavController().navigate(action)
            }

            return rowView
        }
    }
}