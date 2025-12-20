package com.example.quanlytruonghoc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, LopHoc::class, HocSinh::class, BangDiem::class, HanhKiem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quanlytruonghoc_db"
                ).allowMainThreadQueries().build() // Cho phép chạy trên Main Thread để đơn giản hóa
                INSTANCE = instance
                instance
            }
        }
    }
}