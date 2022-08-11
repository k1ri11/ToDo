package com.example.todo.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class ToDoItemRequest(
    val id: UUID,
    var text: String,
    var importance: String,
    @SerializedName("deadline")
    val deadLine: Long? = null,
    var done: Boolean,
    var color: String? = null,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("changed_at")
    var changedAt: Long,
    @SerializedName("last_updated_by")
    var lastUpdatedBy: String,
)