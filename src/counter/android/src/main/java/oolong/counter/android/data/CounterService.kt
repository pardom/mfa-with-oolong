package oolong.counter.android.data

import android.content.Context
import oolong.counter.data.GetCounts
import oolong.counter.data.PutCounts

class CounterService(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("counter", Context.MODE_PRIVATE)

    val getCounts: GetCounts =
        {
            val serialized = sharedPreferences.getString(COUNTS_KEY, "") ?: ""
            serialized.split(";")
                .map { entry -> entry.split(":") }
                .filter { entry -> entry.size == 2 }
                .map { entry -> entry[0].toInt() to entry[1].toInt() }
                .toMap()
        }

    val putCounts: PutCounts =
        { counts ->
            val serialized = counts
                .map { (key, value) -> "$key:$value" }
                .joinToString(";")
            sharedPreferences.edit()
                .putString(COUNTS_KEY, serialized)
                .apply()
        }

    companion object {
        private const val COUNTS_KEY = "counts"
    }

}