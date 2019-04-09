package com.mentalmachines.droidconboston.data

import com.google.gson.Gson
import com.mentalmachines.droidconboston.utils.ServiceLocator
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.FileInputStream
import java.io.InputStreamReader

@RunWith(JUnit4::class)
class EventbriteTest : TestCase() {

    private val gson: Gson = ServiceLocator.gson

    private var eventbriteUser: EventbriteUser? = null

    @Before
    fun setupTests() {
        eventbriteUser = gson.fromJson(
            InputStreamReader(FileInputStream("sampledata/sample_eb_result.json")),
            EventbriteUser::class.java
        )
    }

    @Test
    fun testUserInitialized() {
        assertNotNull(eventbriteUser)
    }

    @Test
    fun testUserHasTwitter() {
        var handle: String? = ""
        eventbriteUser?.let { user ->
            for (question: EventbriteQuestion in user.answers) {
                if (question.question_id == "20940481") {
                    handle = question.answer
                }
            }
        }
        assertNotNull(handle)
        assertEquals("@ccorrads", handle)
    }

    @Test
    fun testQuestionType() {
        var type: QuestionType? = null
        eventbriteUser?.let { user ->
            for (question: EventbriteQuestion in user.answers) {
                if (question.question_id == "20940481") {
                    type = question.type
                }
            }
        }
        assertEquals(QuestionType.Text, type)
    }
}