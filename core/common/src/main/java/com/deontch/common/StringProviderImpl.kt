package com.deontch.common

import android.content.Context
import androidx.annotation.PluralsRes
import com.deontch.base.providers.StringProvider
import javax.inject.Inject

/**
 * StringProviderImpl helps providing strings if a string is needed below the view level
 * such as in ViewModels.
 */
class StringProviderImpl @Inject constructor(private val context: Context) : StringProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }

    override fun getPlural(@PluralsRes resId: Int, quantity: Int): String {
        return context.resources.getQuantityString(resId, quantity)
    }
}
