package es.tipolisto.tasks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.tipolisto.tasks.ui.screens.task.EditTasskScreen
import es.tipolisto.tasks.ui.screens.task.InsertTasskScreen
import es.tipolisto.tasks.ui.screens.task.TaskScreen
import es.tipolisto.tasks.ui.screens.task.TaskViewModel
import es.tipolisto.tasks.ui.screens.tasksList.EditTaskListScreen
import es.tipolisto.tasks.ui.screens.tasksList.InsertTaskListScreen
import es.tipolisto.tasks.ui.screens.tasksList.TaskListScreen

import es.tipolisto.tasks.ui.screens.tasksList.TasksListViewModel


@Composable
fun NavManager(
    viewModelTask: TaskViewModel,
    viewModelTasksList: TasksListViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "tasks"
    ) {
        composable("tasks") {
            TaskScreen(
                navController,
                viewModelTask,
                viewModelTasksList,
                { navidationBackTask(navController) },
                {
                    navController.navigate("tasksList")
                },
                {
                    navController.navigate("insertTask")
                }
            )
        }
        composable("insertTask") {
            InsertTasskScreen(
                viewModelTask,
                viewModelTasksList,
                { navidationBackTask(navController) }
            )
        }
        composable(
            route="editTask/{id}",
            arguments=listOf(
                navArgument(name="id"){NavType.StringType}
            )
        ){
            val id:String?=it.arguments?.getString( "id")
            requireNotNull(id, { "No puede ser nulo" })
            EditTasskScreen(
                viewModelTask,
                viewModelTasksList,
                id.toLong(),
                { navidationBackTask(navController) }
            )
        }




        //InsertTaskListScreen
        composable("tasksList") {
            TaskListScreen(
                navController,
                viewModelTasksList,
                {navidationBackTask(navController)},
                {
                    navController.navigate("tasksList")
                },
                {
                    navController.navigate("insertTaskLiust")
                }
            )
        }
        composable("insertTaskLiust") {
            InsertTaskListScreen(
                viewModelTasksList,
                {navController.popBackStack()}
            )
        }
        composable(
            route="editTaskList/{id}",
            arguments=listOf(
                navArgument(name="id"){NavType.StringType}
            )
        ){
            val id:String?=it.arguments?.getString( "id")
            requireNotNull(id, { "No puede ser nulo" })
            EditTaskListScreen(
                viewModelTasksList ,
                id.toLong(),
                {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun navidationBackTask(navController: NavController){
    navController.navigate("tasks") {
        popUpTo(navController.graph.startDestinationRoute!!) { // !! es seguro si el grafo tiene un startDestination
            inclusive = true
        }
        // Opcional: para asegurar que D no se añada varias veces si ya está en la pila
        launchSingleTop = true
    }
}