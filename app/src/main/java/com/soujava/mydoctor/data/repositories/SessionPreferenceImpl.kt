package com.soujava.mydoctor.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.soujava.mydoctor.domain.models.Profile
import com.soujava.mydoctor.domain.contract.ILocalRepository


class SessionPreferenceImpl(context: Context) : ILocalRepository {
    companion object {
        private const val PREFS_NAME = "SessionPrefs"
        private const val KEY_PROFILE = "profile"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    override fun saveProfile(profile: Profile) {
        val profileJson = gson.toJson(profile)
        prefs.edit().putString(KEY_PROFILE, profileJson).apply()
    }

    override fun getProfile(): Profile? {
        val profileJson = prefs.getString(KEY_PROFILE, null)
        return if (profileJson != null) {
            gson.fromJson(profileJson, Profile::class.java)
        } else {
            null
        }
    }

    override fun clear() {
        prefs.edit().remove(KEY_PROFILE).apply()
    }
}
