package com.pirataram.calendarcustom.tools

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pirataram.calendarcustom.BuildConfig
import kotlinx.coroutines.*
import java.util.*

class Constants {
    companion object {
        val TAG = Constants::class.java.simpleName
        const val transparency = 0.8f
        const val eventStart = 1
        const val colsStart = 0
        const val divisorPadding = 2
        val calendarChanged = MutableLiveData<Calendar>()
        private var isActiveCoroutine = false

        val asd = GlobalScope.async(Dispatchers.Main + CoroutineName("pirataCoroutine")) {
            while (true) {
                val timenow = Calendar.getInstance()
                if (calendarChanged.value == null)
                    calendarChanged.postValue(Calendar.getInstance())
                else {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "Handler call !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    if (calendarChanged.value!![Calendar.MINUTE] != timenow[Calendar.MINUTE])
                        calendarChanged.postValue(timenow)
                }
                delay(3000)
            }
        }

        fun startCoroutineCalendar() {
            if (!isActiveCoroutine) {
                isActiveCoroutine = true
                Log.d(TAG, "Launching coroutine...")
                asd.start()
            }
        }
    }
}