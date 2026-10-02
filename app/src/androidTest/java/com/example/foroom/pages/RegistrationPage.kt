package com.example.foroom.pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
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

class RegistrationPage {
    private val registrationScreen: Matcher<View> = hasSibling(withId(R.id.listView))

    val userNameInput: Matcher<View> = allOf(withId(R.id.userNameInput), registrationScreen)
    val passwordInput: Matcher<View> = allOf(withId(R.id.passwordInput), registrationScreen)
    val repeatPasswordInput: Matcher<View> = allOf(withId(R.id.repeatPasswordInput), registrationScreen)
    val avatarList: Matcher<View> = withId(R.id.listView)
    val signUpButton: Matcher<View> = allOf(withId(R.id.signUpButton), registrationScreen)
    val homeContainer: Matcher<View> = withId(R.id.homeContainer)
    val homeNavigationBar: Matcher<View> = withId(R.id.navBar)

    fun userNameField(): ViewInteraction = waitFor(editTextOf(userNameInput))

    fun passwordField(): ViewInteraction = waitFor(editTextOf(passwordInput))

    fun repeatPasswordField(): ViewInteraction = waitFor(editTextOf(repeatPasswordInput))

    fun signUpButton(): ViewInteraction = waitFor(signUpButton)

    fun loadedAvatarList(): ViewInteraction = waitFor(allOf(avatarList, hasLoadedAvatars()))

    fun avatar(index: Int): ViewInteraction = waitFor(avatarAt(index))

    fun homeContainer(): ViewInteraction = waitFor(homeContainer)

    fun homeNavigationBar(): ViewInteraction = waitFor(homeNavigationBar)

    fun enterUserName(userName: String) = apply {
        userNameField().perform(replaceText(userName), closeSoftKeyboard())
    }

    fun enterPassword(password: String) = apply {
        passwordField().perform(replaceText(password), closeSoftKeyboard())
    }

    fun enterRepeatPassword(password: String) = apply {
        repeatPasswordField().perform(replaceText(password), closeSoftKeyboard())
    }

    fun tapAvatar(index: Int) = apply {
        loadedAvatarList()
        avatar(index).perform(click())
    }

    fun tapSignUp() = apply {
        signUpButton().perform(click())
    }

    fun isSelectedAvatar(): Matcher<View> =
        object : BoundedMatcher<View, ImageChooserItemView>(ImageChooserItemView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("is selected avatar")
            }

            override fun matchesSafely(item: ImageChooserItemView): Boolean = item.isImageSelected
        }

    private fun hasLoadedAvatars(): Matcher<View> =
        object : BoundedMatcher<View, ImageChooserListView>(ImageChooserListView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has loaded avatars")
            }

            override fun matchesSafely(item: ImageChooserListView): Boolean =
                item.isChoosingEnabled && item.images.isNotEmpty() &&
                    item.images.none { image -> image.id == Image.BLANK_IMAGE_ID }
        }

    private fun avatarAt(index: Int): Matcher<View> =
        object : BoundedMatcher<View, ImageChooserItemView>(ImageChooserItemView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("avatar at index $index")
            }

            override fun matchesSafely(item: ImageChooserItemView): Boolean {
                val list = generateSequence(item.parent) { parent -> parent.parent }
                    .filterIsInstance<ImageChooserListView>()
                    .firstOrNull { view -> view.id == R.id.listView } ?: return false

                return TreeIterables.breadthFirstViewTraversal(list)
                    .filterIsInstance<ImageChooserItemView>()
                    .indexOf(item) == index
            }
        }

    private fun editTextOf(input: Matcher<View>): Matcher<View> =
        allOf(withId(DesignSystemR.id.inputEditText), isDescendantOfA(input))

    private fun waitFor(matcher: Matcher<View>): ViewInteraction =
        onView(matcher).waitUntilVisible(TIMEOUT_SECONDS)
}
