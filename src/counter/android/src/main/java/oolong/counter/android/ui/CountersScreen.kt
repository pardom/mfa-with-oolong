package oolong.counter.android.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onCommit
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import oolong.Dispatch
import oolong.counter.android.R
import oolong.counter.app.Counters.Model
import oolong.counter.app.Counters.Msg
import oolong.counter.app.Counters.Props
import oolong.counter.app.Counters.view
import oolong.counter.app.Counter.Model as CounterModel

@Composable
fun CountersScreen(props: Props, dispatch: Dispatch<Msg>) {
    Scaffold(
        topBar = { TopAppBar({ Text(stringResource(R.string.app_name)) }) },
        bodyContent = { Counters(props, dispatch) },
        floatingActionButton = { AddCounterButton(props, dispatch) }
    )
}

@Composable
fun Counters(
    props: Props,
    dispatch: Dispatch<Msg>,
    modifier: Modifier = Modifier,
) {
    LazyColumnForIndexed(
        items = props.counters.toList(),
        modifier = modifier,
    ) { index, counter ->
        CounterItem(counter, dispatch)
        Divider()

        if (index == props.counters.lastIndex) {
            Spacer(Modifier.size(96.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CounterItem(
    props: Props.CounterProps,
    dispatch: Dispatch<Msg>,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberDismissState()

    onCommit(dismissState.value) {
        if (dismissState.value != DismissValue.Default) {
            props.removeCounter(dispatch)
            dismissState.reset()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        background = {
            Spacer(Modifier.fillMaxSize().background(Color.Gray))
        },
    ) {
        Counter(
            props.props,
            props.dispatch(dispatch),
            modifier = modifier.background(MaterialTheme.colors.surface),
        )
    }
}

@Composable
fun AddCounterButton(
    props: Props,
    dispatch: Dispatch<Msg>,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = { props.addCounter(dispatch) },
        modifier = modifier,
    ) {
        Icon(vectorResource(R.drawable.ic_baseline_add_24))
    }
}

@Preview
@Composable
fun CountersScreen() {
    val model = Model(
        counters = mapOf(
            0 to CounterModel(0),
            1 to CounterModel(1),
            2 to CounterModel(2),
        )
    )
    val props = view(model)
    CountersScreen(
        props = props,
        dispatch = {},
    )
}
