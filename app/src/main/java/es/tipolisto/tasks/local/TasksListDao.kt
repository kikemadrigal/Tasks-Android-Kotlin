package es.tipolisto.tasks.local


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.tipolisto.tasks.model.TasksList

@Dao
interface TasksListDao {
    @Query("SELECT * FROM tasksList")
    fun getAllTasksList(): List<TasksList>

    @Query("SELECT * FROM tasksList WHERE id = :id")
    fun getTaskListById(id: Long): List<TasksList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasksList(tasksList: TasksList): Long

    @Update
    suspend fun updateTasksList(tasksList: TasksList)

    @Delete
    suspend fun deleteTasksList(tasksList: TasksList)

}