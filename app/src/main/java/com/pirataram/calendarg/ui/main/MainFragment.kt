package com.pirataram.calendarg.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarg.R
import com.pirataram.calendarg.ui.dummy.eventsDay3
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        val view = View.inflate(activity!!, R.layout.agenda_event_recycler_prototype, null)
        /*val view = LayoutInflater.from(activity!!).inflate(R.layout.agenda_event_recycler_prototype, null)
        val cal1 = Calendar.getInstance(Locale.getDefault())
        cal1[Calendar.HOUR_OF_DAY] = 11
        cal1[Calendar.MINUTE] = 0
        val cal2 = Calendar.getInstance(Locale.getDefault())
        cal2[Calendar.HOUR_OF_DAY] = 12
        cal2[Calendar.MINUTE] = 0
        val cal3 = Calendar.getInstance(Locale.getDefault())
        cal3[Calendar.HOUR_OF_DAY] = 13
        cal3[Calendar.MINUTE] = 0
        val cal4 = Calendar.getInstance(Locale.getDefault())
        cal4[Calendar.HOUR_OF_DAY] = 15
        cal4[Calendar.MINUTE] = 20
        val cal5 = Calendar.getInstance(Locale.getDefault())
        cal5[Calendar.MINUTE] = 17
        cal5[Calendar.HOUR_OF_DAY] = 40
        val cal6 = Calendar.getInstance(Locale.getDefault())
        cal6[Calendar.HOUR_OF_DAY] = 18
        cal6[Calendar.MINUTE] = 30
        val cal7 = Calendar.getInstance(Locale.getDefault())
        cal7[Calendar.HOUR_OF_DAY] = 12
        cal7[Calendar.MINUTE] = 10
        val cal8 = Calendar.getInstance(Locale.getDefault())
        cal8[Calendar.HOUR_OF_DAY] = 13
        cal8[Calendar.MINUTE] = 30

        val listaEventos = ArrayList<EventData>()
        val listaEventosPrint = ArrayList<EventModel>()
        listaEventos.add(EventData(1, "Simon el Gran Varón", "en la sala de un hospital", 2))
        listaEventos.add(EventData(2, "Ernesto", "no sé por donde viva", 2))
        listaEventos.add(EventData(3, "Pirata Ram", "Arquitectos 34, Escandón II Sección, Miguel Hidalgo, CDMX, México", 2))
        listaEventos.add(EventData(4, "Mariana", "calle matamoros 23, Centro, Ciudad Sahagún, Hidalgo, México", 2))*/
//        listaEventos.add(EventData(5, "Laura", "faraway Aguascalientes", 2))
//        listaEventos.add(EventData(6, "Sandra García Magno", "oficina 27 piso 4", 2))
//        val adapter = RecyclerAdapter(activity!!, listaEventos, object:
//            RecyclerAdapter.OnTouchEvent {
//            override fun onClickEvent(eventObject: com.pirataram.calendarg.ui.main.adapter.EventData) {
//                Toast.makeText(activity!!, "Hola que hace presionastes (${eventObject.id}) -> ${eventObject.nombre}", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onLongClickEvent(eventObject: com.pirataram.calendarg.ui.main.adapter.EventData): Boolean {
//                Toast.makeText(activity!!, "Dejaste presionado (${eventObject.id}) -> ${eventObject.nombre}", Toast.LENGTH_LONG).show()
//                return true
//            }
//        })
        /*listaEventos.forEach { element -> run {
            val card = LayoutInflater.from(context).inflate(R.layout.customcard, null, false)

            card.setOnClickListener {
                Toast.makeText(context, "Click on view ${element.nombre}", Toast.LENGTH_LONG).show()
            }

            card.setBackgroundResource(when(element.id){
                1 -> R.drawable.color1rounded
                2 -> R.drawable.color2rounded
                else -> R.drawable.color3rounded
            })

            val tv = card.findViewById<AppCompatTextView>(R.id.text_line_1)
            tv.text = (element.nombre + " - " + element.dirección)
            listaEventosPrint.add(EventModel(
                element.id.toLong(),
                when(element.id){
                    1 -> cal1
                    2 -> cal3
                    3 -> cal5
                    else -> cal7
                },
                when(element.id){
                    1 -> cal2
                    2 -> cal4
                    3 -> cal6
                    else -> cal8
                },
                ContextCompat.getColor(activity!!, when(element.id){
                    1 -> R.color.colorEvent2
                    2 -> R.color.colorEvent3
                    else -> R.color.colorEvent1
                }),
                card,
                element
            ))
            }
        }*/

        val listaEventosPrint = ArrayList<EventModel>()
        eventsDay3.forEach { element ->
            run {
                val card = LayoutInflater.from(context).inflate(R.layout.customcard, null, false)
                val tv = card.findViewById<AppCompatTextView>(R.id.text_line_1)
                card.setOnClickListener {
                    Toast.makeText(
                        context,
                        "Click on view ${element.group.name}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                tv.text = ("${element.group.name} - ${element.place}")
                listaEventosPrint.add(
                    EventModel(
                        Random().nextLong(),
                        element.initTime,
                        element.endTime,
                        card,
                        element
                    )
                )
            }
        }



//        clock.addEvents(activity!!, listaEventosPrint)
    }

}
