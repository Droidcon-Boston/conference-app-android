package com.mentalmachines.droidcon_boston.data

import com.google.firebase.auth.FirebaseUser
import com.mentalmachines.droidcon_boston.firebase.AuthController
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.UUID
import kotlin.collections.HashSet

@RunWith(JUnit4::class)
class UserProfileTest : TestCase() {

    private var localUser: FirebaseDatabase.User? = null

    private var authController = AuthController

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUpTests() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testProfileCreation() {
        val id = UUID.randomUUID().toString()
        Mockito.doReturn("joe@smith.com").`when`(firebaseUser).email
        Mockito.doReturn(id).`when`(firebaseUser).uid
        Mockito.doReturn("Joe Smith").`when`(firebaseUser).displayName

        localUser = authController.createLocalUser(HashSet(), firebaseUser)

        assertNotNull(localUser)
        assertEquals("Joe Smith", localUser?.displayName)
        assertEquals("joe@smith.com", localUser?.username)
        assertEquals(id, localUser?.id)
    }
}
