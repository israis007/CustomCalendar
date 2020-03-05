package com.pirataram.calendarg.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarg.R

class RecyclerAdapter(
    val context: Context,
    val listAdapter: ArrayList<EventModel>,
    val onClick: OnTouchEvent
) : RecyclerView.Adapter<RecyclerAdapter.EventView>() {

    inner class EventView(private var eventView: View) : RecyclerView.ViewHolder(eventView) {
        fun bindItems(eventmodel: EventModel){
            val mainLayout = eventView.findViewById<ConstraintLayout>(R.id.main_event_card)
            val icon1 = eventView.findViewById<ImageView>(R.id.icon1)
            val icon2 = eventView.findViewById<ImageView>(R.id.icon2)
            val menuDots = eventView.findViewById<ImageView>(R.id.ivMore)
            val text_line = eventView.findViewById<AppCompatTextView>(R.id.text_line_1)
            
            val em = eventmodel.classPojo as EventData

            

            text_line.text = "${em.nombre} - ${em.dirección}"

            eventView.setOnClickListener {
                onClick.onClickEvent(eventmodel)
            }

            eventView.setOnLongClickListener {
                onClick.onLongClickEvent(eventmodel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventView =
        EventView(LayoutInflater.from(context).inflate(R.layout.customcard, parent, false))

    override fun getItemCount(): Int = listAdapter.size

    override fun onBindViewHolder(holder: EventView, position: Int) = holder.bindItems(listAdapter[position])

    interface OnTouchEvent {

        fun onClickEvent(eventObject: EventModel)

        fun onLongClickEvent(eventObject: EventModel): Boolean

    }

}