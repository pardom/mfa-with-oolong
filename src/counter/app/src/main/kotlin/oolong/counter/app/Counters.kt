package oolong.counter.app

import oolong.Dispatch
import oolong.Effect
import oolong.counter.data.GetCount
import oolong.counter.data.GetCounts
import oolong.counter.data.PutCount
import oolong.counter.data.PutCounts
import oolong.dispatch.contramap
import oolong.effect
import oolong.effect.map
import oolong.effect.none
import oolong.update

typealias CounterId = Int

object Counters {

    // INIT

    data class Model(
        val counters: Map<CounterId, Counter.Model>
    )

    val init: (GetCounts) -> () -> Pair<Model, Effect<Msg>> =
        { getCounts ->
            {
                Model(counters = emptyMap()) to getCountsEffect(getCounts)
            }
        }

    private val nextCounterId: (Model) -> CounterId =
        { model ->
            model.counters.keys
                .maxOrNull()
                ?.plus(1)
                ?: 0
        }

    // UPDATE

    sealed class Msg {

        data class SetCounts(
            val counts: Map<CounterId, Int>
        ) : Msg()

        object AddCounter : Msg()

        data class RemoveCounter(
            val counterId: CounterId
        ) : Msg()

        data class CounterMsg(
            val counterId: CounterId,
            val msg: Counter.Msg
        ) : Msg()

    }

    val update: (GetCounts, PutCounts) -> (Msg, Model) -> Pair<Model, Effect<Msg>> =
        { getCounts, putCounts ->
            val updateAddCounter = updateAddCounter(getCounts)
            val updateCounterMsg = updateCounterMsg(putCounts)
            val updateRemoveCounter = updateRemoveCounter(putCounts)
            update { msg, model ->
                when (msg) {
                    is Msg.SetCounts -> updateSetCounts(msg, model)
                    is Msg.AddCounter -> updateAddCounter(msg, model)
                    is Msg.RemoveCounter -> updateRemoveCounter(msg, model)
                    is Msg.CounterMsg -> updateCounterMsg(msg, model)
                }
            }
        }

    private val updateSetCounts: (Msg.SetCounts, Model) -> Pair<Model, Effect<Msg>> =
        { msg, model ->
            val counters = msg.counts
                .mapValues { (_, count) ->
                    Counter.Model(count = count)
                }
            model.copy(counters = counters) to none()
        }

    private val updateAddCounter: (GetCounts) -> (Msg.AddCounter, Model) -> Pair<Model, Effect<Msg>> =
        { getCounts ->
            { _, model ->
                val counterKey = nextCounterId(model)
                val getCount: GetCount = { getCounts()[counterKey] ?: 0 }
                val (counterModel, counterEffect) = Counter.init(getCount)()
                model.copy(
                    counters = model.counters + (counterKey to counterModel),
                ) to map(counterEffect, { msg -> Msg.CounterMsg(counterKey, msg) })
            }
        }

    private val updateRemoveCounter: (PutCounts) -> (Msg.RemoveCounter, Model) -> Pair<Model, Effect<Msg>> =
        { putCounts ->
            val putCountsEffect = putCountsEffect(putCounts);
            { msg, model ->
                val counters = model.counters - msg.counterId
                val counts = counters.mapValues { (_, value) -> value.count }
                model.copy(counters = counters) to putCountsEffect(counts)
            }
        }

    private val updateCounterMsg: (PutCounts) -> (Msg.CounterMsg, Model) -> Pair<Model, Effect<Msg>> =
        { putCounts ->
            { msg, model ->
                val counterId = msg.counterId
                val value = model.counters[msg.counterId]
                if (value != null) {
                    val putCount: PutCount =
                        { count ->
                            val counts = model.counters
                                .mapValues { (_, value) -> value.count }
                                .plus(counterId to count)
                            putCounts(counts)
                        }
                    val counterUpdate = Counter.update(putCount)
                    val (counterModel, counterEffect) = counterUpdate(msg.msg, value)
                    model.copy(
                        counters = model.counters + (counterId to counterModel)
                    ) to map(counterEffect) { counterMsg ->
                        Msg.CounterMsg(counterId, counterMsg)
                    }
                } else model to none()
            }
        }

    // VIEW

    data class Props(
        val counters: List<CounterProps>,
        val addCounter: (Dispatch<Msg>) -> Unit,
    ) {

        data class CounterProps(
            val props: Counter.Props,
            val dispatch: (Dispatch<Msg>) -> Dispatch<Counter.Msg>,
            val removeCounter: (Dispatch<Msg>) -> Unit,
        )
    }

    val view: (Model) -> Props =
        { model ->
            Props(
                counters = viewCounters(model.counters),
                addCounter = { dispatch -> dispatch(Msg.AddCounter) },
            )
        }

    private val viewCounters: (Map<CounterId, Counter.Model>) -> List<Props.CounterProps> =
        { counters ->
            counters.map { (counterId, model) ->
                viewCounter(counterId, model)
            }
        }

    private val viewCounter: (CounterId, Counter.Model) -> Props.CounterProps =
        { counterId, model ->
            Props.CounterProps(
                props = Counter.view(model),
                dispatch = { dispatch ->
                    contramap(dispatch) { counterMsg -> Msg.CounterMsg(counterId, counterMsg) }
                },
                removeCounter = { dispatch ->
                    dispatch(Msg.RemoveCounter(counterId))
                },
            )
        }

    // EFFECT

    private val getCountsEffect: (GetCounts) -> Effect<Msg> =
        { getCounts ->
            effect { dispatch ->
                val counts = getCounts()
                dispatch(Msg.SetCounts(counts))
            }
        }

    private val putCountsEffect: (PutCounts) -> (Map<CounterId, Int>) -> Effect<Msg> =
        { putCounts ->
            { counts ->
                effect { putCounts(counts) }
            }
        }

}