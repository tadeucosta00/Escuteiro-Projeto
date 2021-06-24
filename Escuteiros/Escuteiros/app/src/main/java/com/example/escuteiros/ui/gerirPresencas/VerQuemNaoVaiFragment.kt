package com.example.escuteiros.ui.gerirPresencas

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.escuteiros.Models.Presenca
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class VerQuemNaoVaiFragment : Fragment() {

    var escuteiros : MutableList<Utilizadores> = arrayListOf()
    var presencas : MutableList<Presenca> = arrayListOf()

    var idatividade = Atividadesid[0].idAtividade
    var contadorPresencas = 0
    var id_utilizadores : Array<Int> = Array(10) {0}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var numeroPresencas = 0
        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/presencas").build()
        client.newCall(request).execute().use { response ->

            val jsStr : String = response.body!!.string()
            println(jsStr)
            escuteiros.clear()
            val jsonArrayPresencas = JSONArray(jsStr)
            numeroPresencas = jsonArrayPresencas.length()
            for ( index in  0 until jsonArrayPresencas.length())
            {
                val jsonArticle : JSONObject = jsonArrayPresencas.get(index) as JSONObject
                val presenca = Presenca.fromJson(jsonArticle)
                presencas.add(presenca)
            }
        }
        for(index in 0 until numeroPresencas)
        {
            if(idatividade == presencas[index].id_atividade && presencas[index].presenca.equals("Não Presente"))
            {
                id_utilizadores[contadorPresencas] = presencas[index].id_utilizador!!
                contadorPresencas++
            }
        }
        println("fdsfdsfdsf"+contadorPresencas)
    }

    var nomes : Array<String> = Array(Build.VERSION_CODES.N) {""}

    var listView : ListView? = null
    lateinit var adapter : PresenteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.ver_quem_nao_vai_fragment, container, false)
        listView = rootView.findViewById<ListView>(R.id.listQuemNaoVai)
        adapter = PresenteAdapter()
        listView?.adapter = adapter
        setHasOptionsMenu(true)
        return rootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var numeroUtilizadores = 0
        var i = 0
        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/utilizador").build()
        client.newCall(request).execute().use { response ->

            val jsStr : String = response.body!!.string()
            println(jsStr)
            escuteiros.clear()
            val jsonArrayUtilizadores = JSONArray(jsStr)
            numeroUtilizadores = jsonArrayUtilizadores.length()
            for ( index in  0 until jsonArrayUtilizadores.length()) {
                val jsonArticle : JSONObject = jsonArrayUtilizadores.get(index) as JSONObject
                val utilizador = Utilizadores.fromJson(jsonArticle)
                escuteiros.add(utilizador)
            }
        }
        for(index in 0 until numeroUtilizadores)
        {
            if(id_utilizadores[i] == escuteiros[index].codId){
                nomes[i] = escuteiros[index].nomeUtilizador.toString()
                i++
            }
        }
    }

    inner class PresenteAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return nomes.size
        }

        override fun getItem(position: Int): Any {
            return nomes[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_naopresencas, parent, false)

            val textViewNomeUtilizador = rowView.findViewById<TextView>(R.id.textViewNomeUtilizador)

            textViewNomeUtilizador.text =  nomes[position]

            return rowView
        }
    }

}