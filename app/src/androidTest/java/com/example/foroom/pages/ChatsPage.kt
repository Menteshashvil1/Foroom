package com.example.foroom.pages

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.alternator.foroom.R
import com.example.design_system.components.chat.ForoomChatCardView
import androidx.test.espresso.util.TreeIterables
import com.example.foroom.Helper.waitUntilStable
import com.example.foroom.Helper.waitUntilVisible
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import com.example.design_system.R as DesignSystemR

private const val TIMEOUT_SECONDS = 10L
private const val SEARCH_STABLE_MS = 500L

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

    fun openChatButton(name: String): ViewInteraction = waitFor(
        allOf(
            withId(DesignSystemR.id.sendMessageButton),
            isDescendantOfA(chatCardWithTitle(name))
        )
    )

    fun tapOpenChat(name: String) = apply {
        openChatButton(name).perform(click())
    }

    fun waitForSearchResults(query: String) = apply {
        chatsList().waitUntilStable(showsOnlyChatsMatching(query), SEARCH_STABLE_MS, TIMEOUT_SECONDS)
    }

    private fun showsOnlyChatsMatching(query: String): Matcher<View> =
        object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("shows only chats matching \"$query\"")
            }

            override fun matchesSafely(item: RecyclerView): Boolean {
                val titles = TreeIterables.breadthFirstViewTraversal(item)
                    .filterIsInstance<TextView>()
                    .filter { view -> view.id == DesignSystemR.id.chatTitleTextView && view.isShown }
                    .map { view -> view.text.toString() }

                return item.isShown && titles.isNotEmpty() &&
                    titles.all { title -> title.contains(query, ignoreCase = true) }
            }
        }

    fun tapCreateChatTab() = apply {
        createChatTab().perform(click())
    }

    fun tapCloseChat() = apply {
        closeChatButton().perform(click())
    }

    fun enterSearchQuery(query: String) = apply {
        searchField().perform(replaceText(query), closeSoftKeyboard())
    }

    private fun chatCardWithTitle(name: String): Matcher<View> = allOf(
        isAssignableFrom(ForoomChatCardView::class.java),
        isDescendantOfA(chatsList),
        hasDescendant(allOf(withId(DesignSystemR.id.chatTitleTextView), withText(name)))
    )

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
