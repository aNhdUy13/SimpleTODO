package com.nda.simpletodo.ui.home.CustomCalendar

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.nda.simpletodo.R
import com.nda.simpletodo.models.Note
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AdapterGridLayout(context: Context, var dateList: List<Date>, var calendar: Calendar, noteList: List<Note>)
    : ArrayAdapter<Any?>(context, R.layout.item_day_cell)
{
    var noteList: List<Note>
    var layoutInflater: LayoutInflater
    var txt_calendarDay: TextView? = null
    var txt_numberOfEvent: TextView? = null

    init {
        this.noteList = noteList
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val monthDate = dateList[position]
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = monthDate

        val dayNo = dateCalendar[Calendar.DAY_OF_MONTH]
        val displayMonth = dateCalendar[Calendar.MONTH] + 1
        val displayYear = dateCalendar[Calendar.YEAR]

        val calMonth = calendar[Calendar.MONTH] + 1
        val calYear = calendar[Calendar.YEAR]
        val calDay = calendar[Calendar.DAY_OF_MONTH]

        val currDateCalendar = Calendar.getInstance()
        val currMonth = currDateCalendar[Calendar.MONTH] + 1
        val currYear = currDateCalendar[Calendar.YEAR]
        val currDay = currDateCalendar[Calendar.DAY_OF_MONTH]

        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_day_cell, parent, false)
            txt_calendarDay = view!!.findViewById(R.id.txt_calendarDay)
            txt_numberOfEvent = view.findViewById(R.id.txt_numberOfEvent)
        }
        if (displayMonth == calMonth && displayYear == calYear) {
            // Kiểm tra xem những ngày nào thuộc tháng ở trong GridView.
            // Nếu ngày đó thuộc tháng => text sẽ đc tô đậm
            txt_calendarDay!!.setTypeface(txt_calendarDay!!.typeface, Typeface.BOLD)
            txt_calendarDay!!.setTextColor(Color.parseColor("#000000"))
            view!!.setBackgroundColor(Color.parseColor("#FFFFFF"))
            if (dayNo == currDay && displayMonth == currMonth && displayYear == currYear) {
                // Kiểm tra xem Ngày hiện tại thuộc chỗ nào => HIGHLIGHT
                txt_calendarDay!!.setTypeface(txt_calendarDay!!.typeface, Typeface.BOLD)
                txt_calendarDay!!.setTextColor(Color.parseColor("#FFFFFFFF"))
                txt_numberOfEvent!!.setTextColor(Color.parseColor("#FFFFFFFF"))
                view.setBackgroundColor(context.resources.getColor(R.color.purple_500))
            }
        } else {
            // Nếu ngày đó k thuộc tháng => có màu default
            txt_calendarDay!!.setTextColor(Color.parseColor("#B3B3B3"))
            view!!.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        // Set ngày cho từng khối trong Gridview
        val txt_dayNumber = view.findViewById<TextView>(R.id.txt_calendarDay)
        txt_dayNumber.text = dayNo.toString()

        // Hiển thị số lượng events của specific day
        val txt_numberOfEvent = view.findViewById<TextView>(R.id.txt_numberOfEvent)
        val eventCalendar = Calendar.getInstance()
        val arrayList = ArrayList<String>()
        for (i in noteList.indices) {
            eventCalendar.time = convertStringToDate(noteList[i].nCreatedDate)
            if (dayNo == eventCalendar[Calendar.DAY_OF_MONTH] && displayMonth == eventCalendar[Calendar.MONTH] + 1 && displayYear == eventCalendar[Calendar.YEAR]) {
                arrayList.add(noteList[i].nTitle)
                txt_numberOfEvent.text = arrayList.size.toString() + " notes"
            }
        }
        return view
    }

    private fun convertStringToDate(eventDate: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var date: Date? = null
        try {
            date = format.parse(eventDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    override fun getCount(): Int {
        return dateList.size
    }

    override fun getPosition(item: Any?): Int {
        return dateList.indexOf(item)
    }

    override fun getItem(position: Int): Any? {
        return dateList[position]
    }
}
