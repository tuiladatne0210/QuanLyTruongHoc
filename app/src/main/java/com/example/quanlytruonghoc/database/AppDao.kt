package com.example.quanlytruonghoc.database

import androidx.room.*

@Dao
interface AppDao {
    // --- USER (TV1) ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun registerUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :user AND password = :pass")
    fun loginUser(user: String, pass: String): User?

    // --- LỚP HỌC (TV3) ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun themLop(lop: LopHoc): Long

    @Query("SELECT * FROM lop_hoc")
    fun getAllLop(): List<LopHoc>
    @Update
    fun suaLop(lop: LopHoc)
    @Delete
    fun xoaLop(lop: LopHoc)

    // --- HỌC SINH (TV1 + TV3) ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun themHocSinh(hs: HocSinh): Long

    @Update
    fun suaHocSinh(hs: HocSinh)

    @Delete
    fun xoaHocSinh(hs: HocSinh)

    @Query("SELECT * FROM hoc_sinh WHERE maLop = :maLop")
    fun getHocSinhByLop(maLop: String): List<HocSinh>

    @Query("SELECT * FROM hoc_sinh")
    fun getAllHocSinh(): List<HocSinh>

    // --- ĐIỂM (TV2) ---
    @Insert
    fun themDiem(diem: BangDiem)

    @Query("SELECT * FROM bang_diem WHERE maHS = :maHS")
    fun getDiemByHocSinh(maHS: String): List<BangDiem>

    @Query("DELETE FROM bang_diem WHERE maHS = :maHS")
    fun xoaDiemCuaHocSinh(maHS: String)

    // --- HẠNH KIỂM (TV4) ---
    @Insert
    fun themHanhKiem(hk: HanhKiem)

    @Query("SELECT * FROM hanh_kiem WHERE maHS = :maHS")
    fun getHanhKiemByHocSinh(maHS: String): List<HanhKiem>

    @Query("SELECT * FROM hanh_kiem")
    fun getAllHanhKiem(): List<HanhKiem>

    @Delete
    fun xoaHanhKiem(hk: HanhKiem)
    // Trong file database/AppDao.kt, thêm hàm này vào interface AppDao
    @Query("SELECT * FROM bang_diem WHERE maHS = :maHS AND loaiKT = :loaiKT")
    fun getDiemByFilter(maHS: String, loaiKT: String): List<BangDiem>
    @Query("SELECT * FROM hoc_sinh WHERE maHS = :maHS")
    fun getHocSinhById(maHS: String): HocSinh?
}