package com.example.escuteiros.ui.atividades

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.escuteiros.GestaoPresencas
import com.example.escuteiros.Models.*
import com.example.escuteiros.R
import com.example.escuteiros.ui.gerirPresencas.Atividadesid
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

class VerAtividadeFragment : Fragment() {

    var Inventarios : MutableList<Inventario> = arrayListOf()
    var Presencas : MutableList<Presenca> = arrayListOf()

    private var  AtividadesJsonStr: String? = null

    lateinit var  nomeAtividade     : TextView
    lateinit var  tipo_atividade    : TextView
    lateinit var  descicao : TextView
    lateinit var  data_inicial      : TextView
    lateinit var  data_final   : TextView
    lateinit var  custo     : TextView
    lateinit var  local_inicio : TextView
    lateinit var  local_fim : TextView
    lateinit var  inventarios : TextView
    lateinit var  presencas : TextView
    lateinit var  descricao : TextView
    lateinit var  Ir : Button
    lateinit var  NaoIr : Button
    lateinit var  guardar : Button
    lateinit var  apagarAtividade : Button

    var presenca = ""
    var verficador : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            AtividadesJsonStr = it.getString("atividade")
        }
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem.isVisible = false
        val menuItem1: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem1.isVisible = false
        var permissao = 0
        permissao = Dados[0].permissao
        if (permissao == 1)
        {
            val menuItem3: MenuItem = menu.findItem(R.id.itemVerPresenca)
            menuItem3.isVisible = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.ver_atividade_fragment, container, false)
        nomeAtividade = rootView.findViewById(R.id.nomeAtividade)
        tipo_atividade = rootView.findViewById(R.id.tipo_atividade)
        descicao = rootView.findViewById(R.id.descicao)
        data_inicial = rootView.findViewById(R.id.data_inicial)
        data_final = rootView.findViewById(R.id.data_final)
        custo = rootView.findViewById(R.id.custo)
        local_inicio = rootView.findViewById(R.id.local_inicio)
        local_fim = rootView.findViewById(R.id.local_fim)
        inventarios = rootView.findViewById(R.id.inventarios)
        descricao = rootView.findViewById(R.id.descricao)
        presencas = rootView.findViewById(R.id.presenca)

        Ir = rootView.findViewById(R.id.Ir)
        NaoIr = rootView.findViewById(R.id.NaoIr)
        guardar = rootView.findViewById(R.id.save)
        apagarAtividade = rootView.findViewById(R.id.apagarAtividade)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val atividadesTemp = Atividades.fromJson(JSONObject(AtividadesJsonStr))
        verficasejarespondeu()

        if(Dados[0].permissao == 1){
            apagarAtividade.visibility = View.GONE
        }

        if (verficador == true){
            Ir.visibility = View.GONE
            NaoIr.visibility = View.GONE
            guardar.visibility = View.GONE
            descricao.visibility = View.GONE
            presencas.visibility = View.GONE
        }

        nomeAtividade.text = atividadesTemp.nome_atividade
        tipo_atividade.text = atividadesTemp.tipo_atividade
        descicao.text = atividadesTemp.descricao
        data_inicial.text = atividadesTemp.data_inicio
        data_final.text = atividadesTemp.data_fim
        custo.text = atividadesTemp.custo.toString() + "€"
        local_inicio.text = atividadesTemp.local_inicio
        local_fim.text = atividadesTemp.local_fim

        Atividadesid.clear()
        Atividadesid.add(GuardarAtividade(atividadesTemp.id_atividade.toString().toInt()))

        var nomeInventario = ""
        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/inventario").build()
        client.newCall(request).execute().use { response ->
            val jsStr : String = response.body!!.string()
            println(jsStr)
            Inventarios.clear()
            val jsonArrayInventario = JSONArray(jsStr)
            for ( index in  0 until jsonArrayInventario.length()) {
                val jsonArticle : JSONObject = jsonArrayInventario.get(index) as JSONObject
                val inventario = Inventario.fromJson(jsonArticle)
                Inventarios.add(inventario)
                if (Inventarios[index].id_inventario == atividadesTemp.id_inventario){
                    nomeInventario = Inventarios[index].nome_inventario.toString()
                }
            }
        }
        inventarios.text = nomeInventario

        Ir.setOnClickListener(){
            presenca = "Presente"
        }
        NaoIr.setOnClickListener(){
            presenca = "Não Presente"
        }

        apagarAtividade.setOnClickListener(){
            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/atividades/${atividadesTemp.id_atividade}")
                    .delete()
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                    GlobalScope.launch (Dispatchers.Main){
                        if (response.message == "OK")
                        {
                            Toast.makeText(activity, "Atividade Apagada com sucesso! ", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

        guardar.setOnClickListener(){
            if(presenca == "")
            {
            Toast.makeText(activity, "Por favor responda á presença!", Toast.LENGTH_SHORT).show()
            }
            else {
                GlobalScope.launch(Dispatchers.IO)
                {
                    val client = OkHttpClient()
                    val presenca = Presenca(
                        null,
                        Dados[0].id,
                        presenca,
                        atividadesTemp.id_atividade
                    )

                    val requestBody = RequestBody.create(
                        "application/json".toMediaTypeOrNull(),
                        presenca.toJson().toString()
                    )
                    Log.d("coudelaria", presenca.toJson().toString())
                    val request = Request.Builder()
                        .url("http://173.212.215.122:5000/api/presencas")
                        .post(requestBody)
                        .build()
                    client.newCall(request).execute().use { response ->
                        Log.d("coudelaria", response.message)
                    }
                }
                Toast.makeText(activity, "Resposta Guardada com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId){
            R.id.itemVerPresenca -> {
                activity?.let{
                    val intent = Intent (it, GestaoPresencas::class.java)
                    it.startActivity(intent)
                }
                return true
            }
        }

        return false
    }

    fun verficasejarespondeu(){
        var contador = 0
        val atividadesTemp = Atividades.fromJson(JSONObject(AtividadesJsonStr))
        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/presencas").build()
        client.newCall(request).execute().use { response ->
            val jsStr : String = response.body!!.string()
            println(jsStr)
            Presencas.clear()
            val jsonArrayPresenca = JSONArray(jsStr)
            for ( index in  0 until jsonArrayPresenca.length()) {
                val jsonArticle : JSONObject = jsonArrayPresenca.get(index) as JSONObject
                val presencas = Presenca.fromJson(jsonArticle)
                Presencas.add(presencas)
                if(Presencas[index].id_atividade == atividadesTemp.id_atividade) {
                    if (Presencas[index].id_utilizador == Dados[0].id) {
                        verficador = true
                    }
                }
            }
        }
    }

}