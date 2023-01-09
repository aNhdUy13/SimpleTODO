package com.nda.simpletodo.ui.searchNotes

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
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nda.simpletodo.DbHandler
import com.nda.simpletodo.R
import com.nda.simpletodo.models.Note
import kotlinx.android.synthetic.main.dialog_note.cv_action
import kotlinx.android.synthetic.main.dialog_note.edt_noteDes
import kotlinx.android.synthetic.main.dialog_note.edt_noteTitle
import kotlinx.android.synthetic.main.dialog_note.img_selectTime
import kotlinx.android.synthetic.main.dialog_note.txt_noteDate
import kotlinx.android.synthetic.main.dialog_note_detail.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.ArrayList

class SearchNoteFragment : Fragment() {
    var adapterSearchNote:AdapterSearchNote?  = null
    var noteList = arrayListOf<Note>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAndDisplayNotes()

        init()
    }

    private fun init() {
        searchView_notes?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                rcv_notes?.scrollToPosition(0)
                searchData(query)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                rcv_notes?.scrollToPosition(0)

                searchData(newText)

                return false
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            setUpAndDisplayNotes()

            swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun searchData(typingData: String) {
        val tempListData = java.util.ArrayList<Note>()

        for(note in noteList) {
            if (note.nTitle.lowercase().contains(typingData))
            {
                tempListData.add(note)
            }
        }
        adapterSearchNote?.setChangedDataTable(tempListData)
    }

    private fun setUpAndDisplayNotes() {
        noteList = DbHandler.getInstance(requireContext())?.noteDao()?.getAllNote() as ArrayList<Note>

        adapterSearchNote = AdapterSearchNote(noteList, this)
        rcv_notes?.adapter = adapterSearchNote
        rcv_notes?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapterSearchNote?.setChangedDataTable(noteList)

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
                val note = Note(note.nId,nTitle, nDes, nDate, note.nIsFinish, nCategory)
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