package com.soujava.mydoctor.domain.models

data class JsonItem(
    val type: String,
    val typeField: String,
    val label: String,
    val options: List<String>? = emptyList()

)

data class JsonTriage(
    val items: List<JsonItem>,
    val hiddenToDoctor: String="",
    val messageToEmptyJson: String = ""
)