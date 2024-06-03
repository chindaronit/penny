package com.penny.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun FixedHeightPreLoader(
    height: Dp
){
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}