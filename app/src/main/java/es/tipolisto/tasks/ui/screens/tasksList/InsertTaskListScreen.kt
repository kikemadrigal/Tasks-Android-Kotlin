package es.tipolisto.tasks.ui.screens.tasksList

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.tipolisto.tasks.model.TasksList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTaskListScreen(
    viewModel: TasksListViewModel,
    navigationToTaskList:()->Unit
){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Insert Task list", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                navigationIcon={
                    IconButton(
                        onClick = {navigationToTaskList()}
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        }
    ){
        InsertTaskListScreenContent(it,viewModel,navigationToTaskList)
    }
}
@Composable
fun InsertTaskListScreenContent(
    it: PaddingValues,
    viewModel: TasksListViewModel,
    navigationToTaskList:()->Unit
){
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),

    ){
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                if(name.isNotEmpty()) {
                    val taskList = TasksList(name = name)
                    viewModel.insertTaskList(taskList)
                    Toast.makeText(context, "Task list created", Toast.LENGTH_LONG).show()
                    navigationToTaskList()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding( vertical = 30.dp)
        ) {
            Text("Create task list",color = Color.White)
        }
    }
}