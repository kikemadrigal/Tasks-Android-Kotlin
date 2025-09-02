package es.tipolisto.tasks.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasksList")
data class TasksList(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
    var name:String=""
)
