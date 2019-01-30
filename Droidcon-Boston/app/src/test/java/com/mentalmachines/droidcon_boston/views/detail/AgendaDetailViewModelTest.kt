package com.mentalmachines.droidcon_boston.views.detail

import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.mockito.Mockito.mock

class AgendaDetailViewModelTest {
    private val mockAgendaRepo = mock(UserAgendaRepo::class.java)
    private val mockFirebaseHelper = mock(FirebaseHelper::class.java)
    private val talkTitle = "Talk Title"
    private val talkRoom = "Talk Room"
    private val startTime = "2018-03-26T15:00:00.000Z"
    private val endTime = "2018-03-26T15:45:00.000Z"
    private val scheduleId = "Schedule ID"
    private val speakerNames = listOf("John", "Bob")

    private val scheduleRow = Schedule.ScheduleRow(
        talkTitle = talkTitle,
        room = talkRoom,
        startTime = startTime,
        endTime = endTime,
        id = scheduleId,
        speakerNames = speakerNames
    )

    private val viewModel = AgendaDetailViewModel(scheduleRow, mockAgendaRepo, mockFirebaseHelper)

    @Test
    fun getTalkTitle() {
        assertEquals(talkTitle, viewModel.talkTitle)
    }

    @Test
    fun getRoom() {
        assertEquals(talkRoom, viewModel.room)
    }

    @Test
    fun getStartTime() {
        assertEquals(startTime, viewModel.startTime)
    }

    @Test
    fun getEndTime() {
        assertEquals(endTime, viewModel.endTime)
    }

    @Test
    fun getScheduleRowId() {
        assertEquals(scheduleId, viewModel.schedulerowId)
    }

    @Test
    fun getSpeakerNames() {
        assertEquals(speakerNames, viewModel.speakerNames)
    }

    @Test
    fun bookmarkedWithoutSession() {
        assertFalse(viewModel.isBookmarked)
    }
}