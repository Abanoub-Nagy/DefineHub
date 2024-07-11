package com.example.definehub.di

import com.example.definehub.data.repository.DictionaryRepoImpl
import com.example.definehub.domain.repo.DictionaryRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindDictionaryRepo(dictionaryRepoImpl: DictionaryRepoImpl): DictionaryRepo

}