package com.soujava.mydoctor.domain.models

data class History(
    val createdAt: String,
    val type: String,
    val uid: String,
    val data: Data,

)

data class Data(
    val nameOfDoctor: String,
    val crm: String,
    val nameOfPatient: String,
    val dateThisExam: String,
    val resume: String,
)