package es.tipolisto.tasks.util

fun trimString(value:String, length:Int):String{
    return if(value.length>length)value.substring(0,length)+"..."
    else value
}