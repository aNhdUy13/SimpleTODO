package com.nda.simpletodo.ui.searchNotes

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nda.simpletodo.DbHandler
import com.nda.simpletodo.R
import com.nda.simpletodo.models.Note
import kotlinx.android.synthetic.main.item_note.view.*

class AdapterSearchNote(private var noteList: List<com.nda.simpletodo.models.Note>, mContext: SearchNoteFragment)
    : RecyclerView.Adapter<AdapterSearchNote.HolderNote>() {

    val mContext: SearchNoteFragment = mContext

//    // Action on click
//    private lateinit var mListener: onCustomItemClickListener
//
//    interface onCustomItemClickListener{
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(clickListener: onCustomItemClickListener){
//        mListener = clickListener
//    }

    //
    fun setChangedDataTable(noteList: List<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }

    //
    inner class HolderNote(foodItemView: View)
        :RecyclerView.ViewHolder(foodItemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderNote {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent,false)

        return HolderNote(view)
    }

    override fun onBindViewHolder(holder: HolderNote, position: Int) {
        holder.itemView.apply {
            val note = noteList[position]
            
            txt_title.text = note.nTitle
            txt_createdDate.text = note.nCreatedDate

            ll_itemNote.setOnClickListener{
                mContext.dialogDetailNote(note)
            }


            if (note.nIsFinish)
            {
                cb_note.isChecked = true
                txt_title.paintFlags = txt_title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                txt_title.setTypeface(txt_title.typeface, Typeface.ITALIC);
            }
            else {
                cb_note.isChecked = false
                txt_title.setTypeface(txt_title.typeface, Typeface.BOLD);
            }

            var noteUpdate: Note

            cb_note.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    noteUpdate = Note(note.nId,note.nTitle, note.nDescription, note.nCreatedDate,true, note.nCategory)
                }
                else {
                    noteUpdate = Note(note.nId,note.nTitle, note.nDescription, note.nCreatedDate, false, note.nCategory)
                }
                DbHandler.getInstance(context)?.noteDao()?.updateNote(noteUpdate)

            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}