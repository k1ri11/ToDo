package com.example.todo.data.network

import com.example.todo.data.lists.ToDoListReq
import com.example.todo.data.item.ToDoSingleReq
import com.example.todo.data.item.ToDoSingleUpd
import com.example.todo.data.lists.ToDoListUpd
import com.example.todo.utils.Constants
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ToDoApi {

    @GET("/todobackend/list")
    suspend fun getAllTasks(): Response<ToDoListReq>


    @PATCH("/todobackend/list")
    suspend fun saveTasksList(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Body tasksList: ToDoListUpd,
    ): Response<ToDoListReq>


    @POST("/todobackend/list")
    suspend fun addTask(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Body task: ToDoSingleUpd,
    )


    @PUT("/todobackend/list/{id}")
    suspend fun updateTask(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Path("id")
        id: UUID,
        @Body task: ToDoSingleUpd,
    )


    @GET("/todobackend/list/{id}")
    suspend fun getTask(
        @Path("id")
        id: UUID,
    ): Response<ToDoSingleReq>


    @DELETE("/todobackend/list/{id}")
    suspend fun deleteTask(
        @Header(
            "X-Last-Known-Revision"
        )
        revision: Int,
        @Path("id")
        id: UUID,
    ): Response<ToDoSingleReq>


}