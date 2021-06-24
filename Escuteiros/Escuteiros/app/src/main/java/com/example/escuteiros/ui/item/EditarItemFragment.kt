package com.example.escuteiros.ui.item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.escuteiros.Models.Item
import com.example.escuteiros.Models.Utilizadores
import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class EditarItemFragment : Fragment() {

    private var  itensJsonStr: String? = null

    lateinit var  nomeItem : EditText
    lateinit var  descricaoItem : EditText
    lateinit var  guardar : Button
    lateinit var  eliminar : Button

    var descricao1 : String = ""
    var nome1 : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itensJsonStr = it.getString("itens")
        }
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)


        val menuItem: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem.isVisible = false

        val menuItem2: MenuItem = menu.findItem(R.id.itemVerPresenca)
        menuItem2.isVisible = false

        val menuItem3: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem3.isVisible = false

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.editar_item_fragment, container, false)
        nomeItem = rootView.findViewById(R.id.nomeItem)
        descricaoItem = rootView.findViewById(R.id.descricaoItem)
        guardar = rootView.findViewById(R.id.buttonSave)
        eliminar = rootView.findViewById(R.id.buttonEliminar)

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val itensTemp = Item.fromJson(JSONObject(itensJsonStr))
        nomeItem.setText(itensTemp.nome_item)
        descricaoItem.setText(itensTemp.descricao_item)

        eliminar.setOnClickListener {

                GlobalScope.launch(Dispatchers.IO) {
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url("http://173.212.215.122:5000/api/item/${itensTemp.id_item}")
                        .delete()
                        .build()
                    client.newCall(request).execute().use { response ->
                        Log.d("coudelaria", response.message)
                        GlobalScope.launch (Dispatchers.Main){
                        if (response.message == "OK")
                        {
                            Toast.makeText(activity, "Item Apagado com sucesso! ", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

        guardar.setOnClickListener()
        {
        if(nomeItem.text.toString() == ""){
            nome1 = itensTemp.nome_item.toString()
        }
        else
        {
            nome1 = nomeItem.text.toString()
        }


        if(descricaoItem.text.toString() == ""){
            descricao1 = itensTemp.descricao_item.toString()
        }
        else
        {
            descricao1 = descricaoItem.text.toString()
        }


            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()

                val itens = Item(
                    itensTemp.id_item,
                    descricao1,
                    nome1,
                    itensTemp.id_inventario)

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    itens.toJson().toString()
                )
                Log.d("coudelaria", itens.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/item/${itensTemp.id_item}")
                    .put(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                    GlobalScope.launch (Dispatchers.Main){
                        if (response.message == "OK"){
                            Toast.makeText(activity, "Item editado com sucesso! ", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }

            }
        }

    }

}