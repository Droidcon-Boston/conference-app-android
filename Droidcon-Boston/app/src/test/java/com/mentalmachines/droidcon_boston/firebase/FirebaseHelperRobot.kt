package com.mentalmachines.droidcon_boston.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock

/**
 * A robot class that can be used to extract out firebase functionality from the tests.
 *
 * Mainly we can use this to extract mocking.
 */
class FirebaseHelperRobot(mockFirebaseHelper: FirebaseHelper) {
    private val mockSpeakerReference = mock(DatabaseReference::class.java)

    init {
        `when`(mockFirebaseHelper.speakerDatabase).thenReturn(mockSpeakerReference)
        `when`(mockSpeakerReference.orderByChild(anyString())).thenReturn(mockSpeakerReference)
    }

    fun mockSpeakers(speakers: List<FirebaseDatabase.EventSpeaker>): FirebaseHelperRobot {
        val speakerSnapshots = speakers.map { speaker ->
            val mockSnapshot = mock(DataSnapshot::class.java)
            `when`(mockSnapshot.getValue(FirebaseDatabase.EventSpeaker::class.java)).thenReturn(
                speaker
            )
            return@map mockSnapshot
        }

        val mockSnapshot = mock(DataSnapshot::class.java)
        `when`(mockSnapshot.children).thenReturn(speakerSnapshots)

        doAnswer {
            val valueEventListener = it.arguments.first() as ValueEventListener
            valueEventListener.onDataChange(mockSnapshot)
            return@doAnswer null
        }.`when`(mockSpeakerReference).addValueEventListener(any(ValueEventListener::class.java))

        return this
    }
}