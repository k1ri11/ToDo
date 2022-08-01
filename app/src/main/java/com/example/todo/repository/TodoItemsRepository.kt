package com.example.todo.repository

import com.example.todo.model.Importance
import com.example.todo.model.TodoItemReq
import java.util.*

class TodoItemsRepository {
    var tasks = mutableListOf(
        TodoItemReq(
            UUID.randomUUID(),
            "Купить что-то",
            Importance.Low,
            System.currentTimeMillis() + 50000000,
            true,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis() + 1000000,
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "Купить что-то важное",
            Importance.Basic,
            null,
            false,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis() + 75000000,
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "Купить что-то очень очень очень очень очень важное",
            Importance.High,
            System.currentTimeMillis() - 75000000,
            false,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis() - 7500000,
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "Не забыть отдохнуть)",
            Importance.High,
            System.currentTimeMillis(),
            true,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "Доделать домашку до дедлайна",
            Importance.High,
            null,
            true,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "нет названия",
            Importance.Low,
            System.currentTimeMillis() + 600000000,
            true,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "ооочень длинная рыба Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Viverra aliquet eget sit amet tellus cras. Tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra. Elit pellentesque habitant morbi tristique senectus et netus et malesuada. Non consectetur a erat nam. Pretium fusce id velit ut tortor pretium viverra suspendisse potenti. Accumsan lacus vel facilisis volutpat est velit egestas dui. Lectus proin nibh nisl condimentum id venenatis a condimentum. Phasellus faucibus scelerisque eleifend donec. Lorem sed risus ultricies tristique nulla.",
            Importance.Basic,
            null,
            false,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis() + 4739969523,
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "           ",
            Importance.Low,
            System.currentTimeMillis(),
            false,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "очередное очень важноое дело",
            Importance.High,
            null,
            true,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis() + 28858735,
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "лень",
            Importance.High,
            System.currentTimeMillis() + 28998735,
            true,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "когда же они уже закончатся",
            Importance.Basic,
            null,
            false,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            ""
        ),
        TodoItemReq(
            UUID.randomUUID(),
            "Dui ut ornare lectus sit amet est placerat. Pellentesque elit eget gravida cum sociis natoque penatibus et magnis. Auctor urna nunc id cursus metus aliquam eleifend mi in. Purus ut faucibus pulvinar elementum integer. Proin sed libero enim sed. Nullam eget felis eget nunc lobortis mattis aliquam faucibus purus. Facilisis sed odio morbi quis commodo odio aenean sed. Mattis enim ut tellus elementum sagittis vitae. Sed lectus vestibulum mattis ullamcorper. Fringilla urna porttitor rhoncus dolor purus non enim. Velit ut tortor pretium viverra suspendisse potenti nullam ac tortor. Dictumst quisque sagittis purus sit amet volutpat consequat. Porttitor lacus luctus accumsan tortor posuere ac ut consequat semper. Tortor aliquam nulla facilisi cras fermentum odio eu. Nunc scelerisque viverra mauris in aliquam sem fringilla. Sit amet mauris commodo quis. Aliquet eget sit amet tellus cras adipiscing enim. Eu ultrices vitae auctor eu augue. Ut sem nulla pharetra diam sit amet nisl suscipit adipiscing. Sed adipiscing diam donec adipiscing tristique risus.",
            Importance.High,
            System.currentTimeMillis(),
            false,
            "#000000",
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            ""
        )
    )

    fun getAllTasks(): List<TodoItemReq> {
       return tasks
    }

    fun getSingleTask(id: UUID): TodoItemReq? {
        return tasks.find { it.id == id }
    }

    fun deleteTask(id: UUID) {
        tasks = tasks.filterNot { it.id == id } as MutableList<TodoItemReq>
    }

    fun saveTask(task: TodoItemReq) {
        var index = -1
        tasks.forEachIndexed() { ind, it ->
            if (it.id == task.id)
                index = ind
        }
        if (index == -1) tasks.add(task)
        else tasks[index] = task
    }
}