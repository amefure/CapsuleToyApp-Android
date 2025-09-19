package com.amefure.capsuletoyapp.viewModels.interfaces

import android.graphics.Bitmap
import android.net.Uri

/** ViewModelの中でカメラ機能を実装するためのInterface */
interface CameraInterface {
    /** ドロップダウンメニューの開閉フラグ */
    var expanded: Boolean

    /** カメラで撮影した画像を保持 */
    var thumbnail: Bitmap?

    /** 一時保存用のURI */
    val photoUri: Uri?

    /** 一時保存用のURIを構築 */
    fun preparePhotoUri()

    /** カメラで撮影した画像を格納 */
    fun onCameraCaptured()

    /** ギャラリーから選択された画像を格納 */
    fun onGalleryImageSelected(uri: Uri)
}
