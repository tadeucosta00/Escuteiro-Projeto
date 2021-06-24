package com.example.escuteiros.ui.inventario

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.escuteiros.Models.Inventario
import com.example.escuteiros.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class AdicionarInventarioFragment : Fragment() {

    lateinit var  nomeinv : EditText
    lateinit var  guardar : Button
    lateinit var  imagem : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val rootView = inflater.inflate(R.layout.adicionar_inventario_fragment, container, false)

        nomeinv = rootView.findViewById(R.id.editTextNomeInventario)
        guardar = rootView.findViewById(R.id.button2)
        imagem = rootView.findViewById(R.id.imagem)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imagem.setImageResource(R.mipmap.criarinventario_foreground)
        guardar.setOnClickListener()
        {
            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val inventarios = Inventario(
                    null,
                    nomeinv.text.toString()
                )
                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    inventarios.toJson().toString()
                )
                Log.d("coudelaria", inventarios.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/inventario")
                    .post(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                }
            }
        }
    }
}