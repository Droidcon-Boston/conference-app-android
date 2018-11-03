package com.mentalmachines.droidcon_boston.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.mentalmachines.droidcon_boston.utils.SingletonHolder

class UserAgendaRepo private constructor(context: Context) {
    private val prefsKey = "UserAgenda"
    private val sessionIdsKey = "savedSessionsIds"
    private val sharedPrefs : SharedPreferences = context.getSharedPreferences(prefsKey, MODE_PRIVATE)
    private val savedSessionIds = HashSet<String>()

    init {
        savedSessionIds += sharedPrefs.getStringSet(sessionIdsKey, HashSet<String>())
    }

    fun isSessionBookmarked(sessionId : String) : Boolean {
        return savedSessionIds.contains(sessionId)
    }

    fun bookmarkSession(sessionId : String, flag : Boolean) {
        if (flag) {
            savedSessionIds.add(sessionId)
        } else {
            savedSessionIds.remove(sessionId)
        }
        sharedPrefs.edit().putStringSet(sessionIdsKey, savedSessionIds).apply()
    }

    companion object : SingletonHolder<UserAgendaRepo, Context>(::UserAgendaRepo)
}
