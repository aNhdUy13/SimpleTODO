package com.nda.simpletodo

class UtilitiesManager {
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

}