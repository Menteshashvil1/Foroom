package com.example.foroom.pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.TreeIterables
import com.alternator.foroom.R
import com.example.design_system.components.image_chooser.ImageChooserItemView
import com.example.design_system.components.image_chooser.ImageChooserListView
import com.example.foroom.Helper.waitUntilVisible
import com.example.shared.model.Image
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import com.example.design_system.R as DesignSystemR

private const val TIMEOUT_SECONDS = 10L

class CreateChatPage {
    val chatNameInput: Matcher<View> = withId(R.id.chatNameInput)
    val imageChooser: Matcher<View> = withId(R.id.chatImageChooser)
    val createChatButton: Matcher<View> = withId(R.id.createChatButton)

    fun chatNameField(): ViewInteraction =
        waitFor(allOf(withId(DesignSystemR.id.inputEditText), isDescendantOfA(chatNameInput)))

    fun loadedImageChooser(): ViewInteraction = waitFor(allOf(imageChooser, hasLoadedImages()))

    fun image(index: Int): ViewInteraction = waitFor(imageAt(index))

    fun createChatButton(): ViewInteraction = waitFor(createChatButton)

    fun enterChatName(name: String) = apply {
        chatNameField().perform(replaceText(name), closeSoftKeyboard())
    }

    fun tapImage(index: Int) = apply {
        loadedImageChooser()
        image(index).perform(click())
    }

    fun tapCreateChat() = apply {
        createChatButton().perform(click())
    }

    fun isSelectedImage(): Matcher<View> =
        object : BoundedMatcher<View, ImageChooserItemView>(ImageChooserItemView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("is selected chat image")
            }

            override fun matchesSafely(item: ImageChooserItemView): Boolean = item.isImageSelected
        }

    private fun hasLoadedImages(): Matcher<View> =
        object : BoundedMatcher<View, ImageChooserListView>(ImageChooserListView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has loaded chat images")
            }

            override fun matchesSafely(item: ImageChooserListView): Boolean =
                item.isChoosingEnabled && item.images.isNotEmpty() &&
                    item.images.none { image -> image.id == Image.BLANK_IMAGE_ID }
        }

    private fun imageAt(index: Int): Matcher<View> =
        object : BoundedMatcher<View, ImageChooserItemView>(ImageChooserItemView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("chat image at index $index")
            }

            override fun matchesSafely(item: ImageChooserItemView): Boolean {
                val chooser = generateSequence(item.parent) { parent -> parent.parent }
                    .filterIsInstance<ImageChooserListView>()
                    .firstOrNull { view -> view.id == R.id.chatImageChooser } ?: return false

                return TreeIterables.breadthFirstViewTraversal(chooser)
                    .filterIsInstance<ImageChooserItemView>()
                    .indexOf(item) == index
            }
        }

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
