package com.penny.ui.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.penny.data.model.AnalysisItem
import com.penny.ui.data.categoryList
import com.penny.ui.mapping.monthMapper
import java.time.Year

@Composable
fun PieChart(
    month: Int,
    year: Int,
    data: List<AnalysisItem>,
    radiusOuter: Dp = 100.dp,
    chartBarWidth: Dp = 20.dp,
) {

    val amounts = data.mapNotNull { it.amount.toFloatOrNull() }
    val totalSum = amounts.sum()
    val floatValue = mutableListOf<Float>()

    // To set the value of each Arc according to
    // the value given in the data, we have used a simple formula.
    // For a detailed explanation check out the Medium Article.
    // The link is in the about section and readme file of this GitHub Repository
    // Calculate the proportional angles for each slice
    data.mapNotNull { it.amount.toFloatOrNull() }
        .forEachIndexed { index, value ->
            floatValue.add(index, 360 * value / totalSum)
        }

    // add the colors as per the number of data(no. of pie chart entries)
    // so that each data will get a color
    val colors = data.map { categoryList[it.categoryId.toInt()].color }

    var lastValue = 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier.size(radiusOuter*2.5f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2.5f)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }

                // Calculate center coordinates
                val centerX = size.width / 2
                val centerY = size.height / 2

                // Write text in the center
                val text = "${monthMapper[month]} ${year}"
                drawIntoCanvas {
                    val textPaint = Paint().apply {
                        color = android.graphics.Color.WHITE // Set the text color
                        textSize = 24.sp.toPx()
                    }

                    val textWidth = textPaint.measureText(text)
                    val textHeight = textPaint.descent() - textPaint.ascent()

                    val x = centerX - textWidth / 2
                    val y = centerY + textHeight / 2 // Adjusted to center vertically

                    it.nativeCanvas.drawText(
                        text,
                        x,
                        y,
                        textPaint
                    )
                }

            }

        }

    }

}
