package com.nda.simpletodo.ui.home.CustomCalendar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nda.simpletodo.DbHandler
import com.nda.simpletodo.R
import com.nda.simpletodo.UtilsManager
import com.nda.simpletodo.models.Note
import kotlinx.android.synthetic.main.bottomsheet_detail_calendar_day.*
import kotlinx.android.synthetic.main.calendar_layout.view.*
import kotlinx.android.synthetic.main.dialog_note_detail.txt_titleFinishStatus
import kotlinx.android.synthetic.main.dialog_note_detail_2.*
import java.text.SimpleDateFormat
import java.util.*


class CustomCalendarView : LinearLayout {
    var view: View? = null

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
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.calendar_layout, this)

        setUpAndDisplayCalendar()

        view?.img_previousDate!!.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            setUpAndDisplayCalendar()
        }
        view?.img_nextDate!!.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            setUpAndDisplayCalendar()
        }
        view?.gridView_calendar!!.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                val calendarEventDayView = LayoutInflater.from(adapterView.context)
                    .inflate(R.layout.bottomsheet_detail_calendar_day, null)
                val bottomSheetDialog = BottomSheetDialog(context)
                bottomSheetDialog.setContentView(calendarEventDayView)


                // Get list of note from selected date
                val selectedDate = noteDateFormat.format(dateList[i])
                val splitDate = selectedDate.split("-")
                val strSelectedDate = "${splitDate[2]}/${splitDate[1]}/${splitDate[0]}"
                var noteListSelected = ArrayList<Note>()
                if (context != null) {
                    noteListSelected = DbHandler.getInstance(context)?.noteDao()?.getNoteFromDay(strSelectedDate) as ArrayList<Note>
                }

                bottomSheetDialog.txt_showSelectDate.text = "${strSelectedDate} (${noteListSelected.size})"

                val adapterNote = AdapterNote(noteListSelected, this@CustomCalendarView)
                bottomSheetDialog.rcv_showNotes?.adapter = adapterNote
                bottomSheetDialog.rcv_showNotes?.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false)


                adapterNote.setChangedDataTable(noteListSelected)

                bottomSheetDialog.show()
            }
        }
    }



    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     *
     * (Related) Calendar
     *
     */
    fun setUpAndDisplayCalendar() {
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
        view?.txt_currentDate!!.text = "$currMonth, $currYear"


        // Get and display day for custom calendar
        dateList.clear()
        val monthCalendar = calendar.clone() as Calendar
        monthCalendar[Calendar.DAY_OF_MONTH] = 1

        val firstDayOfMonth = monthCalendar[Calendar.DAY_OF_WEEK] - 1
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)

        while (dateList.size < MAX_CALENDAR_DAYS) {
            dateList.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        noteList = DbHandler.getInstance(context)?.noteDao()?.getAllNote() as MutableList<Note>

        myGridLayoutAdapter = AdapterGridLayout(context!!, dateList, calendar, noteList)
        view!!.gridView_calendar!!.adapter = myGridLayoutAdapter
    }



    companion object {
        private val MAX_CALENDAR_DAYS = 42
    }


    fun dialogDetailNote(note: Note, nCreatedDate: String,) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_note_detail_2)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.img_selectTime_2.setOnClickListener {
            UtilsManager.dialogSetUpTime(dialog.txt_noteDate_2, context)
        }

        dialog.txt_category_2.text = note.nCategory
        dialog.txt_noteDate_2.text = note.nCreatedDate
        dialog.edt_noteTitle_2.setText(note.nTitle)
        dialog.edt_noteDes_2.setText(note.nDescription)

        if (!note.nIsFinish)
        {
            dialog.txt_titleFinishStatus.text = "( Not Finish )"
        } else {
            dialog.txt_titleFinishStatus.text = "( Finished )"
        }


        dialog.cv_action_2.setOnClickListener{
            val nTitle = dialog.edt_noteTitle_2.text.toString()
            val nDes = dialog.edt_noteDes_2.text.toString()
            val nDate = dialog.txt_noteDate_2.text.toString()
            val nCategory = dialog.txt_category_2.text.toString()

            if (nTitle.isEmpty() || nDes.isEmpty())
            {
                Toast.makeText(context, "Error : Fulfill data", Toast.LENGTH_SHORT).show()
            }
            else {
                val note = Note(note.nId,nTitle, nDes, nDate, note.nIsFinish,nCategory)
                DbHandler.getInstance(context)?.noteDao()?.updateNote(note)
                dialog.dismiss()
            }
        }

        dialog.cv_delete_2.setOnClickListener{
            DbHandler.getInstance(context)?.noteDao()?.deleteNote(note)

//            val noteListSelected = DbHandler.getInstance(context)?.noteDao()?.getNoteFromDay(nCreatedDate) as ArrayList<Note>
//            val adapterNote = AdapterNote(noteListSelected, this@CustomCalendarView)
//            adapterNote.setChangedDataTable(noteListSelected)

            dialog.dismiss()
        }

        dialog.show()
    }



}