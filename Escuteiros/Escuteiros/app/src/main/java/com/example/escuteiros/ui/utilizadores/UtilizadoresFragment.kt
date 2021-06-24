package com.example.escuteiros.ui.utilizadores

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import com.mancj.materialsearchbar.MaterialSearchBar
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class UtilizadoresFragment : Fragment() {

    var utilizadores : MutableList<Utilizadores> = arrayListOf()
    var listView : ListView? = null
    lateinit var adapter : UtilizadoresAdapter
    lateinit var plainProcura : SearchView

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
        val rootView = inflater.inflate(R.layout.utilizadores_fragment, container, false)
        listView = rootView.findViewById<ListView>(R.id.listviewutilizadores)

        adapter = UtilizadoresAdapter()
        listView?.adapter = adapter
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

            val client = OkHttpClient()
            val request = Request.Builder().url("http://173.212.215.122:5000/api/utilizador").build()
            client.newCall(request).execute().use { response ->

                val jsStr : String = response.body!!.string()
                println(jsStr)
                utilizadores.clear()
                val jsonArrayUtilizadores = JSONArray(jsStr)

                for ( index in  0 until jsonArrayUtilizadores.length()) {
                    val jsonArticle : JSONObject = jsonArrayUtilizadores.get(index) as JSONObject
                    val utilizador = Utilizadores.fromJson(jsonArticle)
                    utilizadores.add(utilizador)
                }
            }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId){
            R.id.itemAdd -> {
                val action = UtilizadoresFragmentDirections.actionNavUtilizadoresToNavAdicionarUtilizadores()
                this.view?.findNavController()?.navigate(action)
                return true
            }
        }

        return false
    }

    inner class UtilizadoresAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return utilizadores.size
        }

        override fun getItem(position: Int): Any {
            return utilizadores[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.rowutilizadores, parent, false)

            val textViewNomeUtilizador = rowView.findViewById<TextView>(R.id.textViewNomeUtilizador)
            val textViewNin = rowView.findViewById<TextView>(R.id.textViewNin)
            val imagem = rowView.findViewById<ImageView>(R.id.foto)


            if(utilizadores[position].imagem != "null") {
                Picasso.get().load(utilizadores[position].imagem ).resize(250, 250).into(imagem)
            }
            else
            {
                Picasso.get().load("https://i.imgur.com/V9CRLdo.png").resize(250, 250).into(imagem)
            }

            textViewNomeUtilizador.text = "Nome: " + utilizadores[position].nomeUtilizador
            textViewNin.text = "NIN: " + utilizadores[position].nin.toString()

            rowView.setOnClickListener {
                val action = UtilizadoresFragmentDirections.actionNavUtilizadoresToNavEditar(utilizadores[position].toJson().toString())
                rowView.findNavController().navigate(action)
            }


            return rowView
        }
    }

}