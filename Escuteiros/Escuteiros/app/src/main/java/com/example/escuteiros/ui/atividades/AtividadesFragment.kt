package com.example.escuteiros.ui.atividades

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.escuteiros.Atualizar
import com.example.escuteiros.GestaoPresencas
import com.example.escuteiros.Models.Atividades
import com.example.escuteiros.Models.Calendario
import com.example.escuteiros.Models.Data
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import com.example.escuteiros.ui.calendario.calendarios
import com.example.escuteiros.ui.home.Dados
import com.example.escuteiros.ui.utilizadores.UtilizadoresFragmentDirections
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class AtividadesFragment : Fragment() {

    var atividades : MutableList<Atividades> = arrayListOf()
    var listView : ListView? = null
    lateinit var adapter : UtilizadoresAdapter

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
        val rootView = inflater.inflate(R.layout.atividades_fragment, container, false)
        listView = rootView.findViewById<ListView>(R.id.listviewatividades)
        adapter = UtilizadoresAdapter()
        listView?.adapter = adapter
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/atividades").build()
        client.newCall(request).execute().use { response ->

            val jsStr : String = response.body!!.string()
            println(jsStr)
            atividades.clear()
            val jsonArrayAtividades = JSONArray(jsStr)

            for ( index in  0 until jsonArrayAtividades.length()) {
                val jsonArticle : JSONObject = jsonArrayAtividades.get(index) as JSONObject
                val atividade = Atividades.fromJson(jsonArticle)
                atividades.add(atividade)
            }
        }

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId){
            R.id.itemAdd -> {
                val action = AtividadesFragmentDirections.actionNavAtividadesToNavAtividadesAdicionar()
                this.view?.findNavController()?.navigate(action)
                return true
            }
        }

        return false
    }

    inner class UtilizadoresAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return atividades.size
        }

        override fun getItem(position: Int): Any {
            return atividades[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_atividade, parent, false)

            val textNomeAtividade = rowView.findViewById<TextView>(R.id.textNomeAtividade)
            val textDataAtividade = rowView.findViewById<TextView>(R.id.textDataAtividade)
            val imagem = rowView.findViewById<ImageView>(R.id.listImage)

            if (atividades[position].tipo_atividade == "Raid")
            {
                imagem.setImageResource(R.mipmap.ic_caminhada_foreground)
            }
            else if (atividades[position].tipo_atividade == "Missa")
            {
                imagem.setImageResource(R.mipmap.ic_missa_foreground)
            }
            else if (atividades[position].tipo_atividade == "Acampamento")
            {
                imagem.setImageResource(R.mipmap.ic_acampamento_foreground)
            }
            else if (atividades[position].tipo_atividade == "Jantares")
            {
                imagem.setImageResource(R.mipmap.ic_jantar_foreground)
            }
            else if (atividades[position].tipo_atividade == "Caminhada")
            {
                imagem.setImageResource(R.mipmap.ic_caminhada1_foreground)
            }
            else{
                imagem.setImageResource(R.mipmap.ic_outro_foreground)
            }

            textNomeAtividade.text =  atividades[position].nome_atividade
            textDataAtividade.text =  atividades[position].data_inicio + " até " + atividades[position].data_fim

            rowView.setOnClickListener {
                val action = AtividadesFragmentDirections.actionNavAtividadesToNavAtividadesVer(atividades[position].toJson().toString())
                rowView.findNavController().navigate(action)

            }

            return rowView
        }
    }

}