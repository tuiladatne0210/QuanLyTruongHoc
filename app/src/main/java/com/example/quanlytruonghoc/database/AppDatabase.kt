package com.example.quanlytruonghoc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

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
                ).addCallback(DatabaseCallback()).allowMainThreadQueries().build() // Cho phép chạy trên Main Thread để đơn giản hóa
                INSTANCE = instance
                instance
            }
        }
    }
    private class DatabaseCallback : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //Chay background thread
            Thread{
                INSTANCE?.appDao()?.registerUser(
                    User("admin", "123","admin")
                )
            }.start()
        }
    }
}