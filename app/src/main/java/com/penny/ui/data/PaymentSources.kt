package com.penny.ui.data


data class PaymentSource(
    val ind: Int,
    val type: String,
    val source: String,
)

val PaymentSourcesList = listOf(
    PaymentSource(
        0,
        "Bank",
        "State Bank Of India",
    ),
    PaymentSource(
        1,
        "Bank",
        "HDFC Bank",
    ),
    PaymentSource(
        2,
        "Bank",
        "Punjab National Bank",
    ),
    PaymentSource(
        3,
        "Card",
        "SBI",
    ),
    PaymentSource(
        4,
        "Card",
        "ICICI",
    ),
    PaymentSource(
        5,
        "Digital Wallet",
        "Paytm"
    ),
    PaymentSource(
        6,
        "Digital Wallet",
        "Google Pay"
    )
)


