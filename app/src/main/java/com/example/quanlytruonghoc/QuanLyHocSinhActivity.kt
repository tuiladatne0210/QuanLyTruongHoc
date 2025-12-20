package com.example.quanlytruonghoc

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quanlytruonghoc.adapter.HocSinhAdapter
import com.example.quanlytruonghoc.database.AppDatabase
import com.example.quanlytruonghoc.database.HocSinh
import com.example.quanlytruonghoc.database.LopHoc

class QuanLyHocSinhActivity : AppCompatActivity() {
    lateinit var db: AppDatabase
    lateinit var spnLop: Spinner
    lateinit var edtMa: EditText
    lateinit var edtTen: EditText
    lateinit var edtNgaySinh: EditText
    lateinit var lvHS: ListView
    var listLop = ArrayList<LopHoc>()
    var selectedMaLop: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_hoc_sinh)

        try {
            // Ánh xạ View
            db = AppDatabase.getDatabase(this)
            spnLop = findViewById(R.id.spnLopHoc)
            edtMa = findViewById(R.id.edtMaHS)
            edtTen = findViewById(R.id.edtTenHS)
            edtNgaySinh = findViewById(R.id.edtNgaySinh)
            lvHS = findViewById(R.id.lvHocSinh)

            // Load Spinner
            loadSpinnerLop()

            // Sự kiện Thêm
            findViewById<Button>(R.id.btnThemHS).setOnClickListener {
                try {
                    val ma = edtMa.text.toString().trim()
                    val ten = edtTen.text.toString().trim()
                    val ns = edtNgaySinh.text.toString().trim()

                    if (ma.isEmpty() || ten.isEmpty() || selectedMaLop.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đủ thông tin và chọn lớp", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Kiểm tra trùng mã (Optional nếu dùng try-catch)
                    val existingHS = db.appDao().getHocSinhById(ma)
                    if (existingHS != null) {
                        Toast.makeText(this, "Mã học sinh $ma đã tồn tại!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val result = db.appDao().themHocSinh(HocSinh(ma, ten, selectedMaLop, ns))
                    if (result > 0) {
                        loadHS()
                        clearInput()
                        Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Bắt lỗi SQLiteConstraintException (Trùng khóa chính)
                    Toast.makeText(this, "Lỗi thêm: Mã có thể bị trùng hoặc lỗi hệ thống", Toast.LENGTH_SHORT).show()
                }
            }

            // Sự kiện Xóa
            findViewById<Button>(R.id.btnXoaHS).setOnClickListener {
                try {
                    val ma = edtMa.text.toString().trim()
                    if (ma.isNotEmpty()) {
                        // Cần lấy đủ thông tin để xóa (hoặc viết hàm xóa chỉ cần ID trong DAO)
                        // Ở đây ta giả định lấy được object
                        val hsCanXoa = db.appDao().getHocSinhById(ma)
                        if(hsCanXoa != null) {
                            db.appDao().xoaHocSinh(hsCanXoa)
                            loadHS()
                            clearInput()
                            Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Không tìm thấy học sinh mã $ma", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Vui lòng nhập/chọn mã HS để xóa", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Lỗi khi xóa: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            // Sự kiện Click Item để fill dữ liệu lên EditText
            lvHS.setOnItemClickListener { _, _, position, _ ->
                try {
                    val adapter = lvHS.adapter as HocSinhAdapter
                    val hs = adapter.getItem(position) as HocSinh
                    edtMa.setText(hs.maHS)
                    edtTen.setText(hs.tenHS)
                    edtNgaySinh.setText(hs.ngaySinh)
                    edtMa.isEnabled = false // Không cho sửa mã
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Nút làm mới (để nhập mới sau khi click xem)
            // Bạn có thể thêm nút này vào layout nếu muốn, hoặc dùng nút Sửa
            // Ở đây tôi thêm logic cho nút Sửa
            findViewById<Button>(R.id.btnSuaHS).setOnClickListener {
                try {
                    val ma = edtMa.text.toString().trim()
                    val ten = edtTen.text.toString().trim()
                    val ns = edtNgaySinh.text.toString().trim()

                    if (ma.isNotEmpty()) {
                        val hsMoi = HocSinh(ma, ten, selectedMaLop, ns)
                        db.appDao().suaHocSinh(hsMoi)
                        loadHS()
                        clearInput()
                        Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Lỗi cập nhật: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi màn hình: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadSpinnerLop() {
        try {
            listLop = ArrayList(db.appDao().getAllLop())
            val adapterLop = ArrayAdapter(this, android.R.layout.simple_spinner_item, listLop)
            adapterLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnLop.adapter = adapterLop

            spnLop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (listLop.isNotEmpty()) {
                        selectedMaLop = listLop[p2].maLop
                        loadHS()
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi tải danh sách lớp", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadHS() {
        try {
            val listHS = db.appDao().getHocSinhByLop(selectedMaLop)
            val adapter = HocSinhAdapter(this, listHS)
            lvHS.adapter = adapter
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi tải danh sách học sinh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInput() {
        edtMa.setText("")
        edtTen.setText("")
        edtNgaySinh.setText("")
        edtMa.isEnabled = true
        edtMa.requestFocus()
    }
}