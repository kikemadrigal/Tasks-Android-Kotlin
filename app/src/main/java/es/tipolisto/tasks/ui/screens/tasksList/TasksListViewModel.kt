package es.tipolisto.tasks.ui.screens.tasksList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.tipolisto.tasks.local.TasksListDao
import es.tipolisto.tasks.model.TasksList
import kotlinx.coroutines.launch

class TasksListViewModel (private val tasksListDao: TasksListDao) : ViewModel() {
    private var _list= MutableLiveData<List<TasksList>>()
    val lista: LiveData<List<TasksList>> get()=_list

    init {
        // Start loading data
        viewModelScope.launch {
            _list.value=tasksListDao.getAllTasksList()

        }
    }
    fun getTaskListById(id: Long): List<TasksList>{
        return tasksListDao.getTaskListById(id)
    }

    fun getAllTasksList(){
        _list.value=tasksListDao.getAllTasksList()
    }
    fun getTasksListByListId(listId: Long): List<TasksList>{
        return tasksListDao.getTaskListById(listId)
    }

    fun insertTaskList(taskList: TasksList){
        viewModelScope.launch {
            tasksListDao.insertTasksList(taskList)
            _list.value=tasksListDao.getAllTasksList()
        }
    }
    fun updateTasksList(tasksList: TasksList){
        viewModelScope.launch {
            tasksListDao.updateTasksList(tasksList)
            _list.value=tasksListDao.getAllTasksList()
        }
    }
    fun deleteTaskList(tasksList: TasksList){
        viewModelScope.launch {
            tasksListDao.deleteTasksList(tasksList)
            _list.value=tasksListDao.getAllTasksList()
        }
    }


}