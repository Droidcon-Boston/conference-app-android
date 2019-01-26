package com.mentalmachines.droidcon_boston.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FirebaseDatabaseTest {

    @Test
    fun scheduleEventToScheduleRow() {
        val notifTime = 10L
        val primarySpeakerName = "Primary Speaker"
        val startTime = "2018-03-26T15:00:00.000Z"
        val startTimeFormatted = "11:00 am"
        val dateFormatted = "03/26/2018"
        val name = "Talk Name"
        val speakerNames = hashMapOf("First Speaker" to true, "Second Speaker" to true)
        val speakerNamesToPhotos = hashMapOf("First Speaker" to "First Photo", "Second Speaker" to 
                "Second Photo")
        val speakerNameToOrg = hashMapOf("First Speaker" to "First Company", "Second Speaker" to 
                "Second Company")
        val roomNames = hashMapOf("Talk Room" to true)
        val speakerIds = hashMapOf("SpeakerOne" to true, "SpeakerTwo" to true)
        val roomIds = hashMapOf("RoomOne" to true)
        val description = "Talk Description"
        val photo = hashMapOf("Photo" to "Photo")
        val endTime = "2018-03-26T15:45:00.000Z"
        val endTimeFormatted = "11:45 am"
        val trackSortOrder = 0
        val scheduleId = "ScheduleId"

        val testEvent = FirebaseDatabase.ScheduleEvent(
            notifTime,
            primarySpeakerName,
            startTime, 
            name,
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

        val testRow = testEvent.toScheduleRow(scheduleId)

        assertEquals(primarySpeakerName, testRow.primarySpeakerName)
        assertEquals(scheduleId, testRow.id)
        assertEquals(startTimeFormatted, testRow.startTime)
        assertEquals(name, testRow.talkTitle)
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
    fun eventSpeakerToScheduleDetail() {
        val pictureUrl = "Picture URL"
        val facebookProfile = "Facebook Profile"
        val linkedinProfile = "LinkedIn Profile"
        val twitterProfile = "Twitter Profile"
        val socialProfiles = hashMapOf("facebook" to facebookProfile, "linkedIn" to
                linkedinProfile,
            "twitter" to twitterProfile)
        val bio = "Speaker Bio"
        val title = "Talk Title"
        val org = "Speaker Org"
        val name = "Speaker Name"

        val testEventSpeaker = FirebaseDatabase.EventSpeaker(
            pictureUrl,
            socialProfiles,
            bio,
            title,
            org,
            name
        )

        val scheduleRow = Schedule.ScheduleRow(id = "ScheduleId")
        val scheduleDetail = testEventSpeaker.toScheduleDetail(scheduleRow)
        assertEquals(scheduleRow.id, scheduleDetail.id)
        assertEquals(facebookProfile, scheduleDetail.facebook)
        assertEquals(linkedinProfile, scheduleDetail.linkedIn)
        assertEquals(twitterProfile, scheduleDetail.twitter)
        assertEquals(bio, scheduleDetail.speakerBio)
    }
}