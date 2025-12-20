package com.example.quanlytruonghoc

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlytruonghoc.adapter.MonHocInputAdapter
import com.example.quanlytruonghoc.database.AppDatabase
import com.example.quanlytruonghoc.database.BangDiem
import com.example.quanlytruonghoc.database.HocSinh
import com.example.quanlytruonghoc.database.LopHoc
import com.example.quanlytruonghoc.model.MonHocInput

class QuanLyDiemActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    // Views
    private lateinit var spnLop: Spinner
    private lateinit var spnHocSinh: Spinner
    private lateinit var spnLoaiDiem: Spinner
    private lateinit var rvMonHoc: RecyclerView
    private lateinit var btnLuu: Button

    private var listLop = ArrayList<LopHoc>()
    private var listHocSinh = ArrayList<HocSinh>()
    private var selectedMaHS = ""

    private val listMonInput = arrayListOf(
        MonHocInput("Toán"), MonHocInput("Lý"), MonHocInput("Hóa"),
        MonHocInput("Văn"), MonHocInput("Anh"), MonHocInput("Sử"),
        MonHocInput("Địa"), MonHocInput("GDCD"), MonHocInput("Tin Học")
    )
    private lateinit var adapterMonHoc: MonHocInputAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_diem)

        try {
            db = AppDatabase.getDatabase(this)

            // Ánh xạ
            spnLop = findViewById(R.id.spnChonLop)
            spnHocSinh = findViewById(R.id.spnChonHS)
            spnLoaiDiem = findViewById(R.id.spnLoaiDiem)
            rvMonHoc = findViewById(R.id.rvMonHocInput)
            btnLuu = findViewById(R.id.btnLuuTatCa)

            // Setup RecyclerView
            adapterMonHoc = MonHocInputAdapter(listMonInput)
            rvMonHoc.layoutManager = LinearLayoutManager(this)
            rvMonHoc.adapter = adapterMonHoc

            // Setup Spinner Loại
            val loaiDiems = listOf("15 Phút", "1 Tiết", "Giữa Kỳ", "Cuối Kỳ")
            spnLoaiDiem.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, loaiDiems)

            // Load Data Logic
            loadDataLogic()

            btnLuu.setOnClickListener {
                luuDiem()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khởi tạo: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDataLogic() {
        try {
            listLop = ArrayList(db.appDao().getAllLop())
            val adapterLop = ArrayAdapter(this, android.R.layout.simple_spinner_item, listLop)
            adapterLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnLop.adapter = adapterLop

            spnLop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (listLop.isNotEmpty()) {
                        loadHocSinhByLop(listLop[position].maLop)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            spnHocSinh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (listHocSinh.isNotEmpty()) {
                        selectedMaHS = listHocSinh[position].maHS
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { selectedMaHS = "" }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi tải dữ liệu: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadHocSinhByLop(maLop: String) {
        try {
            listHocSinh = ArrayList(db.appDao().getHocSinhByLop(maLop))
            val adapterHS = ArrayAdapter(this, android.R.layout.simple_spinner_item, listHocSinh)
            adapterHS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnHocSinh.adapter = adapterHS

            if (listHocSinh.isNotEmpty()) {
                selectedMaHS = listHocSinh[0].maHS
            } else {
                selectedMaHS = ""
                Toast.makeText(this, "Lớp này chưa có học sinh", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi tải học sinh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun luuDiem() {
        try {
            if (selectedMaHS.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn học sinh!", Toast.LENGTH_SHORT).show()
                return
            }

            val loaiKT = spnLoaiDiem.selectedItem.toString()
            var countSuccess = 0

            for (mon in listMonInput) {
                val diemStr = mon.diem.trim()
                if (diemStr.isNotEmpty()) {
                    try {
                        val diemSo = diemStr.toDouble()
                        if (diemSo in 0.0..10.0) {
                            val objDiem = BangDiem(
                                maHS = selectedMaHS,
                                monHoc = mon.tenMon,
                                hocKy = "HK1",
                                loaiKT = loaiKT,
                                diemSo = diemSo
                            )
                            db.appDao().themDiem(objDiem)
                            countSuccess++
                            mon.diem = ""
                        } else {
                            Toast.makeText(this, "Môn ${mon.tenMon}: Điểm phải từ 0-10", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(this, "Môn ${mon.tenMon}: Điểm không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (countSuccess > 0) {
                Toast.makeText(this, "Đã lưu $countSuccess điểm!", Toast.LENGTH_SHORT).show()
                adapterMonHoc.notifyDataSetChanged()
                rvMonHoc.scrollToPosition(0)
            } else {
                Toast.makeText(this, "Chưa có điểm hợp lệ nào được nhập", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi lưu điểm: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}