package com.penny.ui.state

import com.penny.data.model.AnalysisItem

data class AnalysisState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val analysisItems: List<AnalysisItem> = emptyList()
)