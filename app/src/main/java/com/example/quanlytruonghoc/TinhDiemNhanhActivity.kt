package com.example.quanlytruonghoc

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlytruonghoc.adapter.DiemHienThiAdapter
import com.example.quanlytruonghoc.database.AppDatabase
import com.example.quanlytruonghoc.database.BangDiem
import com.example.quanlytruonghoc.database.HocSinh
import com.example.quanlytruonghoc.database.LopHoc

class TinhDiemNhanhActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    // Views
    private lateinit var spnLop: Spinner
    private lateinit var spnHocSinh: Spinner
    private lateinit var spnLoaiKT: Spinner
    private lateinit var btnXem: Button
    private lateinit var rvDiem: RecyclerView
    private lateinit var txtKetQua: TextView
    private lateinit var txtXepLoai: TextView

    // Data lists
    private var listLop = ArrayList<LopHoc>()
    private var listHocSinh = ArrayList<HocSinh>()
    private var selectedMaHS: String = ""

    private lateinit var adapterDiem: DiemHienThiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tinh_diem_nhanh)

        db = AppDatabase.getDatabase(this)

        // 1. Ánh xạ View
        spnLop = findViewById(R.id.spnChonLop)
        spnHocSinh = findViewById(R.id.spnChonHS)
        spnLoaiKT = findViewById(R.id.spnLoaiKiemTra)
        btnXem = findViewById(R.id.btnXemDiem)
        rvDiem = findViewById(R.id.rvDiemHienThi)
        txtKetQua = findViewById(R.id.txtKetQua)
        txtXepLoai = findViewById(R.id.txtXepLoai)

        // 2. Setup RecyclerView
        rvDiem.layoutManager = LinearLayoutManager(this)
        adapterDiem = DiemHienThiAdapter(emptyList())
        rvDiem.adapter = adapterDiem

        // 3. Setup Spinner Loại Kiểm Tra
        val loaiDiems = listOf("15 Phút", "1 Tiết", "Giữa Kỳ", "Cuối Kỳ")
        spnLoaiKT.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, loaiDiems)

        // 4. Load dữ liệu
        loadDataSpinners()

        // 5. Sự kiện nút bấm
        btnXem.setOnClickListener {
            tinhVaHienThi()
        }
    }

    private fun loadDataSpinners() {
        // Load danh sách lớp
        listLop = ArrayList(db.appDao().getAllLop())
        val adapterLop = ArrayAdapter(this, android.R.layout.simple_spinner_item, listLop)
        adapterLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnLop.adapter = adapterLop

        // Sự kiện chọn Lớp
        spnLop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (listLop.isNotEmpty()) {
                    loadHocSinhByLop(listLop[position].maLop)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Sự kiện chọn Học Sinh
        spnHocSinh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (listHocSinh.isNotEmpty()) {
                    selectedMaHS = listHocSinh[position].maHS
                    // Xóa kết quả cũ khi chọn HS mới
                    txtKetQua.text = "Trung Bình: ..."
                    txtXepLoai.text = "Xếp loại: ..."
                    adapterDiem.updateData(emptyList())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedMaHS = ""
            }
        }
    }

    private fun loadHocSinhByLop(maLop: String) {
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
    }

    private fun tinhVaHienThi() {
        if (selectedMaHS.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn học sinh!", Toast.LENGTH_SHORT).show()
            return
        }

        val loaiKT = spnLoaiKT.selectedItem.toString()

        // Lấy danh sách điểm từ Database theo Mã HS và Loại KT
        val listDiem = db.appDao().getDiemByFilter(selectedMaHS, loaiKT)

        if (listDiem.isEmpty()) {
            Toast.makeText(this, "Không có điểm $loaiKT nào cho học sinh này.", Toast.LENGTH_SHORT).show()
            adapterDiem.updateData(emptyList())
            txtKetQua.text = "Trung Bình: 0.0"
            txtXepLoai.text = "Xếp loại: N/A"
            return
        }

        // Hiển thị lên RecyclerView
        adapterDiem.updateData(listDiem)

        // Tính trung bình cộng của các môn được hiển thị
        val tongDiem = listDiem.sumOf { it.diemSo }
        val trungBinh = tongDiem / listDiem.size

        txtKetQua.text = "Trung Bình ($loaiKT): %.2f".format(trungBinh)
        txtXepLoai.text = "Xếp loại: ${xepLoai(trungBinh)}"
    }

    private fun xepLoai(diem: Double): String {
        return when {
            diem >= 8.5 -> "Giỏi"
            diem >= 7.0 -> "Khá"
            diem >= 5.5 -> "Trung bình"
            diem >= 4.0 -> "Yếu"
            else -> "Kém"
        }
    }
}