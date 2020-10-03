package oolong.counter.app

import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import oolong.counter.app.Counter.Model
import oolong.counter.app.Counter.Msg
import oolong.counter.app.Counter.init
import oolong.counter.app.Counter.update
import oolong.counter.app.Counter.view
import org.junit.jupiter.api.Test

class CounterTest {

    // Init

    @Test
    fun `initial Model count should be 0`() {
        val expected = Model(count = 0)

        val (actual, _) = init()

        assertEquals(expected, actual)
    }

    // Update

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

