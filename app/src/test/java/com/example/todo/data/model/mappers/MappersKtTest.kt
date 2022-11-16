package com.example.todo.data.model.mappers

import android.graphics.Color
import com.example.todo.data.model.Importance
import com.example.todo.data.model.ToDoItemRequest
import com.example.todo.domain.model.ToDoItem
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class MappersKtTest {

    companion object {
        val toDoItemRequestList = listOf(
            ToDoItemRequest(
                id = UUID.fromString("e1f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "test text",
                importance = "important",
                deadLine = null,
                done = true,
                color = "-16777216",
                createdAt = 1661424003731,
                changedAt = 1661424003731,
                lastUpdatedBy = "1"
            ),
            ToDoItemRequest(
                id = UUID.fromString("e2f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "test",
                importance = "basic",
                deadLine = 1661424103731,
                done = false,
                color = "-1",
                createdAt = 1661424003731,
                changedAt = 1661424003731,
                lastUpdatedBy = "1"
            ),
            ToDoItemRequest(
                id = UUID.fromString("e3f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "test1",
                importance = "low",
                deadLine = null,
                done = true,
                color = "-16776961",
                createdAt = 1661424003731,
                changedAt = 1661424003731,
                lastUpdatedBy = "1"
            )

        )


        val toDoItemList = listOf(
            ToDoItem(
                id = UUID.fromString("e1f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "test text",
                importance = Importance.High,
                deadLine = null,
                done = true,
                color = Color.BLACK ,
                createdAt = Date(1661424003731),
                changedAt = Date(1661424003731),
            ),
            ToDoItem(
                id = UUID.fromString("e2f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "test",
                importance = Importance.Basic,
                deadLine = Date(1661424103731),
                done = false,
                color =  Color.WHITE,
                createdAt = Date(1661424003731),
                changedAt = Date(1661424003731),
            ),
            ToDoItem(
                id = UUID.fromString("e3f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "test1",
                importance = Importance.Low,
                deadLine = null,
                done = true,
                color =  Color.BLUE,
                createdAt = Date(1661424003731),
                changedAt = Date(1661424003731),
            )
        )
    }

    @Test
    fun `assert map ToDoItem to ToDoItemRequest is passing the right data`() {
        toDoItemList.forEachIndexed { index, it ->
            val actual = it.toToDoItemRequest()

            assertEquals("ids are not the same", toDoItemRequestList[index].id, actual.id)
            assertEquals("text are not the same", toDoItemRequestList[index].text, actual.text)
            assertEquals("importance are not the same", toDoItemRequestList[index].importance, actual.importance)
            assertEquals("deadline are not the same", toDoItemRequestList[index].deadLine, actual.deadLine)
            assertEquals("done are not the same", toDoItemRequestList[index].done, actual.done)
            assertEquals("color are not the same", toDoItemRequestList[index].color, actual.color)
            assertEquals("createdAt are not the same", toDoItemRequestList[index].createdAt, actual.createdAt)
            assertEquals("changedAt are not the same", toDoItemRequestList[index].changedAt, actual.changedAt)
            assertEquals("lastUpdatedBy are not the same", toDoItemRequestList[index].lastUpdatedBy, actual.lastUpdatedBy)
        }
    }

    @Test
    fun toToDoItem() {
        toDoItemRequestList.forEachIndexed { index, it ->
            val actual = it.toToDoItem()

            assertEquals("ids are not the same", toDoItemList[index].id, actual.id)
            assertEquals("text are not the same", toDoItemList[index].text, actual.text)
            assertEquals("importance are not the same", toDoItemList[index].importance, actual.importance)
            assertEquals("deadline are not the same", toDoItemList[index].deadLine, actual.deadLine)
            assertEquals("done are not the same", toDoItemList[index].done, actual.done)
            assertEquals("color are not the same", toDoItemList[index].color, actual.color)
            assertEquals("createdAt are not the same", toDoItemList[index].createdAt, actual.createdAt)
            assertEquals("changedAt are not the same", toDoItemList[index].changedAt, actual.changedAt)
        }
    }
}