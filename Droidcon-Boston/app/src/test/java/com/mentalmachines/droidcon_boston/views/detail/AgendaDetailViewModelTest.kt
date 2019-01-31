package com.mentalmachines.droidcon_boston.views.detail

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelperRobot
import com.mentalmachines.droidcon_boston.utils.NotificationUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class AgendaDetailViewModelTest {
    private val mockFirebaseHelper = mock(FirebaseHelper::class.java)
    private val talkTitle = "Talk Title"
    private val talkRoom = "Talk Room"
    private val startTime = "2018-03-26T15:00:00.000Z"
    private val endTime = "2018-03-26T15:45:00.000Z"
    private val scheduleId = "Schedule ID"
    private val primarySpeakerName = "John"
    private val secondarySpeakerName = "Bob"
    private val speakerOrg = "Speaker Org"
    private val speakerPhoto = "Speaker Photo"
    private val speakerNames = listOf(primarySpeakerName, secondarySpeakerName)

    private val scheduleRow = Schedule.ScheduleRow(
        talkTitle = talkTitle,
        room = talkRoom,
        startTime = startTime,
        endTime = endTime,
        id = scheduleId,
        speakerNames = speakerNames,
        primarySpeakerName = primarySpeakerName,
        photoUrlMap = hashMapOf(primarySpeakerName to speakerPhoto),
        speakerNameToOrgName = hashMapOf(primarySpeakerName to speakerOrg)
    )

    private lateinit var mockAgendaRepo: UserAgendaRepo
    private lateinit var viewModel: AgendaDetailViewModel

    @JvmField
    @Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val mockContext = mock(Context::class.java)
        val mockPreferences = mock(SharedPreferences::class.java)
        val mockEditor = mock(SharedPreferences.Editor::class.java)

        `when`(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPreferences)
        `when`(mockPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putStringSet(anyString(), any())).thenReturn(mockEditor)

        mockAgendaRepo = UserAgendaRepo.getInstance(mockContext)
        viewModel = AgendaDetailViewModel(scheduleRow, mockAgendaRepo, mockFirebaseHelper)
    }

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
    fun isBookmarked() {
        val eventSpeaker = FirebaseDatabase.EventSpeaker(name = primarySpeakerName)
        FirebaseHelperRobot(mockFirebaseHelper).mockSpeakers(listOf(eventSpeaker))

        mockAgendaRepo.bookmarkSession(scheduleId, true)

        viewModel.loadData()
        assertTrue(viewModel.isBookmarked)
        assertEquals(R.color.colorAccent, viewModel.bookmarkColorRes)
    }

    @Test
    fun isNotBookmarked() {
        val eventSpeaker = FirebaseDatabase.EventSpeaker(name = primarySpeakerName)
        FirebaseHelperRobot(mockFirebaseHelper).mockSpeakers(listOf(eventSpeaker))

        mockAgendaRepo.bookmarkSession(scheduleId, false)

        viewModel.loadData()
        assertFalse(viewModel.isBookmarked)
        assertEquals(R.color.colorLightGray, viewModel.bookmarkColorRes)
    }

    @Test
    fun loadData() {
        val eventSpeaker = FirebaseDatabase.EventSpeaker(name = primarySpeakerName)
        FirebaseHelperRobot(mockFirebaseHelper).mockSpeakers(listOf(eventSpeaker))

        viewModel.loadData()

        val scheduleDetail = eventSpeaker.toScheduleDetail(scheduleRow)
        assertEquals(scheduleDetail, viewModel.scheduleDetail.value)
    }

    @Test
    fun getSpeakerWithSpeaker() {
        val eventSpeaker = FirebaseDatabase.EventSpeaker(name = primarySpeakerName)
        FirebaseHelperRobot(mockFirebaseHelper).mockSpeakers(listOf(eventSpeaker))

        viewModel.loadData()
        assertEquals(eventSpeaker, viewModel.getSpeaker(primarySpeakerName))
    }

    @Test
    fun getSpeakerNoSpeaker() {
        assertNull(viewModel.getSpeaker("blahblahblah"))
    }

    @Test
    fun getOrganizationForSpeaker() {
        assertEquals(speakerOrg, viewModel.getOrganizationForSpeaker(primarySpeakerName))
    }

    @Test
    fun getPhotoForSpeaker() {
        assertEquals(speakerPhoto, viewModel.getPhotoForSpeaker(primarySpeakerName))
    }

    @Test
    fun toggleBookmark() {
        val eventSpeaker = FirebaseDatabase.EventSpeaker(name = primarySpeakerName)
        FirebaseHelperRobot(mockFirebaseHelper).mockSpeakers(listOf(eventSpeaker))

        viewModel.loadData()
        assertFalse(viewModel.isBookmarked)

        viewModel.toggleBookmark()

        assertTrue(viewModel.isBookmarked)
    }
}