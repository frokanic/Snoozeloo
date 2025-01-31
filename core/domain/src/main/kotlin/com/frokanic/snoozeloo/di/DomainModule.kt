package com.frokanic.snoozeloo.di

import com.frokanic.snoozeloo.usecase.CountDownTimerUseCase
import com.frokanic.snoozeloo.usecase.CountDownTimerUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideCountDownTimerUseCase(): CountDownTimerUseCase =
        CountDownTimerUseCaseImpl()

}