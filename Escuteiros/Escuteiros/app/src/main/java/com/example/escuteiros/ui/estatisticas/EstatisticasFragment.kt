package com.example.escuteiros.ui.estatisticas

import android.R.attr.x
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat


class EstatisticasFragment : Fragment() {

    lateinit var descricao1 :TextView
    lateinit var descricao2 :TextView
    lateinit var descricao3 :TextView
    lateinit var descricao4 :TextView

    var utilizadores : MutableList<Utilizadores> = arrayListOf()

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


        val menuItem3: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem3.isVisible = false

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.estatisticas_fragment, container, false)
         descricao1 = rootView.findViewById<TextView>(R.id.descricao1)
         descricao2 = rootView.findViewById<TextView>(R.id.descricao2)
         descricao3 = rootView.findViewById<TextView>(R.id.descricao3)
         descricao4 = rootView.findViewById<TextView>(R.id.descricao4)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val request = Request.Builder().url("http://173.212.215.122:5000/api/utilizador").build()
        var numero = 0
        val client = OkHttpClient()
        client.newCall(request).execute().use { response ->
            val jsStr : String = response.body!!.string()
            println(jsStr)
            val jsonArrayUtilizadores = JSONArray(jsStr)
            numero = jsonArrayUtilizadores.length()
            for ( index in  0 until jsonArrayUtilizadores.length()) {
                val jsonArticle : JSONObject = jsonArrayUtilizadores.get(index) as JSONObject
                val utlizador = Utilizadores.fromJson(jsonArticle)
                utilizadores.add(utlizador)
            }
        }
        var contadorLobitos = 0
        var contadorExploradores = 0
        var contadorPioneiros = 0
        var contadorCaminheiros = 0


        for (index in 0 until numero){
            if (utilizadores[index].id_secção == 1)
            {
                contadorLobitos++
            }
            else if (utilizadores[index].id_secção == 2)
            {
                contadorExploradores++
            }
            else if (utilizadores[index].id_secção == 3)
            {
                contadorPioneiros++
            }
            else if (utilizadores[index].id_secção == 4)
            {
                contadorCaminheiros++
            }
        }

        println(numero)

        var percentagemLobitos : Float
        var percentagemFinalLobitos : Float
        var percentagemExploradores : Float
        var percentagemFinalExploradores : Float
        var percentagemPioneiros : Float
        var percentagemFinalPioneiros : Float
        var percentagemCaminheiros : Float
        var percentagemFinalCaminheiros : Float

        percentagemLobitos = contadorLobitos / numero.toFloat()
        percentagemFinalLobitos = percentagemLobitos * 100
        percentagemExploradores = contadorExploradores / numero.toFloat()
        percentagemFinalExploradores = percentagemExploradores * 100
        percentagemPioneiros = contadorPioneiros / numero.toFloat()
        percentagemFinalPioneiros = percentagemPioneiros * 100
        percentagemCaminheiros = contadorCaminheiros / numero.toFloat()
        percentagemFinalCaminheiros = percentagemCaminheiros * 100

        println(percentagemLobitos)
        val df = DecimalFormat("0.0")
        df.format(x)

        descricao1.text = df.format(percentagemFinalLobitos).toString() + "%"
        descricao2.text = df.format(percentagemFinalExploradores).toString() + "%"
        descricao3.text = df.format(percentagemFinalPioneiros).toString() + "%"
        descricao4.text = df.format(percentagemFinalCaminheiros).toString() + "%"


    }

}