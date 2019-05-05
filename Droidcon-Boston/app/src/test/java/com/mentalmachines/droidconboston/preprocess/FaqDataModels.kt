package com.mentalmachines.droidcon_boston.preprocess

data class CsvFaqModel(
        val Answers: String,
        val other_link: String?,
        val photo_link: String?,
        val map_link: String?
)

data class FaqsModel(
        val question: String,
        val answers: Map<Integer,FaqModel>
)

data class FaqModel(
        val answer: String,
        val otherLink: String?,
        val photoLink: String?,
        val mapLink: String?
) {
    constructor(csvFaqModel: CsvFaqModel): this(
            csvFaqModel.Answers,
            csvFaqModel.other_link,
            csvFaqModel.photo_link,
            csvFaqModel.map_link
    )
}
