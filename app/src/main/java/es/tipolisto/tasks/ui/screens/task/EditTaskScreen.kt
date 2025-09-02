package es.tipolisto.tasks.ui.screens.task

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.tipolisto.tasks.model.TasksList
import es.tipolisto.tasks.ui.screens.tasksList.TasksListViewModel
import es.tipolisto.tasks.util.convertDateToString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTasskScreen(
    viewModel: TaskViewModel,
    viewModelTasksList: TasksListViewModel,
    id:Long,
    navigationBack:()->Unit
){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Edit Task", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        }
    ){
        EditTaskScreenContent(it, viewModel,viewModelTasksList, id,navigationBack)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreenContent(
    it: PaddingValues,
    viewModel: TaskViewModel,
    viewModelTasksList: TasksListViewModel,
    id:Long,
    navigationBack:()->Unit
){
    val task=viewModel.getTaskById(id)[0]
    val context = LocalContext.current
    if (task==null) {
        Toast.makeText(context,"No hay norma con este id: "+id, Toast.LENGTH_LONG).show()
        navigationBack()
    }else {
        val taskList=viewModelTasksList.getTasksListByListId(task.listId)[0]
        var taskDescription by remember { mutableStateOf(task.description) }
        var taskPriority by remember { mutableStateOf(task.priority) }

        val tasksListLiveData = viewModelTasksList.lista
        var tasksLists = tasksListLiveData.value
        if (tasksLists == null) tasksLists = listOf(TasksList(name = "Select a list"))
        var showExpanded by remember { mutableStateOf(false) }
        //Por defecto, seleccionamos la 1 lista de la tabla tasksList que es "Lista sin nombre"
        var selectedList by remember { mutableStateOf(taskList) }

        var taskDate by remember { mutableStateOf(task.date) }
        val stateDatePicker=rememberDatePickerState()
        var showDatePicker by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Descriptión") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            Spacer(modifier = Modifier.fillMaxWidth().padding(5.dp))
            /*PriorityPicker(
                selectedPriority = TaskPriority.MEDIUM,
                onPrioritySelected = { },
                modifier = Modifier.fillMaxWidth()
            )*/

            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Text(text = "Priority: ")
                Text(text = taskPriority.toString())
                Slider(
                    value = taskPriority.toFloat(),
                    onValueChange = { taskPriority = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 10,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }


            Row (
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = "Date: $taskDate",
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        showDatePicker=true
                    },
                    modifier = Modifier.weight(0.2f)
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = "Date")
                }
            }

            if(showDatePicker){
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            val dateMillis = stateDatePicker.selectedDateMillis
                            taskDate=convertDateToString(LocalDate.ofEpochDay(dateMillis!! / (24 * 60 * 60 * 1000)))
                            showDatePicker = false
                        }) {
                            Text("Confirm")
                        }
                    },
                ) {
                    DatePicker(
                        state = stateDatePicker
                    )
                }
            }


            ExposedDropdownMenuBox(
                expanded = showExpanded,
                onExpandedChange = { showExpanded = !showExpanded },
                modifier = Modifier
                    .padding(20.dp)
                    .padding(bottom = 15.dp)
                    .fillMaxWidth()
            ) {
                //Vamos a decirle que nos oculte el teclado virual
                //keyboardController?.hide()
                //Esto es solo para la caja de la lista desplegable
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    value = selectedList.name,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showExpanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                //Definimos todos los elementos de este menu
                ExposedDropdownMenu(
                    expanded = showExpanded,
                    onDismissRequest = { showExpanded = false }
                ) {
                    tasksLists.forEachIndexed { index, item ->
                        //No mostramos la tarea con el id 1 que es "Todas las tareas" ni la seleccionada
                        if (item != selectedList && item.id.toInt() != 1) {
                            DropdownMenuItem(
                                text = { Text(text = item.name) },
                                onClick = {
                                    selectedList = item
                                    viewModel.getTasksByListId(item.id)
                                    showExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }//final del dropdown

            Button(
                onClick = {

                    if (taskDescription.isEmpty()) {
                        Toast.makeText(context, "Select a description", Toast.LENGTH_LONG).show()
                    } else if (selectedList.name == "Select a list") {
                        Toast.makeText(context, "Select a list", Toast.LENGTH_LONG).show()
                    } else {
                        task.description = taskDescription
                        task.priority = taskPriority
                        task.date = taskDate
                        task.listId = selectedList.id
                        viewModel.updateTask( task )
                        Toast.makeText(context, "Task updated", Toast.LENGTH_LONG).show()
                        navigationBack()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp)
            ) {
                Text("Update task", color = Color.White)
            }
        }
    }//Final su el task es nulo
}