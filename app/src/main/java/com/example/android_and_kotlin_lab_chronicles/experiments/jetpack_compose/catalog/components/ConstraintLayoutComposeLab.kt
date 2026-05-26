package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con ConstraintLayout en Compose para layouts complejos y optimizados.
 */
@Composable
fun ConstraintLayoutComposeLab() {
    SamplesShowcase(
        { ConstraintSample() },
        { ConstraintGuidesSample() },
        { ConstraintBarrierSample() },
        { ConstraintChainSample() }
    )
}

@Composable
fun ConstraintSample() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (boxRed, boxBlue, boxYellow, boxMagenta, boxGreen) = createRefs()

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Green)
                .constrainAs(boxGreen) {
                    top.linkTo(boxRed.bottom)
                    end.linkTo(boxRed.start)
                })

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Blue)
                .constrainAs(boxBlue) {
                    top.linkTo(boxRed.bottom)
                    start.linkTo(boxRed.end)
                })

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Yellow)
                .constrainAs(boxYellow) {
                    bottom.linkTo(boxRed.top)
                    end.linkTo(boxRed.start)
                })

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Magenta)
                .constrainAs(boxMagenta) {
                    bottom.linkTo(boxRed.top)
                    start.linkTo(boxRed.end)
                })
    }
}

@Composable
fun ConstraintGuidesSample() {
    ConstraintLayout(Modifier.fillMaxSize()) {
//        val startGuide = createGuidelineFromTop(16.dp)

        val boxRed = createRef()
        val topGuide = createGuidelineFromTop(0.1f)
        val startGuide = createGuidelineFromStart(0.25f)

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    top.linkTo(topGuide)
                    start.linkTo(startGuide)
                })

    }
}

@Composable
fun ConstraintBarrierSample() {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (boxRed, boxGreen, boxYellow) = createRefs()
        val barrier = createEndBarrier(boxRed, boxGreen)

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Green)
                .constrainAs(boxGreen) {
                    start.linkTo(parent.start, margin = 16.dp)
                })

        Box(
            modifier = Modifier
                .size(235.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    top.linkTo(boxGreen.bottom)
                    start.linkTo(parent.start, margin = 32.dp)
                })

        Box(
            Modifier
                .size(50.dp)
                .background(Color.Yellow)
                .constrainAs(boxYellow) {
                    start.linkTo(barrier)
                })
    }
}

@Composable
fun ConstraintChainSample() {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (boxRed, boxGreen, boxYellow) = createRefs()

        Box(
            modifier = Modifier
                .size(75.dp)
                .background(Color.Green)
                .constrainAs(boxGreen) {
                    start.linkTo(parent.start)
                    end.linkTo(boxRed.start)
                })

        Box(
            modifier = Modifier
                .size(75.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    start.linkTo(boxGreen.end)
                    end.linkTo(boxYellow.start)
                })

        Box(
            Modifier
                .size(75.dp)
                .background(Color.Yellow)
                .constrainAs(boxYellow) {
                    start.linkTo(boxRed.end)
                    end.linkTo(parent.end)
                })

        createVerticalChain(boxRed, boxGreen, boxYellow, chainStyle = ChainStyle.SpreadInside)
    }
}
