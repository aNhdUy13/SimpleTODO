package com.nda.simpletodo.ui.home.CustomCalendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.nda.simpletodo.DbHandler
import com.nda.simpletodo.R
import com.nda.simpletodo.models.Note
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CustomCalendarView : LinearLayout {
    var img_previousDate: ImageView? = null
    var img_nextDate: ImageView? = null
    var txt_currentDate: TextView? = null
    var gridView_calendar: GridView? = null
    var calendar = Calendar.getInstance(Locale.ENGLISH)

    var dateFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    var monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    var yearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    var noteDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    var dateList: MutableList<Date> = ArrayList()
    var noteList: MutableList<Note> = ArrayList<Note>()
    var myGridLayoutAdapter: AdapterGridLayout? = null

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    {
        initLayoutAndUI()

        setUpAndDisplayCalendar()

        img_previousDate!!.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            setUpAndDisplayCalendar()
        }
        img_nextDate!!.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            setUpAndDisplayCalendar()
        }
        gridView_calendar!!.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                val selectedDate = noteDateFormat.format(dateList[i])
                val splitDate = selectedDate.split("-")

                val strSelectedDate = "${splitDate[2]}/${splitDate[1]}/${splitDate[0]}"

                var noteListSelected = ArrayList<Note>()
                if (context != null) {
                    noteListSelected = DbHandler.getInstance(context)?.noteDao()?.getNoteFromDay(strSelectedDate) as ArrayList<Note>
                }



            }
        }
    }

    /**
     *
     * (Related) Event
     *
     */
    private fun displayEventByDate(date: String): List<Note>? {

        return DbHandler.getInstance(context)?.noteDao()?.getNoteFromDay(date)
    }


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun initLayoutAndUI() {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.calendar_layout, this)

        img_previousDate = view.findViewById(R.id.img_previousDate)
        img_nextDate = view.findViewById(R.id.img_nextDate)
        txt_currentDate = view.findViewById(R.id.txt_currentDate)
        gridView_calendar = view.findViewById(R.id.gridView_calendar)
    }

    /**
     *
     * (Related) Calendar
     *
     */
    private fun setUpAndDisplayCalendar() {
        // Display date for header of custom calendar
        val currDate = dateFormat.format(calendar.time)
        val splitCurrDate = currDate.split(" ".toRegex()).toTypedArray()
        var currMonth = splitCurrDate[0]
        val currYear = splitCurrDate[1]
//        if (splitCurrDate[0] == "December") {
//            currMonth = "Tháng 12"
//        } else if (splitCurrDate[0] == "January") {
//            currMonth = "Tháng 1"
//        } else if (splitCurrDate[0] == "February") {
//            currMonth = "Tháng 2"
//        } else if (splitCurrDate[0] == "March") {
//            currMonth = "Tháng 3"
//        } else if (splitCurrDate[0] == "April") {
//            currMonth = "Tháng 4"
//        } else if (splitCurrDate[0] == "May") {
//            currMonth = "Tháng 5"
//        } else if (splitCurrDate[0] == "June") {
//            currMonth = "Tháng 6"
//        } else if (splitCurrDate[0] == "July") {
//            currMonth = "Tháng 7"
//        } else if (splitCurrDate[0] == "August") {
//            currMonth = "Tháng 8"
//        } else if (splitCurrDate[0] == "September") {
//            currMonth = "Tháng 9"
//        } else if (splitCurrDate[0] == "October") {
//            currMonth = "Tháng 10"
//        } else if (splitCurrDate[0] == "November") {
//            currMonth = "Tháng 11"
//        }
        txt_currentDate!!.text = "$currMonth, $currYear"


        // Get and display day for custom calendar
        dateList.clear()
        val monthCalendar = calendar.clone() as Calendar
        monthCalendar[Calendar.DAY_OF_MONTH] = 1

        val firstDayOfMonth = monthCalendar[Calendar.DAY_OF_WEEK] - 1
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)

        displayEventPerMonth(monthFormat.format(calendar.time), yearFormat.format(calendar.time))

        while (dateList.size < MAX_CALENDAR_DAYS) {
            dateList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        myGridLayoutAdapter = AdapterGridLayout(context!!, dateList, calendar, noteList)
        gridView_calendar!!.adapter = myGridLayoutAdapter
    }

    private fun displayEventPerMonth(month: String, year: String) {
        noteList.clear()
//        val cursor: Cursor = MainActivity.database.getData(
//            "SELECT * FROM CalendarEvent WHERE cMonth = '" + month
//                    + "' AND cYear = '" + year + "'"
//        )
//        while (cursor.moveToNext()) {
//            val cEvent = cursor.getString(0)
//            val cTime = cursor.getString(1)
//            val cDate = cursor.getString(2)
//            val cMonth = cursor.getString(3)
//            val cYear = cursor.getString(4)
//            val events = Events(cEvent, cTime, cDate, cMonth, cYear)
//            eventsList.add(events)
//        }
    }

    companion object {
        private val MAX_CALENDAR_DAYS = 42
    }
}