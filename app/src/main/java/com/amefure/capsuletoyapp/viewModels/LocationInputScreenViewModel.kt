package com.amefure.capsuletoyapp.viewModels

import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationInputScreenViewModel @Inject constructor() : ViewModel() {
    /** UI連動プロパティ */
    public var locationName by mutableStateOf("")

    /** 選択された座標を保持 */
    public var selectedLatLng: LatLng? by mutableStateOf(null)
        private set

    /** 初期位置：スカイツリー：LatLng(35.710063, 139.8107) */
    public var initialLatLng: LatLng by mutableStateOf(LatLng(35.710063, 139.8107))
        private set
    public var showValidationDialog by mutableStateOf(false)
        private set

    @MainThread
    public fun showValidationAlert() {
        showValidationDialog = true
    }

    @MainThread
    public fun closeValidationAlert() {
        showValidationDialog = false
    }

    fun onMapClick(latLng: LatLng) {
        selectedLatLng = latLng
    }

    public fun createLocation(
        seriesId: Long,
    ): Location? {
        if (locationName.isEmpty()) {
            showValidationAlert()
            return null
        }

        val location = Location(
            name = locationName,
            seriesId = seriesId,
            latitude = selectedLatLng?.latitude,
            longitude = selectedLatLng?.longitude,
        )
        return location
    }
}
