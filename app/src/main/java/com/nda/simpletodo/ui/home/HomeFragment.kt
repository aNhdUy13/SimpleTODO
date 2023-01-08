package com.nda.simpletodo.ui.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nda.simpletodo.DbHandler
import com.nda.simpletodo.R
import com.nda.simpletodo.UtilitiesManager
import com.nda.simpletodo.models.Note
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val currentDate = sdf.format(Date())

    var txt_currentDate: TextView? = null
    var img_addNote: ImageView? = null
    var rcv_home: RecyclerView? = null


    lateinit var adapterNote: AdapterNote
    var noteList = ArrayList<Note>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initUI(view)

        setUpAndDisplayNotes()

        init()


        return view
    }


    private fun init() {
        val splitCurrDate = currentDate.toString().split("/")
        val strCurrDate = formatMonthToStr(splitCurrDate[1], splitCurrDate[0], splitCurrDate[2])
        txt_currentDate?.text = strCurrDate

        img_addNote?.setOnClickListener {
            dialogAddNote()
        }

//        adapterNote.setOnItemClickListener(object : AdapterNote.onCustomItemClickListener{
//            override fun onItemClick(position: Int) {
//                val note = noteList[position]
//
//                dialogDetailNote(note)
//            }
//
//        })
    }

    fun setUpAndDisplayNotes() {

        noteList = DbHandler.getInstance(requireContext())?.noteDao()?.getAllNote() as ArrayList<Note>

        adapterNote = AdapterNote(noteList,this)
        rcv_home?.adapter = adapterNote
        rcv_home?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapterNote.setChangedDataTable(noteList)

    }

    private fun dialogAddNote() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_note)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txt_titleDialogNote: TextView = dialog.findViewById(R.id.txt_titleDialogNote)
        val edt_noteTitle: EditText = dialog.findViewById(R.id.edt_noteTitle)
        val edt_noteDes: EditText = dialog.findViewById(R.id.edt_noteDes)
        val txt_noteDate: TextView = dialog.findViewById(R.id.txt_noteDate)

        val cv_action: CardView = dialog.findViewById(R.id.cv_action)

        val cv_cancel: CardView = dialog.findViewById(R.id.cv_cancel)

        val img_selectTime: ImageView = dialog.findViewById(R.id.img_selectTime)


        txt_noteDate.text = currentDate.toString()

        img_selectTime.setOnClickListener {
            dialogSetUpTime(txt_noteDate, requireContext())
        }

        cv_action.setOnClickListener{
            val nTitle = edt_noteTitle.text.toString()
            val nDes = edt_noteDes.text.toString()
            val nDate = txt_noteDate.text.toString()

            if (nTitle.isEmpty() || nDes.isEmpty())
            {
                Toast.makeText(context, "Error : Fulfill data", Toast.LENGTH_SHORT).show()
            }
            else {
                val note = Note(null,nTitle, nDes, nDate, false)
                DbHandler.getInstance(requireContext())?.noteDao()?.insertNote(note)

                setUpAndDisplayNotes()
                dialog.dismiss()
            }
        }

        cv_cancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    fun dialogDetailNote(note: Note) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_note_detail)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val edt_noteTitle: EditText = dialog.findViewById(R.id.edt_noteTitle)
        val edt_noteDes: EditText = dialog.findViewById(R.id.edt_noteDes)
        val txt_noteDate: TextView = dialog.findViewById(R.id.txt_noteDate)
        val txt_titleFinishStatus: TextView = dialog.findViewById(R.id.txt_titleFinishStatus)

        val cv_action: CardView = dialog.findViewById(R.id.cv_action)

        val cv_delete: CardView = dialog.findViewById(R.id.cv_delete)

        val img_selectTime: ImageView = dialog.findViewById(R.id.img_selectTime)


        img_selectTime.setOnClickListener {
            dialogSetUpTime(txt_noteDate, requireContext())
        }

        txt_noteDate.text = note.nCreatedDate
        edt_noteTitle.setText(note.nTitle)
        edt_noteDes.setText(note.nDescription)
        if (!note.nIsFinish)
        {
            txt_titleFinishStatus.text = "( Not Finish )"
        } else {
            txt_titleFinishStatus.text = "( Finished )"
        }

        cv_action.setOnClickListener{
            val nTitle = edt_noteTitle.text.toString()
            val nDes = edt_noteDes.text.toString()
            val nDate = txt_noteDate.text.toString()

            if (nTitle.isEmpty() || nDes.isEmpty())
            {
                Toast.makeText(context, "Error : Fulfill data", Toast.LENGTH_SHORT).show()
            }
            else {
                val note = Note(note.nId,nTitle, nDes, nDate, note.nIsFinish)
                DbHandler.getInstance(requireContext())?.noteDao()?.updateNote(note)

                setUpAndDisplayNotes()
                dialog.dismiss()
            }
        }

        cv_delete.setOnClickListener{
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

    fun formatMonthToStr(month: String, day: String, year: String): String? {
        var strFormatted = "Unk"

        if (month == "01") {
            strFormatted = "January"
        } else if (month == "02") {
            strFormatted = "February"
        } else if (month == "03") {
            strFormatted = "March"
        } else if (month == "04") {
            strFormatted = "April"
        } else if (month == "05") {
            strFormatted = "May"
        } else if (month == "06") {
            strFormatted = "June"
        } else if (month == "07") {
            strFormatted = "July"
        } else if (month == "08") {
            strFormatted = "August"
        } else if (month == "09") {
            strFormatted = "September"
        } else if (month == "10") {
            strFormatted = "October"
        } else if (month == "11") {
            strFormatted = "November"
        } else if (month == "12") {
            strFormatted = "December"
        }

        return "$strFormatted $day, $year"
    }


    private fun initUI(view: View) {
        txt_currentDate = view.findViewById(R.id.txt_currentDate)

        img_addNote = view.findViewById(R.id.img_addNote)

        rcv_home = view.findViewById(R.id.rcv_home)
    }
}