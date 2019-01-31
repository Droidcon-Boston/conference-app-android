package com.mentalmachines.droidcon_boston.views.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelperRobot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*

class AgendaDetailViewModelTest {
    private val mockAgendaRepo = mock(UserAgendaRepo::class.java)
    private val mockFirebaseHelper = mock(FirebaseHelper::class.java)
    private val talkTitle = "Talk Title"
    private val talkRoom = "Talk Room"
    private val startTime = "2018-03-26T15:00:00.000Z"
    private val endTime = "2018-03-26T15:45:00.000Z"
    private val scheduleId = "Schedule ID"
    private val primarySpeakerName = "John"
    private val secondarySpeakerName = "Bob"
    private val speakerNames = listOf(primarySpeakerName, secondarySpeakerName)

    private val scheduleRow = Schedule.ScheduleRow(
        talkTitle = talkTitle,
        room = talkRoom,
        startTime = startTime,
        endTime = endTime,
        id = scheduleId,
        speakerNames = speakerNames,
        primarySpeakerName = primarySpeakerName
    )

    private val viewModel = AgendaDetailViewModel(scheduleRow, mockAgendaRepo, mockFirebaseHelper)

    @JvmField
    @Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

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

    @Test
    fun loadData() {
        val eventSpeaker = FirebaseDatabase.EventSpeaker(name = primarySpeakerName)
        FirebaseHelperRobot(mockFirebaseHelper).mockSpeakers(listOf(eventSpeaker))

        viewModel.loadData()

        val scheduleDetail = eventSpeaker.toScheduleDetail(scheduleRow)
        assertEquals(scheduleDetail, viewModel.scheduleDetail.value)
    }
}