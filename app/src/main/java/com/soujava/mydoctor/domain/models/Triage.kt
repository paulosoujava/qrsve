package com.soujava.mydoctor.domain.models

object Triage{
    var patient = Patient()

    fun clearPatient(){
        patient = Patient()
    }
}