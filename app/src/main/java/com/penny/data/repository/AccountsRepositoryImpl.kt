package com.penny.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.penny.data.model.Accounts
import com.penny.di.IODispatcher
import com.penny.env.Collection_Accounts
import com.penny.env.Internet_Timeout
import com.penny.env.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): AccountsRepository {
    override suspend fun getAllAccounts(uid: String): Status<List<Accounts>> {
        return try {
            withContext(ioDispatcher) {
                val fetchAccountsTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Accounts)
                        .whereEqualTo("uid", uid)
                        .get()
                        .await()
                        .documents.map { document ->
                            Accounts(
                                id = document.id,
                                uid = uid,
                                sourceInd = document.getLong("sourceInd") ?: 0,
                                balance = document.getString("balance") ?: "0"
                            )
                        }
                }
                if (fetchAccountsTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(fetchAccountsTimeout.toList())
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }

    override suspend fun addAccount(uid: String, account: Accounts): Status<Unit> {
        return try {
            withContext(ioDispatcher) {
                val existingAccountQuery = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Accounts)
                        .whereEqualTo("sourceInd", account.sourceInd)
                        .whereEqualTo("uid", uid) // Use the uid passed as a parameter
                        .get()
                        .await()
                }

                if (existingAccountQuery == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                if (!existingAccountQuery.isEmpty) {
                    return@withContext Status.Failure(Exception("Account Already Exists"))
                }

                val addAccountTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Accounts)
                        .add(account)
                        .await()
                }

                if (addAccountTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(Unit)
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }


    override suspend fun updateAccount(id: String, account: Accounts): Status<Unit> {
        return try{
            withContext(ioDispatcher){
                val updatedAccount=mutableMapOf<String,Any>()
                updatedAccount["balance"] = account.balance

                val updateAccountTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Accounts)
                        .document(id)
                        .update(updatedAccount)
                }
                if (updateAccountTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(Unit)
            }
        }catch (e: Exception){
            Status.Failure(e)
        }
    }

    override suspend fun deleteAccount(id: String): Status<Unit> {
        return try {
            withContext(ioDispatcher) {
                val deleteAccountTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Accounts)
                        .document(id)
                        .delete()
                }
                if (deleteAccountTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(Unit)
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }

    override suspend fun reduceAccountBalance(uid: String,sourceInd: Long, amount: Double): Status<Unit> {
        return try {
            withContext(ioDispatcher) {
                val getAccountTimeout = withTimeoutOrNull(10000L) {
                    db.collection(Collection_Accounts)
                        .whereEqualTo("sourceInd",sourceInd)
                        .whereEqualTo("uid",uid)
                        .get()
                        .await()
                }

                if (getAccountTimeout == null) {
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                val document = getAccountTimeout.documents.first()
                val currentBalance=document.getString("balance")?.toFloat() ?: 0f
                val updatedBalance = (currentBalance-amount).toString()

                val updateAccountTimeout= withTimeoutOrNull(10000L){
                    db.collection(Collection_Accounts)
                        .document(document.id)
                        .update("balance",updatedBalance)
                }

                if(updateAccountTimeout==null){
                    return@withContext Status.Failure(IllegalStateException(Internet_Timeout))
                }

                Status.Success(Unit)
            }
        } catch (e: Exception) {
            Status.Failure(e)
        }
    }
}