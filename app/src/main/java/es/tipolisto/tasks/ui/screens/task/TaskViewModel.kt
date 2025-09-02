package es.tipolisto.tasks.ui.screens.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.tipolisto.tasks.local.TaskDao
import es.tipolisto.tasks.model.Task
import kotlinx.coroutines.launch

class TaskViewModel (private val dao: TaskDao) : ViewModel() {
    private var _list= MutableLiveData<List<Task>>()
    val lista: LiveData<List<Task>> get()=_list

    init {
        // Start loading data
        viewModelScope.launch {
            _list.value=dao.getAllTasks()
        }
    }

    fun getTasksByListId(listId: Long){
        _list.value=dao.getTasksByListId(listId)
    }
    fun getAllTasks(){
        _list.value=dao.getAllTasks()
    }
    fun getTaskById(id: Long): List<Task>{
       return dao.getTaskById(id)
    }


    fun insertTask(task: Task){
        viewModelScope.launch {
            dao.insertTask(task)
            _list.value=dao.getAllTasks()
        }
    }
    fun updateTask(task: Task){
        viewModelScope.launch {
            dao.updateTask(task)
            _list.value=dao.getAllTasks()
        }
    }
    fun deleteTask(task: Task){
        viewModelScope.launch {
            dao.deleteTask(task)
            _list.value=dao.getAllTasks()
        }
    }



}