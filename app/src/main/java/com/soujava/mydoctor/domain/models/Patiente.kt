package com.soujava.mydoctor.domain.models

import android.provider.ContactsContract.CommonDataKinds.Phone

data class Patient(
    var uid: String = "",
    var code: String = "",
    var initialResume: String = "",
    var content: String = "",
    var moreInf: String = "",
    var createAt: String = "",
)


