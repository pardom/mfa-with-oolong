package oolong.counter.android.oolong

import oolong.Dispatch

class RenderDelegate<Msg, Props> {

    private var delegate: ((Props, Dispatch<Msg>) -> Any?)? = null

    private var props: Props? = null
    private var dispatch: Dispatch<Msg>? = null

    val render: (props: Props, dispatch: Dispatch<Msg>) -> Any? =
        { props, dispatch ->
            this.props = props
            this.dispatch = dispatch
            delegate?.invoke(props, dispatch)
        }

    fun setRender(render: (props: Props, dispatch: Dispatch<Msg>) -> Any?) {
        delegate = render
        props?.let { props ->
            dispatch?.let { dispatch ->
                delegate?.let { delegate ->
                    delegate(props, dispatch)
                }
            }
        }
    }

    fun clearRender() {
        delegate = null
    }

}