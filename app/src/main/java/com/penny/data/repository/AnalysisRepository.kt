package com.penny.data.repository

import com.penny.data.model.AnalysisItem
import com.penny.env.Status

interface AnalysisRepository {
    suspend fun getAnalysisItems(uid: String): Status<List<AnalysisItem>>
    suspend fun addOrUpdateAnalysisItem(uid: String, analysisItem: AnalysisItem): Status<Unit>
}