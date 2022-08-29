package com.example.todo.data.datasource.database

import androidx.room.TypeConverter
import com.example.todo.data.model.Importance
import java.util.*

class Converters {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun importanceToString(importance: Importance): String {
        return when (importance) {
            Importance.Low -> "low"
            Importance.Basic -> "No"
            Importance.High -> "high"
        }
    }

    @TypeConverter
    fun stringToImportance(strImportance: String): Importance {
        return when (strImportance) {
            "low" -> Importance.Low
            "No" -> Importance.Basic
            "high" -> Importance.High
            else -> Importance.Basic
        }
    }

}