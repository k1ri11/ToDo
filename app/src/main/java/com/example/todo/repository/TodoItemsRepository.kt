package com.example.todo.repository

import com.example.todo.model.Importance
import com.example.todo.model.ToDoItem
import kotlinx.coroutines.delay
import java.util.*

class TodoItemsRepository {
    var tasks = mutableListOf(
        ToDoItem(
            UUID.randomUUID(),
            "Купить что-то",
            Importance.Low,
            Date(System.currentTimeMillis() + 50000000),
            true,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis() + 1000000),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "Купить что-то важное",
            Importance.Basic,
            null,
            false,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis() + 75000000),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "Купить что-то очень очень очень очень очень важное",
            Importance.High,
            Date(System.currentTimeMillis() - 75000000),
            false,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis() - 7500000),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "Не забыть отдохнуть)",
            Importance.High,
            Date(System.currentTimeMillis()),
            true,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "Доделать домашку до дедлайна",
            Importance.High,
            null,
            true,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "нет названия",
            Importance.Low,
            Date(System.currentTimeMillis() + 600000000),
            true,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "ооочень длинная рыба Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Viverra aliquet eget sit amet tellus cras. Tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra. Elit pellentesque habitant morbi tristique senectus et netus et malesuada. Non consectetur a erat nam. Pretium fusce id velit ut tortor pretium viverra suspendisse potenti. Accumsan lacus vel facilisis volutpat est velit egestas dui. Lectus proin nibh nisl condimentum id venenatis a condimentum. Phasellus faucibus scelerisque eleifend donec. Lorem sed risus ultricies tristique nulla.",
            Importance.Basic,
            null,
            false,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis() + 4739969523),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "           ",
            Importance.Low,
            Date(System.currentTimeMillis()),
            false,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "очередное очень важноое дело",
            Importance.High,
            null,
            true,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis() + 28858735),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "лень",
            Importance.High,
            Date(System.currentTimeMillis() + 28998735),
            true,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "когда же они уже закончатся",
            Importance.Basic,
            null,
            false,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
        ),
        ToDoItem(
            UUID.randomUUID(),
            "Dui ut ornare lectus sit amet est placerat. Pellentesque elit eget gravida cum sociis natoque penatibus et magnis. Auctor urna nunc id cursus metus aliquam eleifend mi in. Purus ut faucibus pulvinar elementum integer. Proin sed libero enim sed. Nullam eget felis eget nunc lobortis mattis aliquam faucibus purus. Facilisis sed odio morbi quis commodo odio aenean sed. Mattis enim ut tellus elementum sagittis vitae. Sed lectus vestibulum mattis ullamcorper. Fringilla urna porttitor rhoncus dolor purus non enim. Velit ut tortor pretium viverra suspendisse potenti nullam ac tortor. Dictumst quisque sagittis purus sit amet volutpat consequat. Porttitor lacus luctus accumsan tortor posuere ac ut consequat semper. Tortor aliquam nulla facilisi cras fermentum odio eu. Nunc scelerisque viverra mauris in aliquam sem fringilla. Sit amet mauris commodo quis. Aliquet eget sit amet tellus cras adipiscing enim. Eu ultrices vitae auctor eu augue. Ut sem nulla pharetra diam sit amet nisl suscipit adipiscing. Sed adipiscing diam donec adipiscing tristique risus.",
            Importance.High,
            Date(System.currentTimeMillis()),
            false,
            0,
            Date(System.currentTimeMillis()),
            Date(System.currentTimeMillis()),
        )
    )

    suspend fun getAllTasks(): List<ToDoItem> {
        delay(500)
        return tasks
    }

    suspend fun getSingleTask(id: UUID): ToDoItem? {
        delay(500)
        return tasks.find { it.id == id }
    }

    suspend fun deleteTask(id: UUID) {
        delay(500)
        tasks = tasks.filterNot { it.id == id } as MutableList<ToDoItem>
    }

    suspend fun saveTask(task: ToDoItem) {
        delay(500)
        val index = tasks.indexOf(task)
        if (index == -1) tasks.add(task)
        else tasks[index] = task
    }

    suspend fun saveTaskList(taskList: List<ToDoItem>) {
        delay(500)
        tasks = taskList.toMutableList()
    }
}