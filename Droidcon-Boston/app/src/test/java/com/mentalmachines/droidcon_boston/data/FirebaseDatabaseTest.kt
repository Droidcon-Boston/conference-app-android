package com.mentalmachines.droidcon_boston.data

import org.junit.Assert.*
import org.junit.Test
import org.threeten.bp.ZoneOffset.UTC
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class FirebaseDatabaseTest {

    private val firebaseDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S'Z'")

    private val notifTime = 10L
    private val primarySpeakerName = "Primary Speaker"
    private val startTime = "2018-03-26T15:00:00.000Z"
    private val startTimeFormatted = "11:00 am"
    private val dateFormatted = "03/26/2018"
    private val talkTitle = "Talk Name"
    private val speakerOrg = "Speaker Org"
    private val speakerPicture = "Picture URL"
    private val speakerNames = hashMapOf(primarySpeakerName to true, "Second Speaker" to true)
    private val speakerNamesToPhotos =
        hashMapOf(primarySpeakerName to speakerPicture, "Second" to "Second Photo")
    private val speakerNameToOrg =
        hashMapOf(primarySpeakerName to speakerOrg, "Second Speaker" to "Second Company")
    private val roomNames = hashMapOf("Talk Room" to true)
    private val speakerIds = hashMapOf("SpeakerOne" to true, "SpeakerTwo" to true)
    private val roomIds = hashMapOf("RoomOne" to true)
    private val description = "Talk Description"
    private val photo = hashMapOf("Photo" to "Photo")
    private val endTime = "2018-03-26T15:45:00.000Z"
    private val endTimeFormatted = "11:45 am"
    private val trackSortOrder = 0
    private val scheduleId = "ScheduleId"
    private val speakerBio = "Speaker Bio"
    private val facebookProfile = "Facebook Profile"
    private val linkedinProfile = "LinkedIn Profile"
    private val twitterProfile = "Twitter Profile"
    private val socialProfiles = hashMapOf(
        "facebook" to facebookProfile, "linkedIn" to
                linkedinProfile,
        "twitter" to twitterProfile
    )

    private fun createDummyScheduleEvent(): FirebaseDatabase.ScheduleEvent {
        return FirebaseDatabase.ScheduleEvent(
                notifTime,
                primarySpeakerName,
                startTime,
                talkTitle,
                speakerNames,
                speakerNamesToPhotos,
                speakerNameToOrg,
                roomNames,
                speakerIds,
                roomIds,
                description,
                photo,
                endTime,
                trackSortOrder
        )
    }

    @Test
    fun scheduleEventToScheduleRow() {

        val testEvent = createDummyScheduleEvent()

        val testRow = testEvent.toScheduleRow(scheduleId)

        assertEquals(primarySpeakerName, testRow.primarySpeakerName)
        assertEquals(scheduleId, testRow.id)
        assertEquals(startTimeFormatted, testRow.startTime)
        assertEquals(talkTitle, testRow.talkTitle)
        assertEquals(speakerNames.count(), testRow.speakerCount)
        assertEquals(description, testRow.talkDescription)
        assertEquals(speakerNames.keys.toList(), testRow.speakerNames)
        assertEquals(speakerNameToOrg, testRow.speakerNameToOrgName)
        assertEquals(startTime, testRow.utcStartTimeString)
        assertEquals(endTimeFormatted, testRow.endTime)
        assertEquals(roomNames.keys.first(), testRow.room)
        assertEquals(dateFormatted, testRow.date)
        assertEquals(trackSortOrder, testRow.trackSortOrder)
        assertEquals(speakerNamesToPhotos, testRow.photoUrlMap)
        assertTrue(testRow.isOver)
    }

    @Test
    fun scheduleEventToScheduleRow_isCurrentSession_true_whenNowIsBetweenStartAndEndTime() {

        val now = ZonedDateTime.now(UTC)
        val startTime = now.minusMinutes(30)
        val endTime = now.plusMinutes(30)

        val testEvent = createDummyScheduleEvent().copy(
                startTime = startTime.format(firebaseDateFormatter),
                endTime = endTime.format(firebaseDateFormatter)
        )

        val testRow = testEvent.toScheduleRow(scheduleId)

        assertTrue(testRow.isCurrentSession)
    }


    @Test
    fun scheduleEventToScheduleRow_isCurrentSession_false_whenNowIsBeforeStartTime() {

        val now = ZonedDateTime.now(UTC)
        val startTime = now.plusMinutes(30)
        val endTime = now.plusMinutes(90)

        val testEvent = createDummyScheduleEvent().copy(
                startTime = startTime.format(firebaseDateFormatter),
                endTime = endTime.format(firebaseDateFormatter)
        )

        val testRow = testEvent.toScheduleRow(scheduleId)

        assertFalse(testRow.isCurrentSession)
    }

    @Test
    fun scheduleEventToScheduleRow_isCurrentSession_false_whenNowIsAfterEndTime() {

        val now = ZonedDateTime.now(UTC)
        val startTime = now.minusMinutes(90)
        val endTime = now.minusMinutes(30)

        val testEvent = createDummyScheduleEvent().copy(
                startTime = startTime.format(firebaseDateFormatter),
                endTime = endTime.format(firebaseDateFormatter)
        )

        val testRow = testEvent.toScheduleRow(scheduleId)

        assertFalse(testRow.isCurrentSession)
    }


    @Test
    fun eventSpeakerToScheduleDetail() {
        val testEventSpeaker = FirebaseDatabase.EventSpeaker(
            speakerPicture,
            socialProfiles,
            speakerBio,
            talkTitle,
            speakerOrg,
            primarySpeakerName
        )

        val scheduleRow = Schedule.ScheduleRow()
        val scheduleDetail = testEventSpeaker.toScheduleDetail(scheduleRow)
        assertEquals(scheduleRow.id, scheduleDetail.id)
        assertEquals(facebookProfile, scheduleDetail.facebook)
        assertEquals(linkedinProfile, scheduleDetail.linkedIn)
        assertEquals(twitterProfile, scheduleDetail.twitter)
        assertEquals(speakerBio, scheduleDetail.speakerBio)
    }
}