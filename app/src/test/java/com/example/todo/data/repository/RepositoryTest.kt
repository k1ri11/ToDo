package com.example.todo.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.example.todo.data.datasource.database.ToDoDao
import com.example.todo.data.datasource.network.ToDoApi
import com.example.todo.data.model.lists.TasksListRequest
import com.example.todo.data.model.mappers.toToDoItemRequest
import com.example.todo.data.model.singletask.SingleTaskRequest
import com.example.todo.data.model.singletask.SingleTaskUpdate
import com.example.todo.data.repository.TestResponse.Companion.TasksList
import com.example.todo.data.repository.TestResponse.Companion.singleTaskRequest
import com.example.todo.data.repository.TestResponse.Companion.taskListRequest
import com.example.todo.ui.view.NetworkUtils
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import retrofit2.Response


@RunWith(RobolectricTestRunner::class)
class RepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: Repository
    private val dao = mock<ToDoDao>()
    private val api = mock<ToDoApi>()
    private val networkUtils = mock<NetworkUtils>()

    private val responseBodyError = ResponseBody.create(null, "1111")
    private val fakeSingleRespError400: Response<SingleTaskRequest> = Response.error(400, responseBodyError)
    private val fakeSingleRespError401 = Response.error<SingleTaskRequest>(401, responseBodyError)
    private val fakeSingleRespError404 = Response.error<SingleTaskRequest>(404, responseBodyError)
    private val fakeSingleRespError500 = Response.error<SingleTaskRequest>(500, responseBodyError)

    private val fakeSingleRespSuccess = Response.success(200, singleTaskRequest)
    private val fakeRespSuccess = Response.success(200, taskListRequest)

    private val fakeRespError400 = Response.error<TasksListRequest>(400, responseBodyError)
    private val fakeRespError401 = Response.error<TasksListRequest>(401, responseBodyError)
    private val fakeRespError404 = Response.error<TasksListRequest>(404, responseBodyError)
    private val fakeRespError500 = Response.error<TasksListRequest>(500, responseBodyError)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val data: LiveData<Boolean> = MutableLiveData(false)
        Mockito.`when`(networkUtils.getNetworkLiveData()).thenReturn(data)

        repository = Repository(api, dao, context, networkUtils)

    }

    @Test
    fun `getAllTasks with internet connection and successful response`() {

        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespSuccess)

            repository.getAllTasks()

            verify(api, times(1)).getAllTasks()
            verify(dao, times(1)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
        }
    }

    @Test
    fun `getAllTasks with internet connection and error 400 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespError400)
            repository.allTasks.observeForever {
                if (it.message == "Неправильно сформирован запрос") isFoundError = true
            }
            repository.getAllTasks()

            verify(api, times(1)).getAllTasks()
            verify(dao, times(0)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
            assertEquals(true, isFoundError)

        }
    }

    @Test
    fun `getAllTasks with internet connection and error 401 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespError401)
            repository.allTasks.observeForever {
                if (it.message == "Неверная авторизация") isFoundError = true
            }
            repository.getAllTasks()

            verify(api, times(1)).getAllTasks()
            verify(dao, times(0)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getAllTasks with internet connection and error 404 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespError404)
            repository.allTasks.observeForever {
                if (it.message == "Такой элемент не найден на сервере") isFoundError = true
            }
            repository.getAllTasks()

            verify(api, times(1)).getAllTasks()
            verify(dao, times(0)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getAllTasks with internet connection and error 500 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespError500)
            repository.allTasks.observeForever {
                if (it.message == "Неопознанная ошибка сервера") isFoundError = true
            }
            repository.getAllTasks()

            verify(api, times(1)).getAllTasks()
            verify(dao, times(0)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getAllTasks without internet connection`() {
        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(false)

        runBlocking {
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespSuccess)
            repository.allTasks.observeForever {
                if (it.message == "Нет интернет соединения") isFoundError = true
            }

            repository.getAllTasks()

            verify(api, times(0)).getAllTasks()
            verify(dao, times(0)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getTask with internet connection and success response`() {

        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getTask(id = TasksList[0].id))
                .thenReturn(fakeSingleRespSuccess)

            repository.getTask(id = TasksList[0].id)

            verify(api, times(1)).getTask(id = TasksList[0].id)
            verify(dao, times(1)).updateTask(TasksList[0])
            verify(dao, times(1)).getTask(TasksList[0].id)
        }
    }

    @Test
    fun `getTask with internet connection and error 400 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getTask(id = TasksList[0].id)).thenReturn(fakeSingleRespError400)

            repository.singleTask.observeForever {
                if (it.message == "Неправильно сформирован запрос") isFoundError = true
            }
            repository.getTask(id = TasksList[0].id)

            verify(api, times(1)).getTask(id = TasksList[0].id)
            verify(dao, times(0)).updateTask(TasksList[0])
            verify(dao, times(1)).getTask(TasksList[0].id)
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getTask with internet connection and error 401 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getTask(id = TasksList[0].id)).thenReturn(fakeSingleRespError401)

            repository.singleTask.observeForever {
                if (it.message == "Неверная авторизация") isFoundError = true
            }
            repository.getTask(id = TasksList[0].id)

            verify(api, times(1)).getTask(id = TasksList[0].id)
            verify(dao, times(0)).updateTask(TasksList[0])
            verify(dao, times(1)).getTask(TasksList[0].id)
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getTask with internet connection and error 404 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getTask(id = TasksList[0].id)).thenReturn(fakeSingleRespError404)

            repository.singleTask.observeForever {
                if (it.message == "Такой элемент не найден на сервере") isFoundError = true
            }
            repository.getTask(id = TasksList[0].id)

            verify(api, times(1)).getTask(id = TasksList[0].id)
            verify(dao, times(0)).updateTask(TasksList[0])
            verify(dao, times(1)).getTask(TasksList[0].id)
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getTask with internet connection and error 500 response`() {

        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getTask(id = TasksList[0].id)).thenReturn(fakeSingleRespError500)

            repository.singleTask.observeForever {
                if (it.message == "Неопознанная ошибка сервера") isFoundError = true
            }
            repository.getTask(id = TasksList[0].id)

            verify(api, times(1)).getTask(id = TasksList[0].id)
            verify(dao, times(0)).updateTask(TasksList[0])
            verify(dao, times(1)).getTask(TasksList[0].id)
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `getTask without internet connection`() {
        var isFoundError = false
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(false)

        runBlocking {
            repository.singleTask.observeForever {
                if (it.message == "Нет интернет соединения") isFoundError = true
            }
            repository.getTask(id = TasksList[0].id)

            verify(api, times(0)).getTask(id = TasksList[0].id)
            verify(dao, times(0)).updateTask(TasksList[0])
            verify(dao, times(1)).getTask(TasksList[0].id)
            assertEquals(true, isFoundError)
        }
    }

    @Test
    fun `addTask test with internet connection`() {
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {

            Mockito.`when`(dao.getAllTasks()).thenReturn(TasksList)
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespSuccess)

            repository.addTask(TasksList[0])

            verify(dao, times(1)).addTask(TasksList[0])
            verify(api, times(1)).addTask(0,
                SingleTaskUpdate(element = TasksList[0].toToDoItemRequest()))

            //getAllTasks
            verify(api, times(1)).getAllTasks()
            verify(dao, times(1)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
        }
    }

    @Test
    fun `addTask test without internet connection`() {
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(false)

        runBlocking {

            repository.addTask(TasksList[0])

            verify(dao, times(1)).addTask(TasksList[0])
            verify(api, times(0)).addTask(0,
                SingleTaskUpdate(element = TasksList[0].toToDoItemRequest()))
        }
    }

    @Test
    fun `updateTask test with internet connection`() {
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespSuccess)
            repository.updateTask(TasksList[0])

            verify(dao, times(1)).updateTask(TasksList[0])
            verify(api, times(1)).updateTask(revision = 0,
                id = TasksList[0].id,
                SingleTaskUpdate(element = TasksList[0].toToDoItemRequest()))

            //getAllTasks
            verify(api, times(1)).getAllTasks()
            verify(dao, times(1)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
        }
    }

    @Test
    fun `updateTask test without internet connection`() {
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(false)

        runBlocking {
            repository.updateTask(TasksList[0])

            verify(dao, times(1)).updateTask(TasksList[0])
            verify(api, times(0)).updateTask(revision = 0,
                id = TasksList[0].id,
                SingleTaskUpdate(element = TasksList[0].toToDoItemRequest()))
        }
    }

    @Test
    fun `deleteTask test with internet connection and successful response`() {


        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.deleteTask(id = TasksList[0].id, revision = 0))
                .thenReturn(fakeSingleRespSuccess)
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespSuccess)

            repository.deleteTask(TasksList[0])

            verify(dao, times(1)).deleteTask(TasksList[0])
            verify(api, times(1)).deleteTask(0, TasksList[0].id)
            //getAllTasks
            verify(api, times(1)).getAllTasks()
            verify(dao, times(1)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()
        }
    }

    @Test
    fun `deleteTask test with internet connection and error response`() {

        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)

        runBlocking {
            Mockito.`when`(api.deleteTask(id = TasksList[0].id, revision = 0))
                .thenReturn(fakeSingleRespError400)
            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespSuccess)

            repository.deleteTask(TasksList[0])

            verify(dao, times(1)).deleteTask(TasksList[0])
            verify(api, times(1)).deleteTask(0, TasksList[0].id)
            //getAllTasks
            verify(api, times(1)).getAllTasks()
            verify(dao, times(1)).saveTasksList(TasksList)
            verify(dao, times(1)).getAllTasks()
            verify(dao, times(1)).getFilteredTasks()

        }
    }

    @Test
    fun `deleteTask test without internet connection`() {
        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(false)

        runBlocking {
            repository.deleteTask(TasksList[0])

            verify(dao, times(1)).deleteTask(TasksList[0])
            verify(api, times(0)).deleteTask(0, TasksList[0].id)
        }
    }


//    @Test
//    fun `should change item done`() {
//        Mockito.`when`(networkUtils.hasInternetConnection()).thenReturn(true)
//
//        runBlocking {
//            Mockito.`when`(api.getAllTasks()).thenReturn(fakeRespSuccess)
//            Mockito.`when`(dao.getAllTasks()).thenReturn(TasksList)
//            Mockito.`when`(dao.getFilteredTasks()).thenReturn(TasksList)
//            repository.getAllTasks()
//        }
//        repository.changeItemDone(TasksList[0])
//
//        assertEquals(TasksList[0].done, repository.allTasks.value!!.data!![0].done)
//    }
}