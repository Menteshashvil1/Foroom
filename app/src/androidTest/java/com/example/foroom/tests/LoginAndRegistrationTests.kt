package com.example.foroom.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.foroom.presentation.ui.activity.ForoomActivity
import com.example.foroom.presentation.ui.util.datastore.user.ForoomUserDataStore
import com.example.foroom.steps.LoginSteps
import com.example.foroom.steps.RegistrationSteps
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class LoginAndRegistrationTests {
    private val signedOutUser = object : ExternalResource() {
        override fun before() {
            runBlocking { GlobalContext.get().get<ForoomUserDataStore>().clearUserData() }
        }
    }

    private val activityRule = ActivityScenarioRule(ForoomActivity::class.java)

    @get:Rule
    val rules: RuleChain = RuleChain.outerRule(signedOutUser).around(activityRule)

    private val loginSteps = LoginSteps()
    private val registrationSteps = RegistrationSteps()

    @Test
    fun validUserNameAndInvalidPasswordShowsPasswordError() {
        loginSteps.verifyLoginScreenDisplayed()
            .logIn(EXISTING_USER_NAME, INVALID_PASSWORD)
            .verifyPasswordErrorDisplayed()
    }

    @Test
    fun invalidUserNameAndInvalidPasswordShowsBothErrors() {
        loginSteps.verifyLoginScreenDisplayed()
            .logIn(uniqueUserName("missing"), INVALID_PASSWORD)
            .verifyUserNameErrorDisplayed()
            .verifyPasswordErrorDisplayed()
    }

    @Test
    fun registrationWithUniqueUserNameOpensHomeScreen() {
        loginSteps.verifyLoginScreenDisplayed()
            .openRegistration()

        registrationSteps.verifyRegistrationScreenDisplayed()
            .fillCredentials(uniqueUserName("user"), VALID_PASSWORD)
            .selectAvatar(AVATAR_INDEX)
            .submit()
            .verifyHomeScreenDisplayed()
    }

    private fun uniqueUserName(prefix: String) =
        "${prefix}_${UUID.randomUUID().toString().take(8)}"

    companion object {
        private const val EXISTING_USER_NAME = "student"
        private const val INVALID_PASSWORD = "WrongPassword1!"
        private const val VALID_PASSWORD = "Secret123!"
        private const val AVATAR_INDEX = 1
    }
}
