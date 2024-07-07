package com.soujava.mydoctor.domain.models

data class MedicalPrescription(
    val nameOfMedications: String = "",
    val duration: String = "",
    val qtdPerDay: Int = 0,
    val doctor: String = "",
    val crm: String = "",
    val dateBegin: String = "",
    val dateEnd: String = "",
    val status: String = "",
    val description: String = "",
    val uid: String =""
)
