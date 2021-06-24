package com.example.escuteiros.ui.catalgo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.escuteiros.Models.Catalgo
import com.example.escuteiros.Models.Data
import com.example.escuteiros.Models.GuardarIdCatalogo


import com.example.escuteiros.R
import com.example.escuteiros.ui.home.Dados
import org.json.JSONObject

class VerInstruncoesFragment : Fragment() {

    private var  CatalgoJsonStr: String? = null
    lateinit var TextViewTitulo : TextView
    lateinit var TextViewInstruncoes : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            CatalgoJsonStr = it.getString("catalogo")
        }
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val menuItem: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem.isVisible = false

        val menuItem2: MenuItem = menu.findItem(R.id.itemVerPresenca)
        menuItem2.isVisible = false
        var permissao = 0
        permissao = Dados[0].permissao
        if (permissao == 1)
        {
            val menuItem3: MenuItem = menu.findItem(R.id.itemEdit)
            menuItem3.isVisible = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.ver_instruncoes_fragment, container, false)

        TextViewTitulo = rootView.findViewById(R.id.titulo)
        TextViewInstruncoes = rootView.findViewById(R.id.Descricao)

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val CatalgoTemp = Catalgo.fromJson(JSONObject(CatalgoJsonStr))
        TextViewTitulo.text = CatalgoTemp.nome_catalogo.toString()
        TextViewInstruncoes.text = CatalgoTemp.instrucoes.toString()
        Editar.clear()
        Editar.add(GuardarIdCatalogo(CatalgoTemp.id_catalogo.toString().toInt()))
        println(CatalgoTemp.id_catalogo)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId){
            R.id.itemEdit -> {
                val action = VerInstruncoesFragmentDirections.actionNavVerCatalogoToNavEditarCatalogo()
                this.view?.findNavController()?.navigate(action)
                return true
            }
        }
        return false
    }


}