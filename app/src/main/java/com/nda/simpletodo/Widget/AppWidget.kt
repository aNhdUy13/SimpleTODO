package com.nda.simpletodo.Widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.nda.simpletodo.MainActivity
import com.nda.simpletodo.R

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

@SuppressLint("RemoteViewLayout")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Back to main action
    val intentActionBackToMain = Intent(context, MainActivity::class.java)
    val pendingIntentBackToMain = PendingIntent.getActivity(
        context,
        0,
        intentActionBackToMain,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )


    // Đổ data vào GridView
    // Set up the intent that starts the StackViewService, which will
    // provide the views for this collection.
    val intentDisplayData = Intent(context, WidgetService::class.java)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.app_widget)
    views.setOnClickPendingIntent(R.id.img_backToMain, pendingIntentBackToMain)
    views.setRemoteAdapter(R.id.rcv_home,intentDisplayData)



    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}