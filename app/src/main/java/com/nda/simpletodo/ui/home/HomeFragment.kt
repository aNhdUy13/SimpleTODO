package com.nda.simpletodo.ui.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nda.simpletodo.DbHandler
import com.nda.simpletodo.R
import com.nda.simpletodo.UtilsManager
import com.nda.simpletodo.models.Note
import kotlinx.android.synthetic.main.dialog_note.*
import kotlinx.android.synthetic.main.dialog_note.cv_action
import kotlinx.android.synthetic.main.dialog_note.cv_cancel
import kotlinx.android.synthetic.main.dialog_note.edt_noteDes
import kotlinx.android.synthetic.main.dialog_note.edt_noteTitle
import kotlinx.android.synthetic.main.dialog_note.img_selectTime
import kotlinx.android.synthetic.main.dialog_note.txt_category
import kotlinx.android.synthetic.main.dialog_note.txt_noteDate
import kotlinx.android.synthetic.main.dialog_note_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val currentDate = sdf.format(Date())


    lateinit var adapterNote: AdapterNote
    var noteList = ArrayList<Note>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setUpAndDisplayNotes()

        init()
    }

    private fun init() {
        val splitCurrDate = currentDate.toString().split("/")
        val strCurrDate = UtilsManager.formatMonthToStr(splitCurrDate[1], splitCurrDate[0], splitCurrDate[2])

        img_addNote?.setOnClickListener {
            dialogAddNote()
        }

        img_showNotes?.setOnClickListener {
            Toast.makeText(context, "All Notes", Toast.LENGTH_SHORT).show()
            setUpAndDisplayNotes()
        }

        calendarView_note?.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->

            var sDay = dayOfMonth.toString()
            var sMonth = (month+1).toString()
            var sYear = year.toString()

            if (dayOfMonth < 10)
            {
                sDay = "0$dayOfMonth"
            }
            if ((month+1) < 10)
            {
                sMonth = "0${month+1}"
            }

            var sDate = "$sDay/$sMonth/$sYear"

            setUpAndDisplayNotesBySelectedDay(sDate)
        })

        swipeRefreshLayout.setOnRefreshListener {
            setUpAndDisplayNotes()

            swipeRefreshLayout.isRefreshing = false

        }
    }

    fun setUpAndDisplayNotes() {

        noteList = DbHandler.getInstance(requireContext())?.noteDao()?.getAllNote() as ArrayList<Note>

        if (noteList.size <= 0)
        {
            txt_noDataFound?.visibility  = VISIBLE
            rcv_home?.visibility = GONE
        } else {
            rcv_home?.visibility = VISIBLE
            txt_noDataFound?.visibility  = GONE
        }
        adapterNote = AdapterNote(noteList,this)
        rcv_home?.adapter = adapterNote
        rcv_home?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapterNote.setChangedDataTable(noteList)

    }
    fun setUpAndDisplayNotesBySelectedDay(selectedDay: String) {

        noteList = DbHandler.getInstance(requireContext())?.noteDao()?.getNoteFromDay(selectedDay) as ArrayList<Note>

        if (noteList.size <= 0)
        {
            txt_noDataFound?.visibility  = VISIBLE
            rcv_home?.visibility = GONE
        } else {
            rcv_home?.visibility = VISIBLE
            txt_noDataFound?.visibility  = GONE
        }

        adapterNote = AdapterNote(noteList,this)
        rcv_home?.adapter = adapterNote
        rcv_home?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapterNote.setChangedDataTable(noteList)

    }
    private fun dialogAddNote() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_note)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.txt_noteDate.text = currentDate.toString()

        dialog.img_selectTime.setOnClickListener {
            dialogSetUpTime(dialog.txt_noteDate, requireContext())
        }

        dialog.cv_action.setOnClickListener{
            val nTitle = dialog.edt_noteTitle.text.toString()
            val nDes = dialog.edt_noteDes.text.toString()
            val nDate = dialog.txt_noteDate.text.toString()
            val nCategory = dialog.txt_category.text.toString()

            if (nTitle.isEmpty() || nDes.isEmpty())
            {
                Toast.makeText(context, "Error : Fulfill data", Toast.LENGTH_SHORT).show()
            }
            else {
                val note = Note(null,nTitle, nDes, nDate, false,nCategory)
                DbHandler.getInstance(requireContext())?.noteDao()?.insertNote(note)

                setUpAndDisplayNotes()
                dialog.dismiss()
            }
        }

        dialog.cv_cancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    fun dialogDetailNote(note: Note) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_note_detail)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.img_selectTime.setOnClickListener {
            dialogSetUpTime(dialog.txt_noteDate, requireContext())
        }

        dialog.txt_category.text = note.nCategory
        dialog.txt_noteDate.text = note.nCreatedDate
        dialog.edt_noteTitle.setText(note.nTitle)
        dialog.edt_noteDes.setText(note.nDescription)

        if (!note.nIsFinish)
        {
            dialog.txt_titleFinishStatus.text = "( Not Finish )"
        } else {
            dialog.txt_titleFinishStatus.text = "( Finished )"
        }

        dialog.cv_action.setOnClickListener{
            val nTitle = dialog.edt_noteTitle.text.toString()
            val nDes = dialog.edt_noteDes.text.toString()
            val nDate = dialog.txt_noteDate.text.toString()
            val nCategory = dialog.txt_category.text.toString()

            if (nTitle.isEmpty() || nDes.isEmpty())
            {
                Toast.makeText(context, "Error : Fulfill data", Toast.LENGTH_SHORT).show()
            }
            else {
                val note = Note(note.nId,nTitle, nDes, nDate, note.nIsFinish,nCategory)
                DbHandler.getInstance(requireContext())?.noteDao()?.updateNote(note)

                setUpAndDisplayNotes()
                dialog.dismiss()
            }
        }

        dialog.cv_delete.setOnClickListener{
            DbHandler.getInstance(requireContext())?.noteDao()?.deleteNote(note)

            setUpAndDisplayNotes()

            dialog.dismiss()
        }

        dialog.show()
    }

    fun dialogSetUpTime(txt_showDate: TextView, context: Context) {
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]

        val datePickerDialog = DatePickerDialog(context, { view, Myear, Mmonth, MdayOfMonth ->

            var selectedDay: String = MdayOfMonth.toString()
            var selectedMonth: String = (Mmonth+1).toString()

            if (MdayOfMonth < 10)
            {
                selectedDay = "0$MdayOfMonth"
            }
            if ((Mmonth+1) < 10)
            {
                selectedMonth = "0${Mmonth+1}"
            }

            val finalDate: String = "$selectedDay/$selectedMonth/$Myear"

            txt_showDate.text = finalDate
        }, year, month, day)

        datePickerDialog.show()
    }


}