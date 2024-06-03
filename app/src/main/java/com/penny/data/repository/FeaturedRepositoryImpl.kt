package com.penny.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.penny.data.model.Accounts
import com.penny.data.model.Featured
import com.penny.di.IODispatcher
import com.penny.env.Collection_Accounts
import com.penny.env.Collection_Featured
import com.penny.env.Internet_Timeout
import com.penny.env.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class FeaturedRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : FeaturedRepository {
    override suspend fun getFeatured(uid: String): Status<Featured> {
        return try {
            withContext(ioDispatcher) {
                val fetchFeaturedTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Featured)
                        .whereEqualTo("uid", uid)
                        .get()
                        .await()
                }

                if (fetchFeaturedTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                // Handle the query snapshot correctly
                val document = fetchFeaturedTimeout.documents.firstOrNull()
                if (document != null) {
                    val item = Featured(
                        id = document.id,
                        uid = uid,
                        income = document.getString("income") ?: "0",
                        cash = document.getString("cash") ?: "0",
                        expenses = document.getString("expenses") ?: "0",
                        debt = document.getString("debt") ?: "0",
                    )
                    return@withContext Status.Success(item)
                }


                // If no document exists, create a new one
                val newFeatured = Featured(uid = uid)

                // Attempt to add the new document to the collection
                val createFeaturedTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Featured)
                        .add(newFeatured)
                        .await()
                }

                // Check if the creation operation timed out
                if (createFeaturedTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException("Internet timeout occurred"))
                }

                // If successful, return the created Featured item
                Status.Success(newFeatured)
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }


    override suspend fun updateFeatured(uid: String, featured: Featured): Status<Unit> {
        return try {
            withContext(ioDispatcher) {

                val querySnapshot = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Featured)
                        .whereEqualTo("uid", uid)
                        .get()
                        .await()
                }

                if (querySnapshot == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                val document = querySnapshot.documents.first()
                val documentId = document.id


                val updatedFeatured = mutableMapOf<String, Any>()
                updatedFeatured["income"] = featured.income
                updatedFeatured["expenses"] = featured.expenses
                updatedFeatured["cash"] = featured.cash
                updatedFeatured["debt"] = featured.debt

                val updateFeaturedTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Featured)
                        .document(documentId)
                        .update(updatedFeatured)
                }

                if (updateFeaturedTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(Unit)
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }

}