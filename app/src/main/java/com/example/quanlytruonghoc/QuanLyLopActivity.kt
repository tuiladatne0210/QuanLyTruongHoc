package com.example.quanlytruonghoc

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quanlytruonghoc.adapter.LopHocAdapter
import com.example.quanlytruonghoc.database.AppDatabase
import com.example.quanlytruonghoc.database.LopHoc

class QuanLyLopActivity : AppCompatActivity() {
    lateinit var edtTenLop: EditText
    lateinit var edtGiaoVien: EditText
    lateinit var btnThemLop: Button
    lateinit var btnSuaLop: Button
    lateinit var btnXoaLop: Button
    lateinit var lvLop: ListView
    lateinit var db: AppDatabase
    var lopDangChon: LopHoc? = null
    var listLop = ArrayList<LopHoc>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quan_ly_lop)
        setControl()
        setEvent()
        db = AppDatabase.getDatabase(this)
        loadData()
    }
    fun setControl(){
        edtTenLop = findViewById(R.id.edtTenLop)
        edtGiaoVien = findViewById(R.id.edtGiaoVien)
        lvLop = findViewById(R.id.lvLop)
        btnThemLop = findViewById(R.id.btnThemLop)
        btnXoaLop = findViewById(R.id.btnXoaLop)
        btnSuaLop = findViewById(R.id.btnSuaLop)
    }
    fun setEvent(){
        btnThemLop.setOnClickListener {
            val ten = edtTenLop.text.toString().trim()
            val gv = edtGiaoVien.text.toString().trim()
            if (ten.isNotEmpty()) {
                try {
                    val maLop = "L${System.currentTimeMillis() % 10000}" // Mã ngẫu nhiên
                    val lopMoi = LopHoc(maLop, ten, gv)

                    val result = db.appDao().themLop(lopMoi)
                    if (result > 0) {
                        loadData()
                        edtTenLop.setText("")
                        edtGiaoVien.setText("")
                        Toast.makeText(this, "Thêm lớp thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Thêm thất bại (Lớp đã tồn tại?)", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Lỗi thêm lớp: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập tên lớp", Toast.LENGTH_SHORT).show()
            }
        }
        btnSuaLop.setOnClickListener {
            if(lopDangChon == null){
                Toast.makeText(this,"Vui lòng chọn lớp cần sửa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val tenMoi = edtTenLop.text.toString().trim()
            val gvMoi = edtGiaoVien.text.toString().trim()
            if (tenMoi.isEmpty()) {
                Toast.makeText(this, "Tên lớp không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val lopSua = LopHoc(
                    lopDangChon!!.maLop, // giữ nguyên mã lớp
                    tenMoi,
                    gvMoi
                )
                db.appDao().suaLop(lopSua)
                loadData()
                edtTenLop.setText("")
                edtGiaoVien.setText("")
                lopDangChon = null

                Toast.makeText(this, "Sửa lớp thành công", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Lỗi sửa lớp: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        btnXoaLop.setOnClickListener {
            if(lopDangChon == null){
                Toast.makeText(this, "Vui lòng chọn lớp cần xóa",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                db.appDao().xoaLop(lopDangChon!!)
                loadData()
                edtTenLop.setText("")
                edtGiaoVien.setText("")
                lopDangChon = null

                Toast.makeText(this, "Xóa lớp thành công", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                Toast.makeText(this, "Lỗi xóa lớp: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        lvLop.setOnItemClickListener { _, _, position, _ ->
            lopDangChon = listLop[position]
            edtTenLop.setText(lopDangChon!!.tenLop)
            edtGiaoVien.setText(lopDangChon!!.giaoVien)
        }
    }

    private fun loadData() {
        try {
            listLop.clear()
            listLop.addAll(db.appDao().getAllLop())
            val adapter = LopHocAdapter(this, listLop)
            lvLop.adapter = adapter
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi tải danh sách lớp!", Toast.LENGTH_SHORT).show()
        }
    }
}