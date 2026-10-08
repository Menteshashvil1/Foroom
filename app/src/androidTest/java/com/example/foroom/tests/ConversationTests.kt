package com.example.foroom.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.foroom.presentation.ui.activity.ForoomActivity
import com.example.foroom.steps.ChatSteps
import com.example.foroom.steps.ConversationSteps
import com.example.foroom.steps.LoginSteps
import com.example.foroom.steps.ProfileSteps
import com.example.foroom.tests.ConversationTestData.JOHN_WEEK_CHAT
import com.example.foroom.tests.ConversationTestData.OWN_CHAT
import com.example.foroom.tests.ConversationTestData.SHARED_CHAT
import com.example.foroom.tests.ConversationTestData.USER_A
import com.example.foroom.tests.ConversationTestData.USER_B
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConversationTests {
    private val activityRule = ActivityScenarioRule(ForoomActivity::class.java)

    @get:Rule
    val rules: RuleChain = RuleChain.outerRule(ConversationTestDataRule()).around(activityRule)

    private val loginSteps = LoginSteps()
    private val profileSteps = ProfileSteps()
    private val chatSteps = ChatSteps()
    private val conversationSteps = ConversationSteps()

    private val suffix = System.currentTimeMillis().toString()

    @Test
    fun sentMessageInJohnWeekStaysAfterReopeningChat() {
        val message = "let's go for a drink $suffix"

        logIn(USER_A)
        openChat(JOHN_WEEK_CHAT)

        conversationSteps.sendMessage(message)
            .verifyLatestMessageDisplayed(message)
            .closeConversation()

        openChat(JOHN_WEEK_CHAT)
        conversationSteps.verifyLatestMessageDisplayed(message)
    }

    @Test
    fun questionAboutFavoriteModuleIsShownInOwnChat() {
        val question = "Which module do you like most in the Automation Academy? $suffix"

        logIn(USER_A)
        openChat(OWN_CHAT)

        conversationSteps.sendMessage(question)
            .verifyLatestMessageDisplayed(question)
    }

    @Test
    fun secondAccountReadsOlderGreetingAndReplyIsVisibleToFirstAccount() {
        val greeting = "Hello from ${USER_A.userName} $suffix"
        val followUps = (1..FOLLOW_UP_MESSAGE_COUNT).map { index -> "Message $index $suffix" }
        val reply = "Hi ${USER_A.userName}, reply from ${USER_B.userName} $suffix"

        logIn(USER_A)
        openChat(SHARED_CHAT)
        conversationSteps.sendMessage(greeting)
            .verifyLatestMessageDisplayed(greeting)
            .sendMessages(followUps)
            .closeConversation()
        switchAccount(USER_B)

        openChat(SHARED_CHAT)
        conversationSteps.swipeToOlderMessage(greeting)
            .verifyMessageSender(greeting, USER_A.userName)
            .sendMessage(reply)
            .verifyLatestMessageDisplayed(reply)
            .closeConversation()
        switchAccount(USER_A)

        openChat(SHARED_CHAT)
        conversationSteps.verifyLatestMessageDisplayed(reply)
            .verifyMessageSender(reply, USER_B.userName)
    }

    private fun logIn(account: TestAccount) {
        loginSteps.verifyLoginScreenDisplayed()
            .logIn(account.userName, account.password)
        chatSteps.verifyHomeScreenDisplayed()
    }

    private fun switchAccount(account: TestAccount) {
        profileSteps.signOut()
        logIn(account)
    }

    private fun openChat(title: String) {
        chatSteps.openChat(title)
        conversationSteps.verifyConversationOpen(title)
    }

    companion object {
        private const val FOLLOW_UP_MESSAGE_COUNT = 25
    }
}
