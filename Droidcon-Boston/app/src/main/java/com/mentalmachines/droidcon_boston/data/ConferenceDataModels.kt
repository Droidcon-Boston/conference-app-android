package com.mentalmachines.droidcon_boston.data;

/**
 * modified from data class to have constructors, as required by Firebase
 * https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot#getValue(java.lang.Class<T>)
 */
data class ConferenceDataModel(
        val events: Map<String, EventModel>,
        val rooms: Map<String, RoomModel>,
        val sections: Map<String, SectionModel>,
        val speakers: Map<String, SpeakerModel>,
        val tracks: Map<String, TrackModel>
)

class EventModel() {
    val name: String = ""
    val description: String = ""
    val duration: String = ""
    val isGeneralEvent: Boolean = false
    val isPublished: Boolean = false
    val startTime: String = ""
    val roomIds: Map<String, Boolean>? = null
    val speakerIds: Map<String, Boolean>? = null
    val trackId: String? = ""
    var roomNames: Map<String, Boolean>? = null
    var speakerNames: Map<String, Boolean>? = null
    var trackName: String? = null
    val updatedAt: Long = 0L
    val updatedBy: String = ""
}

class RoomModel() {
    val name: String = ""
    val updatedAt: Long = 0L
    val updatedBy: String = ""
}

class SectionModel() {
    val name: String = ""
    val title: String = ""
    val startTime: String = ""
    val endTime: String = ""
    val updatedAt: Long = 0L
    val updatedBy: String = ""
}

class SpeakerModel() {
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
    val updatedBy: String = ""
}

class TrackModel() {
    val name: String = ""
    val description: String = ""
    val sortOrder: Int = 0
    val updatedAt: Long = 0L
    val updatedBy: String = ""
}