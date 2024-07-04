package com.soujava.mydoctor.domain.models

data class VoiceParseToState(
    var spokenText: String ="",
    val isSpeaking: Boolean = false,
    val error: String ? = null
)
