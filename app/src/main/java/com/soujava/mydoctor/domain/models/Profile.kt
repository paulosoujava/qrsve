package com.soujava.mydoctor.domain.models

import android.net.Uri
import android.util.Log


data class UserAuth(
    val displayName: String,
    val uid: String,
    val email: String?,
    val photoUrl: Uri?,
)

data class Profile(
    var name: String? = null,
    var phone: String? = null,
    var cpf: String? = null,
    val userAuth: UserAuth? = null,
    var address: Address? = null,
    var clincal: Clinical? = null,
)

data class Address(
    var address: String? = null,
    var city: String? = null,
    var state: String? = null,
    var cep: String? = null,
    var number: String? = null,
    var complement: String? = null,
    var neiborhood: String? = null,
    var ddd: String? = null,

    )

data class Clinical(
    var hasMedication: Boolean? = null,
    var medication: String? = null,
//I changed the names below because of the error.
//    java.lang.RuntimeException: Found conflicting getters for name isAllergy on class com.soujava.mydoctor.domain.models.Clinical
    var hasAllergy: Boolean? = null,
    var allergyContent: String? = null,
    var hasExercise: Boolean? = null,
    var howManyExercise: String? = null,
)

fun Profile.allFieldsFilled(): Boolean {
    Log.d("PROFILE", "allFieldsFilled: $this")

    return ( this.name != null || this.phone != null || this.cpf != null || this.userAuth != null || this.address != null || this.clincal != null) &&
            this.name != "" &&
            this.phone != "" &&
            this.cpf != "" &&
            this.userAuth != null &&
            this.address?.address != "" &&
            this.address?.city != "" &&
            this.address?.state != "" &&
            this.address?.cep != "" &&
            this.address?.number != "" &&
            this.address?.neiborhood != "" &&
            this.address?.ddd != "" &&

            this.clincal?.hasMedication != null && (this.clincal?.hasMedication == true && this.clincal?.medication?.isNotEmpty() == true || this.clincal?.hasMedication == false && this.clincal?.medication?.isEmpty() == true) &&
            this.clincal?.hasAllergy != null && (this.clincal?.hasAllergy == true && this.clincal?.allergyContent?.isNotEmpty() == true || this.clincal?.hasAllergy == false && this.clincal?.allergyContent?.isEmpty() == true) &&
            this.clincal?.hasExercise != null && (this.clincal?.hasExercise == true && this.clincal?.howManyExercise?.isNotEmpty() == true || this.clincal?.hasExercise == false && this.clincal?.howManyExercise?.isEmpty() == true)



}
