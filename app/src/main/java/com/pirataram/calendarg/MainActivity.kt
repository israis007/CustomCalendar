package com.pirataram.calendarg

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarcustom.tools.DateHourFormatter
import com.pirataram.calendarcustom.ui.OneDayLayout
import com.pirataram.calendarcustom.ui.viewpagercustom.ViewPagerEvent
import com.pirataram.calendarg.ui.dummy.eventsDay1
import com.pirataram.calendarg.ui.dummy.eventsDay2
import com.pirataram.calendarg.ui.dummy.eventsDay3
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    val listaEventosPrint = ArrayList<EventModel>()
    val listaEventosPrint2 = ArrayList<EventModel>()
    val listaEventosPrint3 = ArrayList<EventModel>()
    var listaCustomCalendar = ArrayList<OneDayLayout>()
    private var refPrevious = false
    private var refToday = false
    private var refNext = false
    private var calendarSelect: Calendar = Calendar.getInstance(Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        eventsDay1.forEach { element ->
            run {
                val card = LayoutInflater.from(this).inflate(R.layout.customcard, null, false)
                val tv = card.findViewById<AppCompatTextView>(R.id.text_line_1)
                card.setOnClickListener {
                    Toast.makeText(
                        this,
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
        eventsDay2.forEach { element ->
            run {
                val card = LayoutInflater.from(this).inflate(R.layout.customcard, null, false)
                val tv = card.findViewById<AppCompatTextView>(R.id.text_line_1)
                card.setOnClickListener {
                    Toast.makeText(
                        this,
                        "Click on view ${element.group.name}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                tv.text = ("${element.group.name} - ${element.place}")
                listaEventosPrint2.add(
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
        eventsDay3.forEach { element ->
            run {
                val card = LayoutInflater.from(this).inflate(R.layout.customcard, null, false)
                val tv = card.findViewById<AppCompatTextView>(R.id.text_line_1)
                card.setOnClickListener {
                    Toast.makeText(
                        this,
                        "Click on view ${element.group.name}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                tv.text = ("${element.group.name} - ${element.place}")
                listaEventosPrint3.add(
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

        val eventos = object: ViewPagerEvent{

            override fun currentPage(position: Int) {
                Log.d(TAG, "La página seleccionada es: $position")
            }

            override fun currentCalendar(calendar: Calendar) {
                Log.d(TAG, "La fecha seleccionada es: ${DateHourFormatter.getStringFormatted(calendar, "dd-MMMM-yyyy")}")
                calendarSelect = calendar
            }

            override fun addEventsPreviousDay(): ArrayList<EventModel> {
                return listaEventosPrint2
            }

            override fun addEventsCurrentDay(): ArrayList<EventModel> {
                return listaEventosPrint
            }

            override fun addEventsNextDay(): ArrayList<EventModel> {
                return listaEventosPrint3
            }

            override fun refreshEventsPreviousDay(): Boolean = refPrevious

            override fun refreshEventsToday(): Boolean = refToday

            override fun refreshEventsNextDay(): Boolean = refNext

            override fun getActivity(): Activity = this@MainActivity

            override fun getCustomCalendarView(oneDayLayout: OneDayLayout) {

            }

            override fun getAllCustomCalendarViews(arrayList: ArrayList<OneDayLayout>) {
                listaCustomCalendar = arrayList
            }
        }

        val calMax = Calendar.getInstance(Locale.getDefault()).apply {
            set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        }
        val calMin = Calendar.getInstance(Locale.getDefault()).apply {
            set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) - 10)
        }
        relojito.setMaxDate(calMax)
        relojito.setMinDate(calMin)
        relojito.addViewPagerListeners(eventos)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow()
//        }
    }
}
