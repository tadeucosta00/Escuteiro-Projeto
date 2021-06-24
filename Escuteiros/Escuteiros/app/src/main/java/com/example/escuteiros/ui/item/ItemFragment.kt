package com.example.escuteiros.ui.item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.escuteiros.Models.Item
import com.example.escuteiros.R
import com.example.escuteiros.ui.catalgo.CatalgoFragmentDirections
import com.example.escuteiros.ui.home.Dados
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class ItemFragment : Fragment() {

    var itens : MutableList<Item> = arrayListOf()
    var listView : ListView? = null
    lateinit var adapter : ItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        var permissao : Int = 0
        val menuItem: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem.isVisible = false

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
        val rootView = inflater.inflate(R.layout.item_fragment, container, false)
        listView = rootView.findViewById<ListView>(R.id.listviewItem)
        adapter = ItemAdapter()
        listView?.adapter = adapter
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var contador  = 0
        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/item").build()
        client.newCall(request).execute().use { response ->

            val jsStr : String = response.body!!.string()
            println(jsStr)
            itens.clear()
            val jsonArrayItens = JSONArray(jsStr)

            for ( index in  0 until jsonArrayItens.length()) {
                val jsonArticle : JSONObject = jsonArrayItens.get(index) as JSONObject
                val item = Item.fromJson(jsonArticle)
                itens.add(item)
            }
        }


    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId){
            R.id.itemAdd -> {
                val action = ItemFragmentDirections.actionNavItemToNavItemAdicionar()
                this.view?.findNavController()?.navigate(action)
                return true
            }
        }

        return false
    }

    inner class ItemAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return itens.size
        }

        override fun getItem(position: Int): Any {
            return itens[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_itens, parent, false)

            val textViewNomeItem = rowView.findViewById<TextView>(R.id.textViewNomeItem)
            val textVieDescricao = rowView.findViewById<TextView>(R.id.textViewdescricao)

            textViewNomeItem.text = "Nome: " + itens[position].nome_item
            textVieDescricao.text = "Descrição: " + itens[position].descricao_item

            rowView.setOnClickListener {
                val action = ItemFragmentDirections.actionNavItemToNavItemEditar(itens[position].toJson().toString())
                rowView.findNavController().navigate(action)
            }

            return rowView
        }
    }

}