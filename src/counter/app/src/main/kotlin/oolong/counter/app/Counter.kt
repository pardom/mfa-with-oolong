package oolong.counter.app

import oolong.Dispatch
import oolong.Effect
import oolong.effect.none

object Counter {

    // INIT

    data class Model(
        val count: Int
    )

    val init: () -> Pair<Model, Effect<Msg>> =
        {
            Model(count = 0) to none()
        }

    // UPDATE

    sealed class Msg {
        object Increment : Msg()
        object Decrement : Msg()
    }

    val update: (Msg, Model) -> Pair<Model, Effect<Msg>> =
        { msg, model ->
            when (msg) {
                Msg.Increment -> model.copy(count = model.count + 1)
                Msg.Decrement -> model.copy(count = model.count - 1)
            } to none()
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

}
