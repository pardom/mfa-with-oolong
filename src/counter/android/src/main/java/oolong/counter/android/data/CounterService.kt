package oolong.counter.android.data

import android.content.Context
import oolong.counter.data.GetCount
import oolong.counter.data.PutCount

class CounterService(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("counter", Context.MODE_PRIVATE)

    val getCount: GetCount =
        {
            sharedPreferences.getInt("count", 0)
        }

    val putCount: PutCount =
        { count ->
            sharedPreferences.edit()
                .putInt("count", count)
                .apply()
        }

}