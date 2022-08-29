package com.example.todo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo.ui.view.MainActivity
import com.example.todo.ui.view.adapter.TaskViewHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random.Default.nextInt

@RunWith(AndroidJUnit4::class)
class UiTests {

    @JvmField
    @Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    val testText = nextInt().toString()


    @Test
    fun addTaskTest() {
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.edit_text))
            .perform(typeText(testText), closeSoftKeyboard())
        onView(withId(R.id.save)).perform(click())
        onView(withId(R.id.tasks_rv)).perform(
            RecyclerViewActions.scrollTo<TaskViewHolder>(
                hasDescendant(withText(testText)))
        )
    }

    @Test(expected = PerformException::class)
    fun deleteTask() {
        onView(withId(R.id.tasks_rv))
            .perform(RecyclerViewActions.actionOnItem<TaskViewHolder>(
                hasDescendant(withText(testText)), click())
            )
        onView(withId(R.id.delete_section)).perform(click())
        onView(withId(R.id.tasks_rv)).perform(
            RecyclerViewActions.scrollTo<TaskViewHolder>(
                hasDescendant(withText(testText)))
        )
    }

}