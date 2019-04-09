package com.mentalmachines.droidconboston.data

import com.mentalmachines.droidconboston.data.Schedule.ScheduleRow
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

    @Test
    fun containsKeywordInTitle() {
        val keyword = "Kotlin"
        val testTitle = "All About $keyword"

        val row = ScheduleRow(talkTitle = testTitle)
        assertTrue(row.containsKeyword(keyword))
    }

    @Test
    fun containsKeywordInDescription() {
        val keyword = "Kotlin"
        val testDescription = "All about $keyword"

        val row = ScheduleRow(talkDescription = testDescription)
        assertTrue(row.containsKeyword(keyword))
    }

    @Test
    fun containsKeywordInSpeakerName() {
        val keyword = "Kotlin"
        val speakers = listOf("Speaker about $keyword")

        val row = ScheduleRow(speakerNames = speakers)
        assertTrue(row.containsKeyword(keyword))
    }

    @Test
    fun containsEmptyKeyword() {
        val row = ScheduleRow()
        assertTrue(row.containsKeyword(""))
    }

    @Test
    fun doesNotContainKeyword() {
        val row = ScheduleRow()
        assertFalse(row.containsKeyword("Blah"))
    }
}
