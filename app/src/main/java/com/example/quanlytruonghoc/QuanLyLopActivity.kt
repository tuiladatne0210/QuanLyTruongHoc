package com.example.quanlytruonghoc

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quanlytruonghoc.database.AppDatabase
import com.example.quanlytruonghoc.database.LopHoc

class QuanLyLopActivity : AppCompatActivity() {
    lateinit var edtTenLop: EditText
    lateinit var lvLop: ListView
    lateinit var db: AppDatabase
    var listLop = ArrayList<LopHoc>()
    lateinit var adapter: ArrayAdapter<LopHoc>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_lop)

        try {
            edtTenLop = findViewById(R.id.edtTenLop)
            lvLop = findViewById(R.id.lvLop)
            val btnThem = findViewById<Button>(R.id.btnThemLop)

            db = AppDatabase.getDatabase(this)
            loadData()

            btnThem.setOnClickListener {
                val ten = edtTenLop.text.toString().trim()
                if (ten.isNotEmpty()) {
                    try {
                        val maLop = "L${System.currentTimeMillis() % 10000}" // Mã ngẫu nhiên
                        val lopMoi = LopHoc(maLop, ten)

                        val result = db.appDao().themLop(lopMoi)
                        if (result > 0) {
                            loadData()
                            edtTenLop.setText("")
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

            lvLop.setOnItemLongClickListener { _, _, position, _ ->
                try {
                    val lop = listLop[position]
                    db.appDao().xoaLop(lop)
                    loadData()
                    Toast.makeText(this, "Đã xóa lớp ${lop.tenLop}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Lỗi xóa lớp: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                true
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khởi tạo màn hình: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadData() {
        try {
            listLop.clear()
            listLop.addAll(db.appDao().getAllLop())
            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged()
            } else {
                adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listLop)
                lvLop.adapter = adapter
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi tải danh sách lớp!", Toast.LENGTH_SHORT).show()
        }
    }
}