package com.soujava.mydoctor.domain.contract

import com.soujava.mydoctor.domain.models.Profile

interface ILocalRepository {
    fun saveProfile(profile: Profile)
    fun getProfile(): Profile?
    fun clear()
}
