package es.tipolisto.tasks.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun convertDateToString(date: LocalDate):String{
    //return DateTimeFormatter.ofPattern("yyyyMMdd").format(date)
    return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date)
}

fun covertStringToDate(date:String):LocalDate{
    return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}
