package com.example.todo.data.datasource.network

import com.example.todo.data.model.lists.TasksListRequest
import com.example.todo.data.model.singletask.SingleTaskRequest
import com.example.todo.data.model.singletask.SingleTaskUpdate
import com.example.todo.data.model.lists.TasksListUpdate
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ToDoApi {

    @GET("/todobackend/list")
    suspend fun getAllTasks(): Response<TasksListRequest>


    @PATCH("/todobackend/list")
    suspend fun saveTasksList(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Body tasksList: TasksListUpdate,
    ): Response<TasksListRequest>


    @POST("/todobackend/list")
    suspend fun addTask(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Body task: SingleTaskUpdate,
    )


    @PUT("/todobackend/list/{id}")
    suspend fun updateTask(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Path("id")
        id: UUID,
        @Body task: SingleTaskUpdate,
    )


    @GET("/todobackend/list/{id}")
    suspend fun getTask(
        @Path("id")
        id: UUID,
    ): Response<SingleTaskRequest>


    @DELETE("/todobackend/list/{id}")
    suspend fun deleteTask(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Path("id")
        id: UUID,
    ): Response<SingleTaskRequest>


}