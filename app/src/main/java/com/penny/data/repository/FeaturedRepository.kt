package com.penny.data.repository

import com.penny.data.model.Featured
import com.penny.env.Status

interface FeaturedRepository {
    suspend fun getFeatured(uid: String): Status<Featured>
    suspend fun updateFeatured(uid: String, featured: Featured): Status<Unit>
}