package com.example.foroom.steps

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.foroom.pages.ChangeLanguagePage
import com.example.foroom.pages.ChangePasswordPage
import com.example.foroom.pages.ProfilePage

class ProfileSteps(
    private val profilePage: ProfilePage = ProfilePage(),
    private val changePasswordPage: ChangePasswordPage = ChangePasswordPage(),
    private val changeLanguagePage: ChangeLanguagePage = ChangeLanguagePage()
) {

    fun openProfile() = apply {
        profilePage.tapProfileTab()
        verifyProfileScreenDisplayed()
    }

    fun verifyProfileScreenDisplayed() = apply {
        profilePage.changePasswordItem().check(matches(isDisplayed()))
        profilePage.changeLanguageItem().check(matches(isDisplayed()))
        profilePage.signOutItem().check(matches(isDisplayed()))
    }

    fun signOut() = apply {
        openProfile()
        profilePage.tapSignOut()
    }

    fun changePassword(newPassword: String) = apply {
        profilePage.tapChangePassword()
        changePasswordPage.enterPassword(newPassword)
            .enterRepeatPassword(newPassword)
            .tapConfirm()
    }

    fun changeLanguageToGeorgian() = apply {
        profilePage.tapChangeLanguage()
        changeLanguagePage.tapGeorgian()
    }

    fun changeLanguageToEnglish() = apply {
        profilePage.tapChangeLanguage()
        changeLanguagePage.tapEnglish()
    }

    fun verifyGeorgianLabelsDisplayed() = apply {
        verifyLabelsDisplayed(GEORGIAN_CHANGE_LANGUAGE, GEORGIAN_SIGN_OUT)
    }

    fun verifyEnglishLabelsDisplayed() = apply {
        verifyLabelsDisplayed(ENGLISH_CHANGE_LANGUAGE, ENGLISH_SIGN_OUT)
    }

    private fun verifyLabelsDisplayed(changeLanguage: String, signOut: String) {
        profilePage.changeLanguageTitle(changeLanguage).check(matches(isDisplayed()))
        profilePage.signOutTitle(signOut).check(matches(isDisplayed()))
    }

    companion object {
        private const val GEORGIAN_CHANGE_LANGUAGE = "ენის შეცვლა"
        private const val GEORGIAN_SIGN_OUT = "გამოსვლა"
        private const val ENGLISH_CHANGE_LANGUAGE = "Change Language"
        private const val ENGLISH_SIGN_OUT = "Sign Out"
    }
}
