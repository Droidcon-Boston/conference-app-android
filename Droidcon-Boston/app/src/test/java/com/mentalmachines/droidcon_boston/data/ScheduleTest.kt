package com.mentalmachines.droidcon_boston.data

import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import org.junit.*
import org.junit.Assert.*
import java.util.Arrays

class ScheduleTest {

    private val s1 = "speaker1"
    private val s2 = "speaker2"

    @Test
    fun hasSpeakerOneSpeaker() {
        val row = ScheduleRow()
        row.speakerNames = Arrays.asList(s1)
        assertTrue(row.hasSpeaker())
    }

    @Test
    fun hasSpeakerNoSpeaker() {
        val row = ScheduleRow()
        row.speakerNames = Arrays.asList()
        assertFalse(row.hasSpeaker())
    }

    @Test
    fun hasMultipleSpeakersOneSpeaker() {
        val row = ScheduleRow()
        row.speakerNames = Arrays.asList(s1)
        assertFalse(row.hasMultipleSpeakers())
    }

    @Test
    fun hasMultipleSpeakersNoSpeaker() {
        val row = ScheduleRow()
        assertFalse(row.hasMultipleSpeakers())
    }

    @Test
    fun hasMultipleSpeakersMultipleSpeaker() {
        val row = ScheduleRow()
        row.speakerNames = Arrays.asList(s1, s2)
        assertTrue(row.hasMultipleSpeakers())
    }

    @Test
    fun getSpeakerStringMultipleSpeakers() {
        val row = ScheduleRow()

        row.speakerNames = Arrays.asList(s1, s2)
        val speakerNameString = row.getSpeakerString()!!
        assertTrue(speakerNameString.contains(s1))
        assertTrue(speakerNameString.contains(s2))
    }
}
