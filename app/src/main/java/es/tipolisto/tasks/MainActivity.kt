package es.tipolisto.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import es.tipolisto.tasks.local.TLDatabase
import es.tipolisto.tasks.ui.navigation.NavManager
import es.tipolisto.tasks.ui.screens.task.TaskViewModel
import es.tipolisto.tasks.ui.screens.tasksList.TasksListViewModel
import es.tipolisto.tasks.ui.theme.CatalogerTheme

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ViewModelConstructorInComposable",)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {

            //val viewModelTask: TaskViewModel by viewModels()


        }
        setContent {
            CatalogerTheme {
                Surface (modifier = Modifier.fillMaxSize()){


                    //val database by lazy { TLDatabase.getDatabase(this) }
                    //val database = Room.databaseBuilder(applicationContext,TLDatabase::class.java,"tl_database").build()
                    val database= TLDatabase.getDatabase(applicationContext)
                    val taskDao = database.taskDao()
                    val tasksListDao = database.tasksListDao()
                    val viewModelTask = TaskViewModel(taskDao)
                    val viewModelTasksList = TasksListViewModel(tasksListDao)
                    NavManager(
                        viewModelTask,
                        viewModelTasksList
                    )

                }
            }
        }
    }
}
