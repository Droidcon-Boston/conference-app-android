package com.mentalmachines.droidcon_boston.data

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class UserAgendaRepoTest {
    private val mockSharedPrefs = mock(SharedPreferences::class.java)
    private val mockEditor = mock(SharedPreferences.Editor::class.java)
    private val mockContext = mock(Context::class.java)
    private lateinit var userAgendaRepo: UserAgendaRepo

    @Before
    fun setup() {
        `when`(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPrefs)
        `when`(mockSharedPrefs.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putStringSet(anyString(), any())).thenReturn(mockEditor)
        userAgendaRepo = UserAgendaRepo.getInstance(mockContext)
    }

    @Test
    fun testBookmarkAndRemove() {
        val sessionId = "Session ID"
        assertFalse(userAgendaRepo.isSessionBookmarked(sessionId))

        userAgendaRepo.bookmarkSession(sessionId, true)
        assertTrue(userAgendaRepo.isSessionBookmarked(sessionId))

        userAgendaRepo.bookmarkSession(sessionId, false)
        assertFalse(userAgendaRepo.isSessionBookmarked(sessionId))
    }
}