package oolong.counter.app

import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import oolong.counter.app.Counter.Model
import oolong.counter.app.Counter.Msg
import oolong.counter.app.Counter.view
import oolong.counter.data.GetCount
import oolong.counter.data.PutCount
import org.junit.jupiter.api.Test

class CounterTest {

    private val getCount: GetCount =
        { 0 }

    private val putCount: PutCount =
        { }

    private val init = Counter.init(getCount)
    private val update = Counter.update(putCount)

    // Init

    @Test
    fun `initial Model count should be 0`() {
        val expected = Model(count = 0)

        val (actual, _) = init()

        assertEquals(expected, actual)
    }

    @Test
    fun `initial Effect should dispatch SetCount`() {
        val expected = Msg.SetCount(getCount())

        val (_, effect) = init()

        runBlocking {
            effect { actual ->
                assertEquals(expected, actual)
            }
        }
    }

    // Update

    @Test
    fun `SetCount msg should set Model count`() {
        val msg = Msg.SetCount(count = 42)
        val model = Model(count = 0)
        val expected = Model(count = msg.count)

        val (actual, _) = update(msg, model)

        assertEquals(expected, actual)
    }

    @Test
    fun `Increment msg should increment Model count`() {
        val msg = Msg.Increment
        val model = Model(count = 0)
        val expected = Model(count = 1)

        val (actual, _) = update(msg, model)

        assertEquals(expected, actual)
    }

    @Test
    fun `Decrement msg should increment Model count`() {
        val msg = Msg.Decrement
        val model = Model(count = 0)
        val expected = Model(count = -1)

        val (actual, _) = update(msg, model)

        assertEquals(expected, actual)

    }

    @Test
    fun `update effect should put count`() {
        val msg = Msg.SetCount(count = 42)
        val model = Model(count = 0)

        val putCount: PutCount = { count ->
            assertEquals(count, msg.count)
        }

        val (_, effect) = Counter.update(putCount)(msg, model)

        runBlocking {
            effect { }
        }
    }

    // View

    @Test
    fun `Props count should be equal to Model count`() {
        val model = Model(count = 0)
        val expected = model.count

        val props = view(model)
        val actual = props.count

        assertEquals(expected, actual)
    }

    @Test
    fun `Props increment should dispatch Increment msg`() {
        val model = Model(count = 0)
        val props = view(model)

        props.increment { msg ->
            assertEquals(msg, Msg.Increment)
        }
    }

    @Test
    fun `Props decrement should dispatch Decrement msg`() = runBlocking {
        val model = Model(count = 0)
        val props = view(model)

        props.decrement { msg ->
            assertEquals(msg, Msg.Decrement)
        }
    }

}

