package com.mentalmachines.droidcon_boston.data

import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

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
    fun getSpeakerStringNoSpeaker() {
        val row = ScheduleRow()
        assertEquals("", row.getSpeakerString())
    }

    @Test
    fun getSpeakerStringOneSpeaker() {
        val row = ScheduleRow()
        row.speakerNames = Arrays.asList(s1)
        assertEquals(s1, row.getSpeakerString())
    }

    @Test
    fun getSpeakerStringMultipleSpeakers() {
        val row = ScheduleRow()
        row.speakerNames = Arrays.asList(s1, s2)
        assertEquals("$s1, $s2", row.getSpeakerString())
    }
}
