package com.example.escuteiros.ui.catalgo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.escuteiros.Models.Catalgo
import com.example.escuteiros.Models.Data
import com.example.escuteiros.Models.GuardarIdCatalogo
import com.example.escuteiros.Models.Item
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
var Editar : MutableList<GuardarIdCatalogo> = arrayListOf()
class EditarCatalogoFragment : Fragment() {


    lateinit var TextViewTitulo : EditText
    lateinit var TextViewInstruncoes : EditText
    lateinit var Guardar : Button
    lateinit var Apagar : Button

    var catalogos : MutableList<Catalgo> = arrayListOf()

    var nome1: String = ""
    var instruncoes1 : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val menuItem: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem.isVisible = false

        val menuItem2: MenuItem = menu.findItem(R.id.itemVerPresenca)
        menuItem2.isVisible = false

        val menuItem3: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem3.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.editar_catalogo_fragment, container, false)

        TextViewTitulo = rootView.findViewById(R.id.titulo)
        TextViewInstruncoes = rootView.findViewById(R.id.Descricao)
        Guardar = rootView.findViewById(R.id.button5)
        Apagar = rootView.findViewById(R.id.button6)
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var contador = 0
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
                contador++
            }
        }
        println(Editar[0].idCatalogo)
        var i: Int = 0

        if(Editar[0].idCatalogo == catalogos[0].id_catalogo)
        {
            i = 0
        }
        else{
        for(index in 1 until contador){
            if(Editar[0].idCatalogo == catalogos[index].id_catalogo)
            {
                i++
            }
        }
        }

        TextViewTitulo.setText(catalogos[i].nome_catalogo)
        TextViewInstruncoes.setText(catalogos[i].instrucoes)


        Guardar.setOnClickListener()
        {
            if(TextViewTitulo.text.toString() == ""){
                nome1 = catalogos[i].nome_catalogo.toString()
            }
            else
            {
                nome1 = TextViewTitulo.text.toString()
            }


            if(TextViewInstruncoes.text.toString() == ""){
                instruncoes1 = catalogos[i].instrucoes.toString()
            }
            else
            {
                instruncoes1 = TextViewInstruncoes.text.toString()
            }




            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()

                val catalogos = Catalgo(
                    catalogos[i].id_catalogo,
                    catalogos[i].id_item,
                    instruncoes1,
                    nome1)

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    catalogos.toJson().toString()
                )
                Log.d("coudelaria", catalogos.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/catalogo/${Editar[0].idCatalogo}")
                    .put(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                    GlobalScope.launch (Dispatchers.Main){
                        if (response.message == "OK"){
                            Toast.makeText(activity, "Catalogo editado com sucesso! ", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }

            }
        }
        Apagar.setOnClickListener(){
            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/catalogo/${Editar[0].idCatalogo}")
                    .delete()
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                    GlobalScope.launch (Dispatchers.Main){
                        if (response.message == "OK"){
                            Toast.makeText(activity, "Catalogo Apagado com sucesso! ", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

    }

}