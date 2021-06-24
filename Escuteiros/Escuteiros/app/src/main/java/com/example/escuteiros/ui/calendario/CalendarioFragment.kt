package com.example.escuteiros.ui.calendario

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.rangeTo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.escuteiros.Models.Atividades
import com.example.escuteiros.Models.Calendario
import com.example.escuteiros.Models.CalendariosAdapter
import com.example.escuteiros.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

var calendarios : MutableList<Calendario> = arrayListOf()
class CalendarioFragment : Fragment() {

    lateinit var compactCalendar: CompactCalendarView
    private val dateFormatMonth: SimpleDateFormat = SimpleDateFormat(
        "MMMM - yyyy",
        Locale.getDefault()
    )

    lateinit var textView: TextView
    lateinit var textView11: TextView
    lateinit var recyclerView : RecyclerView

    val sdf = SimpleDateFormat("MMMM - yyyy")
    val dataAtual = sdf.format(Date())
    var contador = 0
    var data : String = ""

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
        val rootView = inflater.inflate(R.layout.calendario_fragment, container, false)
        compactCalendar =  rootView.findViewById(R.id.calendario)
        textView11 = rootView.findViewById(R.id.textView11)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        val formatter = SimpleDateFormat("dd-MM-yyyy")
        formatter.isLenient = false

        textView11.text = dataAtual

        compactCalendar.setUseThreeLetterAbbreviation(true)

        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/atividades").build()
        client.newCall(request).execute().use { response ->
            val jsStr : String = response.body!!.string()
            val jsonArrayAtividades = JSONArray(jsStr)
            contador = jsonArrayAtividades.length()
        }

        println(contador)

        var datainicio : Array<String> = Array(contador) {""}
        var datafim : Array<String> = Array(contador) {""}

        for(index in 0 until contador)
        {
            val dataInicial = calendarios[index].datainicial
            val dataInicialDate = formatter.parse(dataInicial)
            val dataMilisegundos = dataInicialDate.time

            val dataFinal = calendarios[index].datafinal
            val dataFinalDate = formatter.parse(dataFinal)
            val dataMilisegundosFinal = dataFinalDate.time

            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: Date = dateFormat.parse(calendarios[index].datainicial)
            val date2: Date = dateFormat.parse(calendarios[index].datafinal)

            datainicio[index] = date.toString()
            datafim[index] = date2.toString()


            val ev1 = Event(Color.BLACK, dataMilisegundos)
            compactCalendar.addEvent(ev1)

            val ev2 = Event(Color.BLACK, dataMilisegundosFinal)
            compactCalendar.addEvent(ev2)

            val datas : ArrayList<String> = ArrayList()


            for (i in 0 until contador){
                datas.add("Dia: ")
            }

            recyclerView.layoutManager = LinearLayoutManager (context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = CalendariosAdapter(datas)

        }
        //ver dia
        compactCalendar.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                for (index in 0 until contador) {
                    if (dateClicked.toString().compareTo(datainicio[index]) === 0 || dateClicked.toString().compareTo(datafim[index]) === 0
                    ) {
                        Toast.makeText(activity, calendarios[index].nome, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                data = dateFormatMonth.format(firstDayOfNewMonth).toString()
                textView11.text = data
            }
        })

    }
}

