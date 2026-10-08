# Android 4 – Device Matrix

Test class: `com.example.foroom.tests.ConversationTests`
Build: `debug` variant installed as **Foroom Training** (`com.alternator.foroom.training`)
Date: 2026-10-08

## Configurations

| # | Device (AVD) | Android / API | Screen resolution | Density | Type |
|---|---|---|---|---|---|
| 1 | `Pixel_6a` (Pixel 6a profile) | Android 13 / API 33 | 1080 x 2400 | 420 dpi | Emulator (Google APIs, x86_64) |
| 2 | `Pixel_7_Pro_API_34` (Pixel 7 Pro profile) | Android 14 / API 34 | 1440 x 3120 | 560 dpi | Emulator (Google APIs, x86_64) |
| 3 | `Pixel_3a_API_35` (Pixel 3a profile) | Android 15 / API 35 | 1080 x 2220 | 440 dpi | Emulator (Google APIs, x86_64) |

## Results

| Scenario | Test | API 33 | API 34 | API 35 |
|---|---|---|---|---|
| 1 – Send a message in johnWeek | `sentMessageInJohnWeekStaysAfterReopeningChat` | Pass | Pass | Pass |
| 2 – Send a question in own chat | `questionAboutFavoriteModuleIsShownInOwnChat` | Pass | Pass | Pass |
| 3 – Continue a conversation using another account | `secondAccountReadsOlderGreetingAndReplyIsVisibleToFirstAccount` | Pass | Pass | Pass |

On each configuration the tests were run:

1. as the full test class,
2. each scenario on its own,
3. as the full test class again, with the messages and sessions from the earlier runs still on the device.

All runs passed on all three configurations.

## Test data

`ConversationTestDataRule` prepares the local data on each device before every test. It signs out any saved session, makes sure both accounts exist, and creates the following chats as User A if they are missing:

| Data | Value |
|---|---|
| User A | `nodari_user_a` |
| User B | `nodari_user_b` |
| Chat 1 | `johnWeek` |
| Chat 2 | `Nodari Menteshashvili chat` |
| Shared chat | `something` |

The accounts are local training accounts created only for these tests. Every sent message ends with a timestamp suffix, so messages from earlier runs do not affect the assertions.

## Notes

- No physical device was used. The available phone (Pixel 6a) runs Android 16 / API 36, which is outside the required matrix, so three emulators with different screen sizes were used instead.
- During development, earlier versions of the tests failed intermittently on API 35 right after the app data was cleared, in two ways:
  - a chat card was tapped while the chat list was still reloading after the search;
  - the newest message was laid out just below the visible area when a chat was reopened.

  The final version waits for stable search results and scrolls to the newest message before checking it. It passed every run listed above, plus 3 extra full-class runs from cleared app data on API 35.

## Screenshots

Android Studio test results for each configuration are in `screenshots/android4/`:

- `screenshots/android4/api33_pixel_6a.png`
- `screenshots/android4/api34_pixel_7_pro.png`
- `screenshots/android4/api35_pixel_3a.png`
