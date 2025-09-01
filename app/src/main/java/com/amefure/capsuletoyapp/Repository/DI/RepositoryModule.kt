package com.amefure.capsuletoyapp.Repository.DI

import com.amefure.capsuletoyapp.Repository.Impl.ImageFileRepositoryImpl
import com.amefure.capsuletoyapp.Repository.Impl.RoomSeriesRepositoryImpl
import com.amefure.capsuletoyapp.Repository.Interface.ImageRepository
import com.amefure.capsuletoyapp.Repository.Interface.SeriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt に対して Repository 関連の依存関係をどのように提供するかを定義するクラス
 *
 * このモジュールはアプリケーション全体（Singletonスコープ）で有効
 * Repository（インターフェース）を要求されたときに、RepositoryImpl（実装）を注入することを定義
 */
@Module // Hiltに依存性提供の定義をする場所であることを示す
@InstallIn(SingletonComponent::class) // アプリ全体で共有する依存性（シングルトン）であることを示す
object RepositoryModule {

    @Provides // 対象のインターフェースにどの実態を渡すかを定義する
    fun provideSeriesRepository(
        impl: RoomSeriesRepositoryImpl
    ): SeriesRepository = impl

    @Provides
    fun provideImageFileRepository(
        impl: ImageFileRepositoryImpl
    ): ImageRepository = impl
}