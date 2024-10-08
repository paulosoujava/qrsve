package com.soujava.mydoctor.data.repositories

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.soujava.mydoctor.core.getFormattedDate
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.models.Data
import com.soujava.mydoctor.domain.models.History
import com.soujava.mydoctor.domain.models.MedicalPrescription
import com.soujava.mydoctor.domain.models.Patient
import com.soujava.mydoctor.domain.models.Profile


const val PROFILE = "profiles"
const val TRIAGE = "triage"
const val HISTORY = "history"
const val MEDICATIONS = "medications"

class FirestoreImpl : IExternalRepository {



    override fun getUID(onResult: (String?) -> Unit) {
        val session = Firebase.auth.currentUser?.uid
        onResult(session)
    }

    override fun getMedications(onResult: (List<MedicalPrescription>) -> Unit) {
        val uid = Firebase.auth.currentUser?.uid
        Firebase.firestore.collection(MEDICATIONS).whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener {
                onResult(it.toObjects(MedicalPrescription::class.java))
            }
    }


    override fun addMedications(list: List<MedicalPrescription>, onResult: (Boolean) -> Unit) {
        val medicationsCollection = Firebase.firestore.collection(MEDICATIONS)
        list.forEach { medication ->
            medicationsCollection.add(medication)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }
        }
    }


    override fun saveProfile(
        profile: Profile,
        onResult: (Boolean) -> Unit
    ) {
        profile.userAuth?.let {
            Firebase.firestore.collection(PROFILE).document(it.uid)
                .set(profile)
                .addOnCompleteListener { data ->
                    onResult(data.isSuccessful)
                }
        } ?: run {
            onResult(false)
        }

    }

    override fun saveHistoryInStore(data: Data, onResult: (Boolean) -> Unit) {

        val session = Firebase.auth.currentUser?.uid

        val history = History(
            createdAt = getFormattedDate(),
            type = "IMAGE_CONTENT",
            uid = session.toString(),
            data = data
        )


        Firebase.firestore.collection(HISTORY)
            .add(history)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
    }

    override fun getHistoryInStore(onResult: (List<History>) -> Unit) {
        val session = Firebase.auth.currentUser?.uid
        Firebase.firestore.collection(HISTORY)
            .whereEqualTo("uid", session)
            .whereEqualTo("type", "IMAGE_CONTENT")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val historyList = mutableListOf<History>()

                    for (document in task.result) {
                        Log.d("TAG", "getHistoryInStore: ${document.data["data"]}")
                        val createdAt = document.data["createdAt"].toString()
                        val uid = document.data["uid"].toString()
                        val type = document.data["type"].toString()

                        val map = document.data["data"] as Map<*, *>

                        val myPersonaData = Data(
                            nameOfDoctor = map["nameOfDoctor"].toString(),
                            crm = map["crm"].toString(),
                            nameOfPatient = map["nameOfPatient"].toString(),
                            dateThisExam = map["dateThisExam"].toString(),
                            resume = map["resume"].toString()
                        )

                        Log.d("TAG", "getHistoryInStore: $myPersonaData")
                        historyList.add(
                            History(
                                createdAt = createdAt,
                                type = type,
                                uid = uid,
                                data = myPersonaData
                            )
                        )
                    }
                    onResult(historyList)
                } else {
                    onResult(emptyList())
                }
            }
    }

    override fun saveInStore(patient: Patient, onResult: (Boolean) -> Unit) {
        Firebase.firestore.collection(TRIAGE)
            .add(patient)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
    }

    override fun getInStore(
        uid: String,
        onError: (String) -> Unit, onResult: (List<Patient>) -> Unit
    ) {
        Firebase.firestore.collection(TRIAGE).whereEqualTo("uid", uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(it.result.toObjects(Patient::class.java))
                } else {
                    Log.d("TAG", "getInStore: ${it.exception?.message}")
                    onError(it.exception?.message.toString())
                }
            }
    }

    override fun getCode(
        code: String,
        onError: (String) -> Unit,
        onResult: (Patient) -> Unit
    ) {
        Firebase.firestore.collection(TRIAGE).whereEqualTo("code", code)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.isEmpty)
                        onError("Código inválido")
                    else
                        onResult(it.result.first().toObject(Patient::class.java)!!)
                } else {
                    Log.d("TAG", "getInStore: ${it.exception?.message}")
                    onError(it.exception?.message.toString())
                }
            }
    }
}