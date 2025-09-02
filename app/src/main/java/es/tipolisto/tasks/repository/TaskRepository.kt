package es.tipolisto.tasks.repository


import es.tipolisto.tasks.local.TaskDao
import es.tipolisto.tasks.local.TasksListDao
import es.tipolisto.tasks.model.Task

class TaskRepository(private val taskDao: TaskDao, private val tasksListDao: TasksListDao) {

    fun getAllTasks(): List<Task>{
        return taskDao.getAllTasks()
    }
    


}