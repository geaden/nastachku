package com.example.simplejni

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class HelloStachkaScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showGreeting_whenNameFilled() {
        composeRule.setContent {
            HelloStachkaScreen()
        }

        composeRule.onNodeWithTag(NameFieldTag).performTextInput("Yandex")
        composeRule.onNodeWithText(TEST_GREETING, substring = true).assertExists()
    }

    @Test
    fun showGreetingWithJniGenerator_whenNameFilled() {
        composeRule.setContent {
            HelloStachkaScreen()
        }

        composeRule.onNodeWithTag(JniSwitchTag).performClick()
        composeRule.onNodeWithTag(NameFieldTag).performTextInput("Yandex")
        composeRule.onNodeWithText("JNI Generator", substring = true).assertExists()
    }

    private companion object {
        const val TEST_GREETING = "Hello Stachka! From Yandex with Native! ("
    }
}
