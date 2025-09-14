package com.amefure.capsuletoyapp.viewModels

import android.content.Context
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amefure.capsuletoyapp.services.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RootEnvironment @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    public var isShowPermissionAlertDialog by mutableStateOf(false)
        private set

    @MainThread
    public fun showPermissionAlertDialog() {
        isShowPermissionAlertDialog = true
    }

    @MainThread
    public fun closePermissionAlertDialog() {
        isShowPermissionAlertDialog = false
    }

    /** 位置情報関係のパーミッション申請が許可済みかどうか */
    fun isGrantedLocationPermission(): Boolean {
        val service = LocationService(context)
        return service.isGrantedLocationPermission()
    }
}
