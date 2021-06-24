package com.example.escuteiros.ui.Perfil

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.escuteiros.Atualizar
import com.example.escuteiros.R
import com.example.escuteiros.ui.Perfil.VerPerfilFragmentDirections
import com.example.escuteiros.ui.home.Dados
import com.squareup.picasso.Picasso

class VerPerfilFragment : Fragment() {

    lateinit var  textView: TextView
    lateinit var  textView2: TextView
    lateinit var  textView3: TextView
    lateinit var  textView4: TextView
    lateinit var  textView5: TextView
    lateinit var  textView6: TextView
    lateinit var  textView7: TextView
    lateinit var  textView8: TextView
    lateinit var imageView : ImageView


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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.ver_perfil_fragment, container, false)
        textView = rootView.findViewById(R.id.textView)
        textView2 = rootView.findViewById(R.id.textView2)
        textView3 = rootView.findViewById(R.id.textView3)
        textView4 = rootView.findViewById(R.id.textView4)
        textView5 = rootView.findViewById(R.id.textView5)
        textView6 = rootView.findViewById(R.id.textView6)
        textView7 = rootView.findViewById(R.id.textView7)
        textView8 = rootView.findViewById(R.id.textView8)
        imageView = rootView.findViewById(R.id.imageViewPost)

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if(Dados[0].imagem.trim().toString() != "null") {
            Picasso.get().load(Dados[0].imagem).resize(250, 250).into(imageView)
        }
        else
        {
            Picasso.get().load("https://i.imgur.com/V9CRLdo.png").resize(250, 250).into(imageView)
        }

        var seccao = ""
        if(Dados[0].id_seccao == 1)
        {
            seccao = "Lobitos"
        }
        if(Dados[0].id_seccao == 2)
        {
            seccao = "Exploradores"
        }
        if(Dados[0].id_seccao == 3)
        {
            seccao = "Pioneiros"
        }
        if(Dados[0].id_seccao == 4)
        {
            seccao = "Caminheiros"
        }

        textView.text =  Dados[0].nome
        textView2.text = "Email: " + Dados[0].email
        textView8.text = "Morada: " + Dados[0].morada
        textView4.text = "Telemovel: " + Dados[0].contacto.toString()
        textView5.text = "NIN: " + Dados[0].nin
        textView6.text = "Genero: " + Dados[0].genero
        textView7.text = "Data de Nascimento: " + Dados[0].data
        textView3.text = "Secção: " + seccao

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId){
            R.id.itemEdit -> {
                val action = VerPerfilFragmentDirections.actionNavVerperfilToNavEditarperfil()
                this.view?.findNavController()?.navigate(action)
                return true
            }
        }
        return false
    }
}