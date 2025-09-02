package es.tipolisto.tasks.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import es.tipolisto.tasks.model.Task
import es.tipolisto.tasks.model.TasksList
import es.tipolisto.tasks.util.convertDateToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Database(
    entities = [Task::class, TasksList::class],
    version = 1
)
abstract class TLDatabase : RoomDatabase(){
    abstract fun taskDao(): TaskDao
    abstract fun tasksListDao(): TasksListDao
    companion object{
        @Volatile
        private var INSTANCE: TLDatabase? = null

        fun getDatabase(context: Context): TLDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, TLDatabase::class.java, "tl_database")
                    .allowMainThreadQueries()
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                    .also { INSTANCE = it }

            }
        }
        private class AppDatabaseCallback(
            private val context: Context // Puedes necesitar el contexto si obtienes datos de, por ejemplo, JSON
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    // Opción A: Usar Coroutines y DAOs (Recomendado)
                    // Necesitarás un CoroutineScope. Puedes usar un GlobalScope
                    // o un scope específico si tu app ya tiene una gestión de scopes.
                    // Para datos iniciales, un scope simple suele ser suficiente.
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("Mensaje", "onCreate: insert metidos")
                        populateDatabase(database.taskDao(), database.tasksListDao())
                    }

                    // Opción B: Usar un ExecutorService si no quieres usar coroutines aquí
                    // Executors.newSingleThreadExecutor().execute {
                    //    populateDatabase(database.taskDao())
                    // }
                }
            }

            // También puedes sobrescribir onOpen(db: SupportSQLiteDatabase) si quieres ejecutar
            // algo cada vez que la base de datos se abre, no solo en la creación.

            suspend fun populateDatabase(taskDao: TaskDao, tasksListDao: TasksListDao) {
                // Limpia la base de datos si quieres que los datos de prueba
                // se inserten siempre frescos en cada creación (opcional)
                // taskDao.deleteAll() // Si tienes un método así en tu DAO

                // Inserta datos falsos en la tabla tasks
                val task1 = Task(
                    description = "Buy a new bathroom and garage lock",
                    isCompleted = true,
                    //date = LocalDate.now().toString()
                    //Con esto le ponemos nostros el formato que es dia/mes/año
                    date = convertDateToString(LocalDate.now()),
                    listId = 4
                )
                //date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(java.time.LocalDate.now())
                //la lista con el id 1 será la de Todas las tareas y no asignamos nada ahí xq no se puede tocar
                val task2 = Task(description = "Buy digital caliper, window rollers, cement stain, and rechargeable batteries",listId=3)
                val task3 = Task(description = "Clean the toilet", isCompleted = true,listId=4)
                val task4 = Task(description = "Buy a prepaid phone card",listId=5)

                taskDao.insertTask(task1)
                taskDao.insertTask(task2)
                taskDao.insertTask(task3)
                taskDao.insertTask(task4)

                //Tabla tasksList
                val tasksList1 = TasksList(name = "All tasks")// id 1
                val tasksList2 = TasksList(name = "Shopping at the Chinese")// id 2
                val tasksList3 = TasksList(name = "Shopping on AliExpress")// id 3
                val tasksList4 = TasksList(name = "Pending work at home")// id 4
                val tasksList5 = TasksList(name = "Things to do")// id 5
                tasksListDao.insertTasksList(tasksList1)
                tasksListDao.insertTasksList(tasksList2)
                tasksListDao.insertTasksList(tasksList3)
                tasksListDao.insertTasksList(tasksList4)
                tasksListDao.insertTasksList(tasksList5)


            }
        }

    }
}