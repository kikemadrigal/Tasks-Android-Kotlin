package es.tipolisto.tasks.ui.screens.task

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import es.tipolisto.tasks.model.Task
import es.tipolisto.tasks.ui.components.CircularProgressBar
import es.tipolisto.tasks.ui.components.MyAlertDialog
import es.tipolisto.tasks.ui.screens.tasksList.TasksListViewModel
import es.tipolisto.tasks.util.trimString
import kotlin.collections.sortedByDescending


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(
    navController: NavHostController,
    viewModel: TaskViewModel,
    taskListViewModel: TasksListViewModel,
    navigationBack:()->Unit,
    navigationOnClickTaskLists:()->Unit,
    navigationToInsertTask:()->Unit
) {
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Tasks", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {navigationOnClickTaskLists()}) {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = "Tasks List"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {navigationToInsertTask()},
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ){
        ViewContent(
            it,
            navController,
            viewModel,
            taskListViewModel,
            navigationBack
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewContent(
    it: PaddingValues,
    navController: NavHostController,
    viewModel: TaskViewModel,
    taskListViewModel: TasksListViewModel,
    navigationBack:()->Unit
) {
    val context = LocalContext.current
    //val activity= LocalActivity.current as Activity
    val tasksLiveData = viewModel.lista
    val tasks = tasksLiveData.value
    val tasksListLiveData = taskListViewModel.lista
    val tasksList = tasksListLiveData.value

    if( tasksList!=null && tasksList.isNotEmpty()) {
        var showExpanded by remember { mutableStateOf(false) }
        //Por defecto, seleccionamos la 1 lista de la tabla tasksList que es "Lista sin nombre"
        var selectedList by remember { mutableStateOf(tasksList.get(0)) }

        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {

            //Ahora vamos a mostrar en una lista deplegable de las listas de tareas
            ExposedDropdownMenuBox(
                expanded = showExpanded,
                onExpandedChange = { showExpanded = !showExpanded },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //Vamos a decirle que nos oculte el teclado virual
                //keyboardController?.hide()
                //Esto es solo para la caja de la lista desplegable
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    value = selectedList.name,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showExpanded) },
                    //colors = ExposedDropdownMenuDefaults.textFieldColors()
                    colors = TextFieldDefaults.colors(
                        // <-- AQUÍ LA PERSONALIZACIÓN
                        // Colores cuando el TextField está HABILITADO
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f), // Común para deshabilitado

                        focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer, // Color del contenedor con foco
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,  // Color del contenedor sin foco
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                            alpha = 0.12f
                        ),

                        cursorColor = MaterialTheme.colorScheme.primary, // Color del cursor

                        focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Color del indicador inferior con foco
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant, // Color del indicador inferior sin foco
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),

                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),

                        )
                )
                //Definimos todos los elementos de este menu
                ExposedDropdownMenu(
                    expanded = showExpanded,
                    onDismissRequest = { showExpanded = false }
                ) {
                    tasksList.forEachIndexed { index, item ->
                        if (item != selectedList) {
                            DropdownMenuItem(
                                text = { Text(text = item.name) },
                                onClick = {
                                    selectedList = item
                                    //Si el item es Todas las tareas vemos todas las tareas
                                    if (item.id.toInt() == 1) {
                                        viewModel.getAllTasks()
                                    } else {
                                        viewModel.getTasksByListId(item.id)
                                    }
                                    showExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }//final del dropdown

            if (tasks != null) {
                val completed=tasks.filter { it.isCompleted }.size
                Row (
                    modifier = Modifier.fillMaxWidth().padding( 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text="Completed tasks: ${completed} de ${tasks.size}")
                    CircularProgressBar(percentage =completed.toFloat()/tasks.size , number = 100)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        tasks.sortedByDescending {
                            it.priority
                        }
                    ) { task->
                        val description=trimString(task.description,100)
                        var taskPriority by remember { mutableStateOf(task.priority) }
                        var isCompleted by remember { mutableStateOf(task.isCompleted) }
                        var isShowinDialogDeleted by remember { mutableStateOf(false) }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .clickable{
                                    navController.navigate("editTask" + "/${task.id}")
                                },
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shadowElevation = 40.dp,
                            shape = RectangleShape
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(
                                    modifier = Modifier
                                        .weight(0.2f)
                                        .padding(horizontal = 24.dp, vertical = 6.dp)
                                ) {
                                    Checkbox(
                                        checked = isCompleted,
                                        onCheckedChange = {
                                            isCompleted = !isCompleted
                                            val updateTask= Task(
                                                task.id,
                                                description,
                                                task.priority,
                                                isCompleted,
                                                task.date,
                                                task.listId
                                            )
                                            viewModel.updateTask(updateTask)
                                            navigationBack()
                                        }
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 24.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        fontSize = 20.sp,
                                        text = description
                                    )
                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ){
                                        //Text(text = "Priority: ")
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(if (taskPriority>8) Color.Red else if (taskPriority>5) Color.Magenta else if (taskPriority>2) Color.Green else Color.Transparent)
                                                .border(2.dp, Color.Black, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = taskPriority.toString(),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                    Text(text = task.date)
                                }
                                IconButton(
                                    onClick = {
                                        isShowinDialogDeleted=true
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }//Final del row
                            if (isShowinDialogDeleted) {
                                MyAlertDialog(
                                    onDismiss = {
                                        isShowinDialogDeleted = false
                                    },
                                    onConfirm = {
                                        val task = viewModel.getTaskById(task.id)
                                        viewModel.deleteTask(task[0])
                                        viewModel.getAllTasks()
                                        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT)
                                            .show()
                                        navigationBack()
                                    }
                                )
                            }

                        }//FInal del card
                    }
                }
            }//final de si las tareas es null
        }//Final del column general
    }//Final de si la lista de listas es nula
    else{
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Cierra el programa y vuelve a abrirlo, listas creadas")
        }
    }


}




