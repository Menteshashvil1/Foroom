package com.example.foroom.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.foroom.presentation.ui.activity.ForoomActivity
import com.example.foroom.presentation.ui.util.datastore.user.ForoomUserDataStore
import com.example.foroom.steps.ChatSteps
import com.example.foroom.steps.LoginSteps
import com.example.foroom.steps.ProfileSteps
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext

@RunWith(AndroidJUnit4::class)
class ProfileAndChatTests {
    private val signedOutUser = object : ExternalResource() {
        override fun before() {
            runBlocking { GlobalContext.get().get<ForoomUserDataStore>().clearUserData() }
        }
    }

    private val activityRule = ActivityScenarioRule(ForoomActivity::class.java)

    @get:Rule
    val rules: RuleChain = RuleChain.outerRule(signedOutUser).around(activityRule)

    private val loginSteps = LoginSteps()
    private val profileSteps = ProfileSteps()
    private val chatSteps = ChatSteps()

    @Test
    fun changedPasswordAllowsLogInWithNewPassword() {
        logIn(TEST_PASSWORD)

        profileSteps.openProfile()
            .changePassword(NEW_PASSWORD)

        loginSteps.verifyLoginScreenDisplayed()
        logIn(NEW_PASSWORD)

        profileSteps.openProfile()
            .changePassword(TEST_PASSWORD)

        loginSteps.verifyLoginScreenDisplayed()
    }

    @Test
    fun languageChangesFromGeorgianToEnglishAndBack() {
        logIn(TEST_PASSWORD)

        profileSteps.openProfile()
            .changeLanguageToGeorgian()
            .verifyGeorgianLabelsDisplayed()
            .changeLanguageToEnglish()
            .verifyEnglishLabelsDisplayed()
            .changeLanguageToGeorgian()
            .verifyGeorgianLabelsDisplayed()
    }

    @Test
    fun createdChatIsFoundInChatList() {
        val chatName = "$STUDENT_FULL_NAME ${System.currentTimeMillis()}"

        logIn(TEST_PASSWORD)

        chatSteps.openCreateChat()
            .enterChatName(chatName)
            .selectChatImage(CHAT_IMAGE_INDEX)
            .submitChat()
            .verifyOpenedChatName(chatName)
            .closeChat()
            .searchChat(chatName)
            .verifyChatDisplayedInList(chatName)
    }

    private fun logIn(password: String) {
        loginSteps.verifyLoginScreenDisplayed()
            .logIn(TEST_USER_NAME, password)
        chatSteps.verifyHomeScreenDisplayed()
    }

    companion object {
        private const val TEST_USER_NAME = "menteshashvili_qa"
        private const val TEST_PASSWORD = "Foroom123!"
        private const val NEW_PASSWORD = "Foroom456!"
        private const val STUDENT_FULL_NAME = "Nodari Menteshashvili"
        private const val CHAT_IMAGE_INDEX = 1
    }
}
