package com.example.todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.todo.data.model.Importance
import java.util.*

@Entity(
    tableName = "todo_items"
)
data class ToDoItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "room_id")
    val roomId: Int? = null,
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "text")
    var text: String,
    @ColumnInfo(name = "importance")
    var importance: Importance,
    @ColumnInfo(name = "deadline")
    var deadLine: Date? = null,
    @ColumnInfo(name = "done")
    var done: Boolean,
    @ColumnInfo(name = "color")
    var color: Int? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "changed_at")
    var changedAt: Date,
)


