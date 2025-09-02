package es.tipolisto.tasks.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import es.tipolisto.tasks.util.convertDateToString
import java.time.LocalDate

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var description: String = "",
    var priority: Int = 5,
    var isCompleted: Boolean = false,
    var date: String = convertDateToString(LocalDate.now()),
    //@ForeignKey(entity = TasksList::class, parentColumns = ["id"], childColumns = ["listId"])
    var listId: Long = 1
)


