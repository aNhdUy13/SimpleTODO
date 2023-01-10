package com.nda.simpletodo.Widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.nda.simpletodo.R
import com.nda.simpletodo.models.Note

class WidgetService: RemoteViewsService() {
    lateinit var context: Context
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return WidgetRemoteViewFactory(this.applicationContext, p0)
    }
}

class WidgetRemoteViewFactory(applicationContext: Context?, p0: Intent?) : RemoteViewsService.RemoteViewsFactory {

    var context = applicationContext
    var noteList = arrayListOf<Note>()

    override fun onCreate() {
        val dbBroadcast: SQLiteDatabase = context!!.openOrCreateDatabase("simpTODO.db", 0, null)

        val cursor: Cursor = dbBroadcast.rawQuery("SELECT * FROM noteTable",null)


        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val des = cursor.getString(2)
            val date = cursor.getString(3)
            val isFinished:Boolean
            val category = cursor.getString(5)
            noteList.add(Note(id, title, des, date, false, category))
        }
    }

    @SuppressLint("RemoteViewLayout")
    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(context?.packageName, R.layout.item_note_widget)

        rv.setTextViewText(R.id.txt_title, noteList[p0].nTitle)
        rv.setTextViewText(R.id.txt_createdDate, noteList[p0].nCreatedDate)

        return rv
    }

    override fun onDataSetChanged() {

    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return 48
    }


    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        TODO("Not yet implemented")
    }

}
