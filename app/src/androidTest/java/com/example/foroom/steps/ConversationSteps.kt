package com.example.foroom.steps

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.foroom.pages.ConversationPage
import org.hamcrest.Matchers.allOf

class ConversationSteps(private val conversationPage: ConversationPage = ConversationPage()) {

    fun verifyConversationOpen(chatName: String) = apply {
        conversationPage.chatName(chatName).check(matches(isDisplayed()))
        conversationPage.messageField().check(matches(isDisplayed()))
        conversationPage.messagesList().check(matches(isDisplayed()))
    }

    fun sendMessage(text: String) = apply {
        conversationPage.enterMessage(text)
            .tapSend()
            .waitUntilMessageFieldCleared()
            .scrollToLatestMessage()
    }

    fun sendMessages(texts: List<String>) = apply {
        texts.forEach { text -> sendMessage(text) }
    }

    fun verifyMessageDisplayed(text: String) = apply {
        conversationPage.message(text).check(matches(isDisplayed()))
    }

    fun verifyLatestMessageDisplayed(text: String) = apply {
        conversationPage.latestMessage(text).check(matches(isDisplayed()))
    }

    fun verifyMessageSender(text: String, senderName: String) = apply {
        conversationPage.senderOf(text).check(matches(allOf(isDisplayed(), withText(senderName))))
    }

    fun swipeToOlderMessage(text: String) = apply {
        conversationPage.swipeToOlderMessage(text)
        verifyMessageDisplayed(text)
    }

    fun closeConversation() = apply {
        conversationPage.tapClose()
    }
}
