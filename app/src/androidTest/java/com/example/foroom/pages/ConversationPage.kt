package com.example.foroom.pages

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.alternator.foroom.R
import com.example.foroom.Helper.isShownOnScreen
import com.example.foroom.Helper.swiper
import com.example.foroom.Helper.waitUntil
import com.example.foroom.Helper.waitUntilVisible
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import com.example.design_system.R as DesignSystemR

private const val TIMEOUT_SECONDS = 10L
private const val MAX_SWIPES = 15
private const val SWIPE_DURATION_MS = 300
private const val SWIPE_EDGE_DIVIDER = 5
private const val LATEST_MESSAGE_RETRY_MS = 200L

class ConversationPage {
    private val conversationScreen: Matcher<View> = hasSibling(withId(R.id.messageInput))

    val header: Matcher<View> = allOf(withId(R.id.chatHeaderView), conversationScreen)
    val closeButton: Matcher<View> = allOf(withId(R.id.closeButton), conversationScreen)
    val messageInput: Matcher<View> = withId(R.id.messageInput)
    val sendButton: Matcher<View> =
        allOf(withId(R.id.sendMessageButton), isDescendantOfA(messageInput))
    val messagesList: Matcher<View> = withId(R.id.messagesRecyclerView)

    fun chatName(name: String): ViewInteraction = waitFor(
        allOf(withId(DesignSystemR.id.chatNameTextView), withText(name), isDescendantOfA(header))
    )

    fun messageField(): ViewInteraction =
        waitFor(allOf(withId(DesignSystemR.id.inputEditText), isDescendantOfA(messageInput)))

    fun sendButton(): ViewInteraction = waitFor(sendButton)

    fun closeButton(): ViewInteraction = waitFor(closeButton)

    fun messagesList(): ViewInteraction = waitFor(messagesList)

    fun message(text: String): ViewInteraction = waitFor(messageWithText(text))

    fun senderOf(text: String): ViewInteraction = waitFor(senderOfMessage(text))

    fun latestMessage(text: String): ViewInteraction {
        val endTime = System.currentTimeMillis() + TIMEOUT_SECONDS * 1000

        while (System.currentTimeMillis() < endTime) {
            scrollToLatestMessage()
            if (messageWithText(text).isShownOnScreen()) break
            Thread.sleep(LATEST_MESSAGE_RETRY_MS)
        }

        return onView(messageWithText(text))
    }

    fun enterMessage(text: String) = apply {
        messageField().perform(replaceText(text), closeSoftKeyboard())
    }

    fun tapSend() = apply {
        sendButton().waitUntil(isEnabled(), TIMEOUT_SECONDS).perform(click())
    }

    fun waitUntilMessageFieldCleared() = apply {
        messageField().waitUntil(withText(""), TIMEOUT_SECONDS)
    }

    fun scrollToLatestMessage() = apply {
        messagesList().perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
    }

    fun tapClose() = apply {
        closeButton().perform(click())
    }

    fun swipeToOlderMessage(text: String) = apply {
        messagesList()

        for (attempt in 1..MAX_SWIPES) {
            if (senderOfMessage(text).isShownOnScreen()) return@apply
            swipeTowardsOlderMessages()
        }
    }

    private fun swipeTowardsOlderMessages() {
        val bounds = IntArray(2)

        messagesList().perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = isDisplayed()

            override fun getDescription(): String = "read message list bounds on screen"

            override fun perform(uiController: UiController, view: View) {
                val location = IntArray(2)
                view.getLocationOnScreen(location)
                bounds[0] = location[1] + view.paddingTop
                bounds[1] = location[1] + view.height - view.paddingBottom
            }
        })

        val (top, bottom) = bounds.let { it[0] to it[1] }
        val edge = (bottom - top) / SWIPE_EDGE_DIVIDER
        swiper(top + edge, bottom - edge, SWIPE_DURATION_MS)
    }

    private fun messageWithText(text: String): Matcher<View> = allOf(
        withId(DesignSystemR.id.messageTextView),
        withText(text),
        isDescendantOfA(messagesList)
    )

    private fun senderOfMessage(text: String): Matcher<View> = allOf(
        withId(DesignSystemR.id.userNameTextView),
        isDescendantOfA(
            allOf(
                withId(DesignSystemR.id.contentLinearLayout),
                hasDescendant(messageWithText(text))
            )
        )
    )

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
