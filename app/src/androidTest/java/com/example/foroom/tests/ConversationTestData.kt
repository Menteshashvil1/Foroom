package com.example.foroom.tests

import com.example.foroom.domain.model.request.LogInRequest
import com.example.foroom.domain.model.request.RegistrationRequest
import com.example.foroom.domain.repository.rest.ForoomRestRepository
import com.example.foroom.presentation.ui.util.datastore.user.ForoomUserDataStore
import com.example.shared.util.runtime.user_token.UserTokenRuntimeHolder
import kotlinx.coroutines.runBlocking
import org.junit.rules.ExternalResource
import org.koin.core.context.GlobalContext

data class TestAccount(val userName: String, val password: String)

object ConversationTestData {
    val USER_A = TestAccount("nodari_user_a", "UserA123!")
    val USER_B = TestAccount("nodari_user_b", "UserB123!")

    const val JOHN_WEEK_CHAT = "johnWeek"
    const val OWN_CHAT = "Nodari Menteshashvili chat"
    const val SHARED_CHAT = "something"

    val CHAT_TITLES = listOf(JOHN_WEEK_CHAT, OWN_CHAT, SHARED_CHAT)
}

class ConversationTestDataRule : ExternalResource() {
    private val koin get() = GlobalContext.get()

    override fun before() {
        runBlocking {
            val repository = koin.get<ForoomRestRepository>()
            val tokenHolder = koin.get<UserTokenRuntimeHolder>()

            koin.get<ForoomUserDataStore>().clearUserData()
            ensureAccount(repository, ConversationTestData.USER_B)
            tokenHolder.setUserToken(ensureAccount(repository, ConversationTestData.USER_A))

            ConversationTestData.CHAT_TITLES.forEach { title -> ensureChat(repository, title) }

            tokenHolder.setUserToken("")
        }
    }

    private suspend fun ensureAccount(repository: ForoomRestRepository, account: TestAccount): String =
        runCatching { repository.logInUser(LogInRequest(account.userName, account.password)) }
            .getOrElse {
                repository.registerUser(
                    RegistrationRequest(account.userName, account.password, AVATAR_ID)
                )
            }
            .token

    private suspend fun ensureChat(repository: ForoomRestRepository, title: String) {
        val exists = repository.getChats(page = 0, limit = CHAT_SEARCH_LIMIT, name = title)
            .chats.any { chat -> chat.name == title }

        if (!exists) repository.createChat(title, CHAT_EMOJI_ID)
    }

    companion object {
        private const val AVATAR_ID = 1
        private const val CHAT_EMOJI_ID = 1
        private const val CHAT_SEARCH_LIMIT = 100
    }
}
