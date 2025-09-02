package es.tipolisto.tasks.ui.screens.task

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import es.tipolisto.tasks.model.Task
import es.tipolisto.tasks.model.TasksList
import es.tipolisto.tasks.ui.screens.tasksList.TasksListViewModel
import es.tipolisto.tasks.util.convertDateToString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTasskScreen(
    viewModel: TaskViewModel,
    viewModelTasksList: TasksListViewModel,
    navigationBack:()->Unit,
){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Insert Task", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
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
        InsertTaskScreenContent(
            it,
            viewModel,
            viewModelTasksList,
            navigationBack
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTaskScreenContent(
    paddingValues: PaddingValues,
    viewModel: TaskViewModel,
    viewModelTasksList: TasksListViewModel,
    navigationBack:()->Unit
){
    var taskDescription by remember { mutableStateOf("") }
    var taskPriority by remember { mutableStateOf(5) }
    var taskDate by remember { mutableStateOf("") }
    val context = LocalContext.current

    val tasksListLiveData = viewModelTasksList.lista
    var tasksList = tasksListLiveData.value
    if(tasksList == null)tasksList=listOf(TasksList(name = "Select a list"))
    var showExpanded by remember { mutableStateOf(false) }
    //Por defecto, seleccionamos la 1 lista de la tabla tasksList que es "Lista sin nombre"
    var selectedList by remember { mutableStateOf(TasksList(name = "Select a list"))}

    val stateDatePicker=rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
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
            onPrioritySelected = {  },
            modifier = Modifier.fillMaxWidth()
        )*/

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
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
            verticalAlignment = Alignment.CenterVertically
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
                .padding( 20.dp)
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
                tasksList.forEachIndexed { index, item ->
                    if(item!=selectedList && item.id.toInt()!=1){
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
                if(taskDate.isEmpty())taskDate=convertDateToString(LocalDate.now())
                if(taskDescription.isEmpty()) {
                   Toast.makeText(context, "Select a description", Toast.LENGTH_LONG).show()
                }else if(selectedList.name=="Select a list"){
                    Toast.makeText(context, "Select a list", Toast.LENGTH_LONG).show()
                }else{
                    val task = Task(description = taskDescription, priority = taskPriority, date = taskDate, listId = selectedList.id)
                    viewModel.insertTask(task)
                    Toast.makeText(context, "Task created", Toast.LENGTH_LONG).show()
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
            Text("Create task",color = Color.White)
        }
    }
}