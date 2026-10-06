package com.example.foroom.pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.alternator.foroom.R
import com.example.foroom.Helper.waitUntilVisible
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import com.example.design_system.R as DesignSystemR

private const val TIMEOUT_SECONDS = 10L

class ChatsPage {
    private val openedChatScreen: Matcher<View> = hasSibling(withId(R.id.messageInput))

    val homeContainer: Matcher<View> = withId(R.id.homeContainer)
    val navigationBar: Matcher<View> = withId(R.id.navBar)
    val chatsTab: Matcher<View> = withId(R.id.homeNavigationChats)
    val createChatTab: Matcher<View> = withId(R.id.homeNavigationCreateChat)
    val searchChatInput: Matcher<View> = withId(R.id.searchChatInput)
    val chatsList: Matcher<View> = withId(R.id.chatsRecyclerView)
    val openedChatHeader: Matcher<View> = allOf(withId(R.id.chatHeaderView), openedChatScreen)
    val closeChatButton: Matcher<View> = allOf(withId(R.id.closeButton), openedChatScreen)

    fun homeContainer(): ViewInteraction = waitFor(homeContainer)

    fun navigationBar(): ViewInteraction = waitFor(navigationBar)

    fun createChatTab(): ViewInteraction = waitFor(createChatTab)

    fun searchField(): ViewInteraction =
        waitFor(allOf(withId(DesignSystemR.id.inputEditText), isDescendantOfA(searchChatInput)))

    fun chatsList(): ViewInteraction = waitFor(chatsList)

    fun chatCard(name: String): ViewInteraction = waitFor(
        allOf(
            withId(DesignSystemR.id.chatTitleTextView),
            withText(name),
            isDescendantOfA(chatsList)
        )
    )

    fun openedChatName(name: String): ViewInteraction = waitFor(
        allOf(
            withId(DesignSystemR.id.chatNameTextView),
            withText(name),
            isDescendantOfA(openedChatHeader)
        )
    )

    fun closeChatButton(): ViewInteraction = waitFor(closeChatButton)

    fun tapCreateChatTab() = apply {
        createChatTab().perform(click())
    }

    fun tapCloseChat() = apply {
        closeChatButton().perform(click())
    }

    fun enterSearchQuery(query: String) = apply {
        searchField().perform(replaceText(query), closeSoftKeyboard())
    }

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
