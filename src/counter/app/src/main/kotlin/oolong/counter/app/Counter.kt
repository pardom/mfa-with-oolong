package oolong.counter.app

import oolong.Dispatch
import oolong.Effect
import oolong.counter.data.GetCount
import oolong.counter.data.PutCount
import oolong.effect

object Counter {

    // INIT

    data class Model(
        val count: Int
    )

    val init: (GetCount) -> () -> Pair<Model, Effect<Msg>> =
        { getCount ->
            {
                Model(count = 0) to getCountEffect(getCount)
            }
        }

    // UPDATE

    sealed class Msg {
        data class SetCount(val count: Int) : Msg()
        object Increment : Msg()
        object Decrement : Msg()
    }

    val update: (PutCount) -> (Msg, Model) -> Pair<Model, Effect<Msg>> =
        { putCount ->
            val putCountEffect = putCountEffect(putCount);
            { msg, model ->
                val model = when (msg) {
                    is Msg.SetCount -> model.copy(count = msg.count)
                    Msg.Increment -> model.copy(count = model.count + 1)
                    Msg.Decrement -> model.copy(count = model.count - 1)
                }
                model to putCountEffect(model.count)
            }
        }

    // VIEW

    data class Props(
        val count: Int,
        val increment: (Dispatch<Msg>) -> Unit,
        val decrement: (Dispatch<Msg>) -> Unit,
    )

    val view: (Model) -> Props =
        { model ->
            Props(
                count = model.count,
                increment = { dispatch -> dispatch(Msg.Increment) },
                decrement = { dispatch -> dispatch(Msg.Decrement) },
            )
        }

    // EFFECT

    private val getCountEffect: (GetCount) -> Effect<Msg> =
        { getCount ->
            effect { dispatch ->
                val count = getCount()
                dispatch(Msg.SetCount(count))
            }
        }

    private val putCountEffect: (PutCount) -> (Int) -> Effect<Msg> =
        { putCount ->
            { count ->
                effect {
                    putCount(count)
                }
            }
        }
}
