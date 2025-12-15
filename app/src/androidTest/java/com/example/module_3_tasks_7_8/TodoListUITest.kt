package com.example.module_3_tasks_7_8

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onFirst
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoListUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun test1_отображаются_все_3_задачи_из_JSON() {

        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Список задач (3)").assertIsDisplayed()

        composeTestRule.onNodeWithText("Купить молоко").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 литра, обезжиренное").assertIsDisplayed()

        composeTestRule.onNodeWithText("Позвонить маме").assertIsDisplayed()
        composeTestRule.onNodeWithText("Спросить про выходные").assertIsDisplayed()

        composeTestRule.onNodeWithText("Сделать ДЗ по Android").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clean Architecture + Compose").assertIsDisplayed()
    }

    @Test
    fun test2_чекбокс_переключает_статус() {

        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Купить молоко").assertIsDisplayed()

        composeTestRule.onAllNodesWithContentDescription("Checkbox")
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithText("Купить молоко").assertIsDisplayed()
    }

    @Test
    fun test3_навигация_List_Detail_List() {

        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Список задач (3)").assertIsDisplayed()

        composeTestRule.onNodeWithText("Купить молоко").performClick()

        composeTestRule.onNodeWithText("Назад").assertIsDisplayed()

        composeTestRule.onNodeWithText("Купить молоко").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 литра, обезжиренное").assertIsDisplayed()

        composeTestRule.onNodeWithText("Назад").performClick()

        composeTestRule.onNodeWithText("Список задач (3)").assertIsDisplayed()

        composeTestRule.onNodeWithText("Назад").assertDoesNotExist()
    }
}