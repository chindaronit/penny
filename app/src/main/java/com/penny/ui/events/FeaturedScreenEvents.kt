package com.penny.ui.events

import com.penny.data.model.Featured

sealed class FeaturedScreenEvents {
    data class GetFeatured(val uid: String) : FeaturedScreenEvents()
    data class UpdateFeatured(val uid: String, val featured: Featured) : FeaturedScreenEvents()
    data class ReloadFeatured(val uid: String): FeaturedScreenEvents()
    data class WithoutReloadUpdateFeatured(val uid: String,val featured: Featured): FeaturedScreenEvents()
}