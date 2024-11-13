package com.sanpc.roadsense.utils

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}