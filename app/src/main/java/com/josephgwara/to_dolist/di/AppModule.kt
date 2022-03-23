package com.josephgwara.to_dolist.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.josephgwara.to_dolist.data.TodoDatabase
import com.josephgwara.to_dolist.data.TodoRepository
import com.josephgwara.to_dolist.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesTodoDatabase(app: Application):TodoDatabase{
return Room.databaseBuilder(app, TodoDatabase::class.java, "todo_db").build()

    }
    @Provides
    @Singleton
    fun TodoRepository(db:TodoDatabase):TodoRepository{
        return TodoRepositoryImpl(db.dao)
    }

}