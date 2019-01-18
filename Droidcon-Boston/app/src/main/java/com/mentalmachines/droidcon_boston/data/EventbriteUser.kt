package com.mentalmachines.droidcon_boston.data

import com.google.gson.annotations.SerializedName

data class EventbriteUser(
    var id: String?,
    var profile: EventbriteProfile,
    var answers: List<EventbriteQuestion>
)

data class EventbriteProfile(
    var first_name: String,
    var last_name: String,
    var company: String,
    var name: String,
    var email: String,
    var job_title: String
)

data class EventbriteQuestion(
    var answer: String?,
    var question: String,
    //20940481 is the question_id for the twitter handle in the response.
    var question_id: String,
    var type: QuestionType?
)

enum class QuestionType {
    @SerializedName("mutliple_choice")
    MultipleChoice,
    @SerializedName("text")
    Text
}