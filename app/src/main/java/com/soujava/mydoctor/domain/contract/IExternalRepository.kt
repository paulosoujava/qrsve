package com.soujava.mydoctor.domain.contract

import com.soujava.mydoctor.domain.models.Data
import com.soujava.mydoctor.domain.models.History
import com.soujava.mydoctor.domain.models.MedicalPrescription
import com.soujava.mydoctor.domain.models.Patient
import com.soujava.mydoctor.domain.models.Profile

interface IExternalRepository {

    fun saveInStore(patient: Patient, onResult: (Boolean) -> Unit)
    fun getInStore( uid: String, onError: (String) -> Unit, onResult: (List<Patient>) -> Unit)
    fun getCode(
        code:String,  onError: (String) -> Unit, onResult: (Patient) -> Unit)
    fun saveProfile(
        profile: Profile,
        onResult: (Boolean) -> Unit
    )
    fun saveHistoryInStore(data: Data, onResult: (Boolean) -> Unit)
    fun getHistoryInStore( onResult: (List<History>) -> Unit)

    fun addMedications(list: List<MedicalPrescription>, onResult: (Boolean) -> Unit)
    fun getMedications( onResult: (List<MedicalPrescription>) -> Unit)
    fun getUID(onResult: (String?) -> Unit)
}