package com.amefure.capsuletoyapp.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

/**
 * [android.location.Location]の管理クラス
 * 端末の位置情報取得
 */
class LocationService(
    private val context: Context,
) {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    /** 位置情報関係のパーミッション申請が許可済みかどうか */
    fun isGrantedLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        return fine && coarse
    }

    /** ユーザーの位置情報取得 */
    @SuppressLint("MissingPermission")
    suspend fun fetchLastLocation(): Location? {
        // パーミッションチェック
        if (!isGrantedLocationPermission()) { return null }

        return try {
            val location = fusedLocationClient.lastLocation.await()
            Log.e("Location", "位置情報取得成功：" + location)
            return location
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Location", "位置情報取得失敗：" + e)
            null
        }
    }
}
