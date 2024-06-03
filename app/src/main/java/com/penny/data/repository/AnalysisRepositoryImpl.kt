package com.penny.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.penny.data.model.AnalysisItem
import com.penny.di.IODispatcher
import com.penny.env.Collection_Analysis
import com.penny.env.Internet_Timeout
import com.penny.env.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class AnalysisRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): AnalysisRepository{
    override suspend fun getAnalysisItems(uid: String): Status<List<AnalysisItem>> {
        return try {
            withContext(ioDispatcher) {
                val fetchAnalysisItemTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Analysis)
                        .whereEqualTo("uid", uid)
                        .get()
                        .await()
                        .documents.map { document ->
                            AnalysisItem(
                                id = document.id,
                                uid = uid,
                                month = document.getLong("month") ?: 1,
                                year = document.getLong("year") ?: 0,
                                amount = document.getString("amount") ?: "",
                                categoryId = document.getLong("categoryId") ?:0
                            )
                        }
                }
                if (fetchAnalysisItemTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(fetchAnalysisItemTimeout.toList())
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }

    override suspend fun addOrUpdateAnalysisItem(
        uid: String,
        analysisItem: AnalysisItem
    ): Status<Unit> {
        return try {
            withContext(ioDispatcher) {
                val fetchAnalysisTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Analysis)
                        .whereEqualTo("uid", uid)
                        .whereEqualTo("month", analysisItem.month)
                        .whereEqualTo("year", analysisItem.year)
                        .whereEqualTo("categoryId", analysisItem.categoryId)
                        .get()
                        .await()
                }
                if (fetchAnalysisTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                val document = fetchAnalysisTimeout.documents.firstOrNull()

                if (document == null) {
                    val addItemTimeout = withTimeoutOrNull(10000L) {
                        db.collection(Collection_Analysis)
                            .add(analysisItem)
                            .await()
                    }
                    if (addItemTimeout == null) {
                        return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                    }
                } else {
                    val currentAmount = document.getString("amount")?.toFloatOrNull() ?: 0f
                    val updatedAmount = currentAmount + analysisItem.amount.toFloat()

                    val updatedAnalysisItem = mapOf(
                        "amount" to updatedAmount.toString()
                    )

                    val updateItemTimeout = withTimeoutOrNull(10000L) {
                        db.collection(Collection_Analysis)
                            .document(document.id)
                            .update(updatedAnalysisItem)
                    }

                    if (updateItemTimeout == null) {
                        return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                    }
                }

                Status.Success(Unit)
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }
}