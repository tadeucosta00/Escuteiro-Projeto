package com.example.escuteiros.ui.catalgo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.escuteiros.Models.Catalgo
import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class CatalgoFragment : Fragment() {


    var catalogos : MutableList<Catalgo> = arrayListOf()
    var listView : ListView? = null
    lateinit var adapter : CatalgoAdapter

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
        val rootView = inflater.inflate(R.layout.catalgo_fragment, container, false)
        listView = rootView.findViewById<ListView>(R.id.listacatalogo)
        adapter = CatalgoAdapter()
        listView?.adapter = adapter
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/catalogo").build()
        client.newCall(request).execute().use { response ->

            val jsStr : String = response.body!!.string()
            println(jsStr)
            catalogos.clear()
            val jsonArrayCatalogos = JSONArray(jsStr)

            for ( index in  0 until jsonArrayCatalogos.length()) {
                val jsonArticle : JSONObject = jsonArrayCatalogos.get(index) as JSONObject
                val catalogo = Catalgo.fromJson(jsonArticle)
                catalogos.add(catalogo)
            }

        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId){
            R.id.itemAdd -> {
                val action = CatalgoFragmentDirections.actionNavCatalgoToNavAdicionarCatalogo()
                this.view?.findNavController()?.navigate(action)
                return true
            }
        }
        return false
    }

    inner class CatalgoAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return catalogos.size
        }

        override fun getItem(position: Int): Any {
            return catalogos[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.rowcatalogo, parent, false)

            val textViewNomeItem = rowView.findViewById<TextView>(R.id.textViewNomeCatalogo)

            textViewNomeItem.text =  catalogos[position].nome_catalogo

            rowView.setOnClickListener {
                val action = CatalgoFragmentDirections.actionNavCatalgoToNavVerCatalogo(catalogos[position].toJson().toString())
                rowView.findNavController().navigate(action)
            }

            return rowView
        }
    }

}