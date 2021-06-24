package com.example.escuteiros.ui.item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.escuteiros.Models.Item
import com.example.escuteiros.Models.Posts
import com.example.escuteiros.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class AdicionarItemFragment : Fragment() {

    lateinit var  nome : EditText
    lateinit var  descricao : EditText
    lateinit var  guardar : Button

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
        val rootView = inflater.inflate(R.layout.adicionar_item_fragment, container, false)
        nome = rootView.findViewById<EditText>(R.id.nomeItem)
        descricao = rootView.findViewById<EditText>(R.id.descricaoItem)
        guardar = rootView.findViewById(R.id.buttonSave)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        guardar.setOnClickListener()
        {
            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val itens = Item(
                    null,
                    descricao.text.toString(),
                    nome.text.toString(),
                    0
                )
                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    itens.toJson().toString()
                )
                Log.d("coudelaria", itens.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/item")
                    .post(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                }
            }
        }
    }
}