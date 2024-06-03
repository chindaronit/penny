package com.penny.ui.data

import com.penny.R
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.penny.ui.theme.BlueViolet1
import com.penny.ui.theme.BlueViolet2
import com.penny.ui.theme.BlueViolet3
import com.penny.ui.theme.LightGreen1
import com.penny.ui.theme.LightGreen2
import com.penny.ui.theme.LightGreen3
import com.penny.ui.theme.OrangeYellow1
import com.penny.ui.theme.OrangeYellow2
import com.penny.ui.theme.OrangeYellow3
import com.penny.ui.theme.Red1
import com.penny.ui.theme.Red2
import com.penny.ui.theme.Red3

data class FeaturedItem(
    val title: String,
    @DrawableRes val iconId: Int,
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color,
    var amount: String,
)


val featuredList = listOf(
    FeaturedItem(
        title = "Income",
        R.drawable.down,
        LightGreen1,
        LightGreen2,
        LightGreen3,
        "-"
    ),
    FeaturedItem(
        title = "Expense",
        R.drawable.arrow,
        Red1,
        Red2,
        Red3,
        "-"
    ),
    FeaturedItem(
        title = "Balance",
        R.drawable.wallet,
        BlueViolet1,
        BlueViolet2,
        BlueViolet3,
        "-"
    ),
    FeaturedItem(
        title = "Debt",
        R.drawable.creditor,
        OrangeYellow3,
        OrangeYellow2,
        OrangeYellow1,
        "-"
    )
)
