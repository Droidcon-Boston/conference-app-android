package com.mentalmachines.droidcon_boston.utils

import com.google.gson.Gson

class ServiceLocator {

    companion object {
        val gson: Gson = Gson()
    }
}
