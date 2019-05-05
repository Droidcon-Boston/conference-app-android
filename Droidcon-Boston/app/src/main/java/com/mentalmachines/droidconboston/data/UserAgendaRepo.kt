package com.mentalmachines.droidconboston.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.mentalmachines.droidconboston.utils.SingletonHolder

class UserAgendaRepo private constructor(context: Context) {
    private val prefsKey = "UserAgenda"
    private val sessionIdsKey = "savedSessionsIds"
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(prefsKey, MODE_PRIVATE)
    val savedSessionIds = HashMap<String, String>()

    init {
        savedSessionIds += sharedPrefs.getStringSet(sessionIdsKey, HashSet<String>()).orEmpty()
            .map { it to it }.toMap()
    }

    fun isSessionBookmarked(sessionId: String): Boolean {
        return savedSessionIds.contains(sessionId)
    }

    fun bookmarkSession(sessionId: String, flag: Boolean) {
        if (flag) {
            savedSessionIds.put(sessionId, sessionId)
        } else {
            savedSessionIds.remove(sessionId)
        }
        sharedPrefs.edit().putStringSet(sessionIdsKey, savedSessionIds.values.toSet()).apply()
    }

    companion object : SingletonHolder<UserAgendaRepo, Context>(::UserAgendaRepo)
}
