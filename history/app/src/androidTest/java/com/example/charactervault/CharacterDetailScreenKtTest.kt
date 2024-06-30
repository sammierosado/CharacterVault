package com.example.charactervault

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.charactervault.model.CharacterDetail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCharacterDetailScreen() {
        // Mock data
        val characterDetail = CharacterDetail(
            name = "Scorpion",
            real_name = "Hanzo Hasashi",
            aliases = "Hanzo Hasashi",
            deck = "A wraith from the Netherrealm.",
            description = "Scorpion's description.",
            gender = 1.toString(),
            birthday = null,
            first_appeared_in_game = null,
            site_detail_url = null,
            image = null
        )

        composeTestRule.setContent {
            CharacterDetailScreen(characterId = 1)
        }

        // Wait until the mock data is loaded
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Scorpion").fetchSemanticsNodes().isNotEmpty()
        }

        // Check if the character name is displayed
        composeTestRule.onNodeWithText("Scorpion").assertExists()

        // Check if the real name is displayed
        composeTestRule.onNodeWithText("Real Name: Hanzo Hasashi").assertExists()

        // Perform click on the character (if there's any clickable action)
        composeTestRule.onNodeWithText("Scorpion").performClick()
    }
}
