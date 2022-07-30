package com.example.todo.repository

import androidx.lifecycle.MutableLiveData
import com.example.todo.model.TodoItemReq
import java.util.*

class TodoItemsRepository {
    val allTasks: MutableLiveData<List<TodoItemReq>> = MutableLiveData(
        listOf(
            TodoItemReq(
                UUID.fromString("b0a899ac-0e6d-11ed-861d-0242ac120002"),
                "Купить что-то",
                "low",
                System.currentTimeMillis() + 50000000,
                true,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis() + 1000000,
                ""
            ),
            TodoItemReq(
                UUID.fromString("b0a89baa-0e6d-11ed-861d-0242ac120002"),
                "Купить что-то важное",
                "basic",
                System.currentTimeMillis(),
                false,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis() + 75000000,
                ""
            ),
            TodoItemReq(
                UUID.fromString("b0a89baa-0e6d-11ed-861d-0242ac120002"),
                "Купить что-то очень очень очень очень очень важное",
                "high",
                System.currentTimeMillis() - 75000000,
                false,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis() - 7500000,
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a157554-0f4c-11ed-861d-0242ac120002"),
                "Не забыть отдохнуть)",
                "high",
                System.currentTimeMillis(),
                true,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a15763a-0f4c-11ed-861d-0242ac120002"),
                "Доделать домашку до дедлайна",
                "high",
                System.currentTimeMillis(),
                true,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a157720-0f4c-11ed-861d-0242ac120002"),
                "нет названия",
                "low",
                System.currentTimeMillis() + 600000000,
                true,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a15796e-0f4c-11ed-861d-0242ac120002"),
                "ооочень длинная рыба Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Viverra aliquet eget sit amet tellus cras. Tellus cras adipiscing enim eu turpis egestas pretium aenean pharetra. Elit pellentesque habitant morbi tristique senectus et netus et malesuada. Non consectetur a erat nam. Pretium fusce id velit ut tortor pretium viverra suspendisse potenti. Accumsan lacus vel facilisis volutpat est velit egestas dui. Lectus proin nibh nisl condimentum id venenatis a condimentum. Phasellus faucibus scelerisque eleifend donec. Lorem sed risus ultricies tristique nulla.",
                "basic",
                System.currentTimeMillis(),
                false,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis() + 4739969523,
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a157a54-0f4c-11ed-861d-0242ac120002"),
                "           ",
                "low",
                System.currentTimeMillis(),
                false,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a157b30-0f4c-11ed-861d-0242ac120002"),
                "очередное очень важноое дело",
                "high",
                System.currentTimeMillis(),
                true,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis() + 28858735,
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a157c0c-0f4c-11ed-861d-0242ac120002"),
                "лень",
                "high",
                System.currentTimeMillis() + 28998735,
                true,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a157e96-0f4c-11ed-861d-0242ac120002"),
                "когда же они уже закончатся",
                "basic",
                System.currentTimeMillis() + 1000000,
                false,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                ""
            ),
            TodoItemReq(
                UUID.fromString("9a158134-0f4c-11ed-861d-0242ac120002"),
                "Dui ut ornare lectus sit amet est placerat. Pellentesque elit eget gravida cum sociis natoque penatibus et magnis. Auctor urna nunc id cursus metus aliquam eleifend mi in. Purus ut faucibus pulvinar elementum integer. Proin sed libero enim sed. Nullam eget felis eget nunc lobortis mattis aliquam faucibus purus. Facilisis sed odio morbi quis commodo odio aenean sed. Mattis enim ut tellus elementum sagittis vitae. Sed lectus vestibulum mattis ullamcorper. Fringilla urna porttitor rhoncus dolor purus non enim. Velit ut tortor pretium viverra suspendisse potenti nullam ac tortor. Dictumst quisque sagittis purus sit amet volutpat consequat. Porttitor lacus luctus accumsan tortor posuere ac ut consequat semper. Tortor aliquam nulla facilisi cras fermentum odio eu. Nunc scelerisque viverra mauris in aliquam sem fringilla. Sit amet mauris commodo quis. Aliquet eget sit amet tellus cras adipiscing enim. Eu ultrices vitae auctor eu augue. Ut sem nulla pharetra diam sit amet nisl suscipit adipiscing. Sed adipiscing diam donec adipiscing tristique risus.",
                "high",
                System.currentTimeMillis(),
                false,
                "#000000",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                ""
            )
        )
    )

}