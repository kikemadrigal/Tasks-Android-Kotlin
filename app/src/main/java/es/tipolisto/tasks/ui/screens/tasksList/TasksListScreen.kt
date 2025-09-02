package es.tipolisto.tasks.ui.screens.tasksList

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.tipolisto.tasks.ui.components.MyAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navController: NavHostController,
    viewModel: TasksListViewModel,
    navigationBack:()->Unit,
    navigationToTaskList:()->Unit,
    navigationToInsertTaskList:()->Unit
) {
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Tasks List", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                navigationIcon={
                    IconButton(
                        onClick = {navigationBack()}
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint =MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {navigationToInsertTaskList()},
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Crear")
            }
        }
    ){
        TaskListViewContent(it,navController,viewModel,navigationToTaskList)
    }
}

@Composable
fun TaskListViewContent(
    paddingValues: PaddingValues,
    navController:NavHostController,
    viewModel: TasksListViewModel,
    navigationToTaskList:()->Unit
){
    val context = LocalContext.current
    val tasksListLiveData = viewModel.lista
    var tasksList = tasksListLiveData.value
    Log.d("Mensaje", "tasksList es "+tasksList.toString())
    var isShowinDialogDeleted by remember { mutableStateOf(false) }
    if (tasksList != null) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(top = 10.dp)) {
            items(
                tasksList.sortedBy { task->
                    task.name
                }
            ) {

                if(it.id.toInt()!=1){
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable{
                                navController.navigate("editTaskList" + "/${it.id}")
                            },
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = 40.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(
                                modifier = Modifier.weight(1f)
                                    .padding(horizontal = 24.dp, vertical = 6.dp)
                            ) {
                                Text(text = it.name)

                            }
                            IconButton(
                                onClick = {
                                    isShowinDialogDeleted=true
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }//FInal del surface
                    if (isShowinDialogDeleted) {
                        MyAlertDialog(
                            onDismiss = {
                                isShowinDialogDeleted = false
                            },
                            onConfirm = {
                                val tasksList = viewModel.getTasksListByListId(it.id)
                                viewModel.deleteTaskList(tasksList[0])
                                viewModel.getAllTasksList()
                                Toast.makeText(context, "Task list deleted", Toast.LENGTH_SHORT).show()
                                navigationToTaskList()
                            }
                        )
                    }
                }//final del if si no es el id 1
            }
        }
    }//final de si las tareas es null
}