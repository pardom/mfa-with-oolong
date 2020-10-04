package oolong.counter.android

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import oolong.Dispatch
import oolong.counter.android.App.Companion.clearRender
import oolong.counter.android.App.Companion.setRender
import oolong.counter.android.ui.CountersScreen
import oolong.counter.app.Counters.Msg
import oolong.counter.app.Counters.Props

class AppActivity : AppCompatActivity() {

    private val render: (Props, Dispatch<Msg>) -> Any? =
        { props, dispatch ->
            setContent { CountersScreen(props, dispatch) }
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setRender(render)
    }

    override fun onDetachedFromWindow() {
        clearRender()
        super.onDetachedFromWindow()
    }

}