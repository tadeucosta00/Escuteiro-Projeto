package com.example.escuteiros.Models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.escuteiros.R
import com.example.escuteiros.ui.calendario.calendarios

class CalendariosAdapter( val datas: ArrayList<String>) : RecyclerView.Adapter<CalendariosAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataInicial : TextView = itemView.findViewById(R.id.dataInicial)
        val dataFinal : TextView = itemView.findViewById(R.id.dataFinal)
        val nome : TextView = itemView.findViewById(R.id.nome)
        val imagem : ImageView = itemView.findViewById(R.id.imagem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.rowcalendarios, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        calendarios.sortedBy{it.datainicial}
        calendarios.sortedBy{it.datafinal}

        holder.nome.text = calendarios[position].nome
        holder.dataInicial.text = calendarios[position].datainicial
        holder.dataFinal.text = calendarios[position].datafinal
        if (calendarios[position].tipo == "Raid")
        {
            holder.imagem.setImageResource(R.mipmap.ic_caminhada_foreground)
        }
        else if (calendarios[position].tipo == "Missa")
        {
            holder.imagem.setImageResource(R.mipmap.ic_missa_foreground)
        }
        else if (calendarios[position].tipo == "Acampamento")
        {
            holder.imagem.setImageResource(R.mipmap.ic_acampamento_foreground)
        }
        else if (calendarios[position].tipo == "Jantares")
        {
            holder.imagem.setImageResource(R.mipmap.ic_jantar_foreground)
        }
        else if (calendarios[position].tipo == "Caminhada")
        {
            holder.imagem.setImageResource(R.mipmap.ic_caminhada1_foreground)
        }
        else{
            holder.imagem.setImageResource(R.mipmap.ic_outro_foreground)
        }
    }

    override fun getItemCount()= datas.size
}