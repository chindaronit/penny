package com.penny.ui.events

import com.penny.data.model.AnalysisItem

sealed class AnalysisScreenEvents {
    data class GetAnalysisItems(val uid: String): AnalysisScreenEvents()
    data class AddOrUpdateAnalysisItem(val uid: String,val analysisItem: AnalysisItem): AnalysisScreenEvents()
}