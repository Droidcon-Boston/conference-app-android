package com.mentalmachines.droidcon_boston.data;

/**
 * modified from data class to have constructors, as required by Firebase
 * https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot#getValue(java.lang.Class<T>)
 */

class EventClass() {
    val name: String? = null
    val description: String? = null
    val duration: String? = null
    val startTime: String? = null
    val roomIds: Map<String, Boolean>? = null
    val speakerIds: Map<String, Boolean>? = null
    val trackId: String? = ""
    var roomNames: Map<String, Boolean>? = null
    var speakerNames: Map<String, Boolean>? = null
    var trackName: String? = null
    val updatedAt: Long = 0L
}

class SpeakerData() {
    val name: String = ""
    val lastName: String = ""
    val firstName: String = ""
    val title: String? = null
    val org: String? = null
    val bio: String = ""
    val pictureId: String? = null
    val pictureUrl: String? = null //picture is not optional
    val isFeatured: Boolean = false
    val socialProfiles: Map<String, String>? = null
    val updatedAt: Long = 0L
}