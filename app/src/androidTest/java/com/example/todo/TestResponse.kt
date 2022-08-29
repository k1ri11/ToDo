package com.example.todo

import android.graphics.Color
import com.example.todo.data.model.Importance
import com.example.todo.domain.model.ToDoItem
import java.util.*

class TestResponse {
    companion object{
        const val successResp = "{\n" +
                "    \"revision\": 1890,\n" +
                "    \"list\": [\n" +
                "        {\n" +
                "            \"done\": false,\n" +
                "            \"changed_at\": 1661253969520,\n" +
                "            \"text\": \"56789\",\n" +
                "            \"created_at\": 1661143529821,\n" +
                "            \"last_updated_by\": \"1\",\n" +
                "            \"importance\": \"basic\",\n" +
                "            \"color\": \"#000000\",\n" +
                "            \"id\": \"e1f90023-0755-4ae7-9cd4-809af52b988c\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"status\": \"ok\"\n" +
                "}"

        val expectedSuccessResp = listOf(
            ToDoItem(
                id = UUID.fromString("e1f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "56789",
                importance = Importance.Basic,
                deadLine = null,
                done = false,
                color = Color.parseColor("#000000"),
                createdAt = Date(1661143529821),
                changedAt = Date(1661253969520),
            )
        )

    }
}