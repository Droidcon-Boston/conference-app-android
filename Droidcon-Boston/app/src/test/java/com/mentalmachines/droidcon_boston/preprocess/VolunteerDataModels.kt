package com.mentalmachines.droidcon_boston.preprocess

import com.squareup.moshi.Json

data class CsvVolunteerModel(
        @Json(name = "First Name")
        val firstName: String,
        @Json(name = "Last Name")
        val lastName: String,
        @Json(name = "Position")
        val position: String,
        @Json(name = "Email")
        val email: String,
        @Json(name = "Twitter")
        val twitter: String,
        @Json(name = "PhotoUrl")
        val photoUrl: String?
)

data class VolunteerModel(
        val firstName: String,
        val lastName: String,
        val position: String,
        val email: String,
        val twitter: String?,
        val pictureUrl: String?
) {
    constructor(csvModel: CsvVolunteerModel): this(
            csvModel.firstName,
            csvModel.lastName,
            csvModel.position,
            csvModel.email,
            if ("".equals(csvModel.twitter) || "---".equals(csvModel.twitter)) null else csvModel.twitter,
            csvModel.photoUrl
    )
}
