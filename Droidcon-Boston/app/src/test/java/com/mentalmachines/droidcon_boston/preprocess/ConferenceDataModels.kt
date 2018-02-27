package com.mentalmachines.droidcon_boston.preprocess

import java.time.Duration
import java.util.*

data class ConferenceDataModel(
        val events: Map<String, EventModel>,
        val rooms: Map<String, RoomModel>,
        val sections: Map<String, SectionModel>,
        val speakers: Map<String, SpeakerModel>,
        val tracks: Map<String, TrackModel>
)

data class EventModel(
        val name: String,
        val description: String,
        val duration: Duration,
        val isGeneralEvent: Boolean,
        val isPublished: Boolean,
        val startTime: Date,
        var endTime: Date?,
        val roomIds: Map<String, Boolean>?,
        val speakerIds: Map<String, Boolean>?,
        val trackId: String?,
        var roomNames: Map<String, Boolean>?,
        var speakerNames: Map<String, Boolean>?,
        var speakerNameToPhotoUrl: Map<String, String>?,
        var speakerNameToOrg: Map<String, String>?,
        var trackName: String?,
        var trackSortOrder: Int?,
        val updatedAt: Long,
        val updatedBy: String
)

data class RoomModel(
        val name: String,
        val updatedAt: Long,
        val updatedBy: String
)

data class SectionModel(
        val name: String,
        val title: String,
        val startTime: Date,
        val endTime: Date,
        val updatedAt: Long,
        val updatedBy: String
)

data class SpeakerModel(
        val name: String,
        var firstName: String?,
        var lastName: String?,
        val title: String?,
        val org: String?,
        val bio: String,
        val pictureId: String?,
        val pictureUrl: String?,
        val isFeatured: Boolean,
        val socialProfiles: Map<String,String>?,
        val updatedAt: Long,
        val updatedBy: String
)

data class TrackModel(
        val name: String,
        val description: String,
        val sortOrder: Int,
        val updatedAt: Long,
        val updatedBy: String
)