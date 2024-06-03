package com.penny.ui.state

import com.penny.data.model.Featured

data class FeaturedState (
    val isLoading: Boolean = false,
    val error: String? = null,
    val featured: Featured? =null
)