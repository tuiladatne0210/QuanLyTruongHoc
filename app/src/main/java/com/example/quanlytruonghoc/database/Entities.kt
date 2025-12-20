package com.example.quanlytruonghoc.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String
)

@Entity(tableName = "lop_hoc")
data class LopHoc(
    @PrimaryKey val maLop: String,
    val tenLop: String
) {
    override fun toString(): String = tenLop
}

@Entity(tableName = "hoc_sinh")
data class HocSinh(
    @PrimaryKey val maHS: String,
    val tenHS: String,
    val maLop: String, // Khóa ngoại logic
    val ngaySinh: String
) {
    override fun toString(): String = "$maHS - $tenHS"
}

@Entity(tableName = "bang_diem")
data class BangDiem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val maHS: String,
    val monHoc: String,
    val hocKy: String,
    val loaiKT: String,
    val diemSo: Double
)

// Entity mới cho Thành viên 4
@Entity(tableName = "hanh_kiem")
data class HanhKiem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val maHS: String,
    val noiDung: String // Khen thưởng hoặc vi phạm
)