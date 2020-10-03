package oolong.counter.android

import android.app.Application
import android.content.Context
import oolong.Dispatch
import oolong.counter.android.oolong.RenderDelegate
import oolong.counter.app.Counter.Msg
import oolong.counter.app.Counter.Props
import oolong.counter.app.Counter.init
import oolong.counter.app.Counter.update
import oolong.counter.app.Counter.view
import oolong.runtime

class App : Application() {

    private val renderDelegate = RenderDelegate<Msg, Props>()

    override fun onCreate() {
        super.onCreate()
        runtime(
            init,
            update,
            view,
            renderDelegate.render
        )
    }

    companion object {

        fun Context.setRender(render: (Props, Dispatch<Msg>) -> Any?) {
            (applicationContext as App).renderDelegate.setRender(render)
        }

        fun Context.clearRender() {
            (applicationContext as App).renderDelegate.clearRender()
        }

    }

}
