package com.soujava.mydoctor.domain.models

import android.net.Uri


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
    var howManyExercise: Int? = null,
)

fun Profile.allFieldsFilled(): Boolean {
    return name?.isNotEmpty() ?: false &&
            phone?.isNotEmpty() ?: false &&
            cpf?.isNotEmpty() ?: false &&
            userAuth?.run {
                uid?.isNotEmpty() ?: false &&
                        email?.isNotEmpty() ?: false
            } ?: false &&
            address?.run {
                cep?.isNotEmpty() ?: false &&
                        address?.isNotEmpty() ?: false &&
                        city?.isNotEmpty() ?: false &&
                        state?.isNotEmpty() ?: false &&
                        number?.isNotEmpty() ?: false &&
                        neiborhood?.isNotEmpty() ?: false
            } ?: false &&
            clincal?.run {
                hasMedication == true && medication?.isNotEmpty() == true ||
                        hasAllergy == true && allergyContent?.isNotEmpty() == true ||
                        hasMedication == false && medication?.isEmpty() == true &&
                        hasAllergy == false && allergyContent?.isEmpty() == true &&
                        hasExercise == true && howManyExercise != null

            } ?: false
}
