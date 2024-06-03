package com.penny.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.penny.data.model.Transaction
import com.penny.di.IODispatcher
import com.penny.env.Collection_Transaction
import com.penny.env.Internet_Timeout
import com.penny.env.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): TransactionRepository {

    override suspend fun getTransaction(id: String): Status<Transaction> {
        return withContext(ioDispatcher) {
            try {
                val documentSnapshot = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Transaction)
                        .document(id)
                        .get()
                        .await()
                } ?: return@withContext Status.Failure(IllegalStateException(Internet_Timeout))

                if (documentSnapshot.exists()) {
                    val transaction = Transaction(
                        id = documentSnapshot.id,
                        uid = documentSnapshot.getString("uid") ?:"",
                        title = documentSnapshot.getString("title") ?:"",
                        categoryInd = documentSnapshot.getLong("categoryInd") ?: 0,
                        timestamp = documentSnapshot.getTimestamp("timestamp") ?: Timestamp.now(),
                        amount = documentSnapshot.getString("amount") ?:"",
                        paymentSourceInd = documentSnapshot.getLong("paymentSourceInd")?: 0,
                        description = documentSnapshot.getString("description") ?:""
                    )
                    Status.Success(transaction)
                } else {
                    Status.Failure(IllegalStateException("Transaction not found"))
                }
            } catch (e: Exception) {
                Status.Failure(e)
            }
        }
    }

    override suspend fun getAllTransactions(uid: String): Status<List<Transaction>> {
        return try {
            withContext(ioDispatcher) {
                val fetchTransactionsTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Transaction)
                        .whereEqualTo("uid", uid)
                        .get()
                        .await()
                        .documents.map { document ->
                            Transaction(
                                id = document.id,
                                uid = uid,
                                title = document.getString("title") ?:"",
                                categoryInd = document.getLong("categoryInd")?: 0,
                                timestamp = document.getTimestamp("timestamp") ?: Timestamp.now(),
                                amount = document.getString("amount") ?:"",
                                paymentSourceInd = document.getLong("paymentSourceInd")?: 0,
                                description = document.getString("description") ?:""
                            )
                        }
                }
                if (fetchTransactionsTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(fetchTransactionsTimeout.toList())
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }

    override suspend fun addTransaction(uid: String, transaction: Transaction): Status<Unit> {
        return try{
            withContext(ioDispatcher){
                val addTransactionTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Transaction)
                        .add(transaction)
                        .await()
                }
                if (addTransactionTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(Unit)
            }
        } catch (e: Exception){
            Status.Failure(e)
        }
    }

    override suspend fun deleteTransaction(id: String): Status<Unit> {
        return try {
            withContext(ioDispatcher) {
                val deleteTransactionTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Transaction)
                        .document(id)
                        .delete()
                }
                if (deleteTransactionTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(Unit)
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }

}