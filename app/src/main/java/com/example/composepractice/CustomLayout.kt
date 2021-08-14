package com.example.composepractice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composepractice.ui.theme.ComposePracticeTheme

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp,
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Composable
fun TextWithPaddingToBaselinePreview() {
    ComposePracticeTheme {
        Text(text = "Hola", Modifier.firstBaselineToTop(32.dp))
    }
}

@Composable
fun TextWithNormalPaddingPreview() {
    ComposePracticeTheme {
        Text(text = "Hola", Modifier.padding(top = 32.dp))
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Layout(modifier = modifier, content = content) { measurable, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeable = measurable.map {
            // Measure each child
            it.measure(constraints)
        }
        var yPos = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeable.forEach {
                it.placeRelative(x = 0, y = yPos)
                yPos += it.height
            }
        }
    }
}

@Composable
fun BodyContent1(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

