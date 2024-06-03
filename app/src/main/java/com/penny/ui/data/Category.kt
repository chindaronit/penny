package com.penny.ui.data

import androidx.compose.ui.graphics.Color
import com.penny.R
import com.penny.ui.theme.AquaBlue
import com.penny.ui.theme.BlueViolet1
import com.penny.ui.theme.ButtonBlue
import com.penny.ui.theme.LightGreen1
import com.penny.ui.theme.LightGreen3
import com.penny.ui.theme.LightRed
import com.penny.ui.theme.MixedGreen
import com.penny.ui.theme.OrangeYellow1
import com.penny.ui.theme.Pink40
import com.penny.ui.theme.Pink80
import com.penny.ui.theme.White

class Category (
    val categoryName: String,
    val icon: Int,
    val color: Color
)

val categoryList=listOf(
    Category(
        "Grocery",
        R.drawable.grocery,
        Color.Yellow
    ),
    Category(
        "Entertainment",
        R.drawable.entertainment,
        BlueViolet1
    ),
    Category(
        "Food",
        R.drawable.food,
        LightGreen3
    ),
    Category(
        "Education",
        R.drawable.education,
        White
    ),
    Category(
        "Health",
        R.drawable.health,
        Color.Red
    ),
    Category(
        "Savings",
        R.drawable.savingoutlined,
        OrangeYellow1
    ),
    Category(
        "Rent",
        R.drawable.rent,
        Pink40
    ),
    Category(
        "Bills",
        R.drawable.bill,
        Color.Cyan
    ),
    Category(
        "Shopping",
        R.drawable.shopping,
        ButtonBlue
    ),
    Category(
        "Travel",
        R.drawable.travel,
        AquaBlue
    ),
    Category(
        "Gifts",
        R.drawable.gifts,
        MixedGreen
    ),
    Category(
        "Investment",
        R.drawable.investment,
        LightRed
    ),
    Category(
        "Insurance",
        R.drawable.insurance,
        LightGreen1
    ),
    Category(
        "Other",
        R.drawable.other,
        Pink80
    )
)
