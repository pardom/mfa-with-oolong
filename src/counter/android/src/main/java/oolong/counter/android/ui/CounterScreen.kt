package oolong.counter.android.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import oolong.Dispatch
import oolong.counter.android.R
import oolong.counter.app.Counter.Model
import oolong.counter.app.Counter.Msg
import oolong.counter.app.Counter.Props
import oolong.counter.app.Counter.view

@Composable
fun CounterScreen(props: Props, dispatch: Dispatch<Msg>) {
    Scaffold(
        topBar = { TopAppBar({ Text(stringResource(R.string.app_name)) }) },
        bodyContent = { Counter(props, dispatch) }
    )
}

@Composable
fun Counter(
    props: Props,
    dispatch: Dispatch<Msg>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DecrementButton(props, dispatch)
        CountLabel(
            props.count,
            modifier = Modifier.weight(1F)
        )
        IncrementButton(props, dispatch)
    }
}

@Composable
fun CountLabel(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        "$count",
        style = MaterialTheme.typography.h2,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
fun IncrementButton(
    props: Props,
    dispatch: Dispatch<Msg>,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { props.increment(dispatch) },
        modifier = modifier.size(72.dp),
    ) {
        Icon(vectorResource(R.drawable.ic_baseline_arrow_drop_up_48))
    }
}

@Composable
fun DecrementButton(
    props: Props,
    dispatch: Dispatch<Msg>,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = { props.decrement(dispatch) },
        modifier = modifier.size(72.dp),
    ) {
        Icon(vectorResource(R.drawable.ic_baseline_arrow_drop_down_48))
    }
}

@Preview
@Composable
fun CounterScreen() {
    val model = Model(count = 0)
    val props = view(model)
    CounterScreen(
        props = props,
        dispatch = {},
    )
}