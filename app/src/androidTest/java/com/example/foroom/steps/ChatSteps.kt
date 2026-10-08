package com.example.foroom.steps

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.foroom.pages.ChatsPage
import com.example.foroom.pages.CreateChatPage

class ChatSteps(
    private val chatsPage: ChatsPage = ChatsPage(),
    private val createChatPage: CreateChatPage = CreateChatPage()
) {

    fun verifyHomeScreenDisplayed() = apply {
        chatsPage.homeContainer().check(matches(isDisplayed()))
        chatsPage.navigationBar().check(matches(isDisplayed()))
    }

    fun openCreateChat() = apply {
        chatsPage.tapCreateChatTab()
        createChatPage.chatNameField().check(matches(isDisplayed()))
        createChatPage.createChatButton().check(matches(isDisplayed()))
    }

    fun enterChatName(name: String) = apply {
        createChatPage.enterChatName(name)
    }

    fun selectChatImage(index: Int) = apply {
        createChatPage.tapImage(index)
        createChatPage.image(index).check(matches(createChatPage.isSelectedImage()))
    }

    fun submitChat() = apply {
        createChatPage.tapCreateChat()
    }

    fun verifyOpenedChatName(name: String) = apply {
        chatsPage.openedChatName(name).check(matches(isDisplayed()))
    }

    fun closeChat() = apply {
        chatsPage.tapCloseChat()
        chatsPage.searchField().check(matches(isDisplayed()))
        chatsPage.chatsList().check(matches(isDisplayed()))
    }

    fun searchChat(name: String) = apply {
        chatsPage.enterSearchQuery(name)
    }

    fun verifyChatDisplayedInList(name: String) = apply {
        chatsPage.chatCard(name).check(matches(isDisplayed()))
    }

    fun openChat(name: String) = apply {
        searchChat(name)
        chatsPage.waitForSearchResults(name)
        verifyChatDisplayedInList(name)
        chatsPage.tapOpenChat(name)
    }
}
