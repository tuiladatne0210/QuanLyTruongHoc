package com.example.quanlytruonghoc

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quanlytruonghoc.database.AppDatabase
import com.example.quanlytruonghoc.database.HanhKiem
import com.example.quanlytruonghoc.database.HocSinh

class QuanLyHanhKiemActivity : AppCompatActivity() {
    lateinit var spnHocSinh: Spinner
    lateinit var edtNoiDung: EditText
    lateinit var lvHienThi: ListView
    lateinit var db: AppDatabase

    var listHS = ArrayList<HocSinh>()
    var selectedMaHS = ""
    var listHanhKiemString = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_hanh_kiem)

        db = AppDatabase.getDatabase(this)
        spnHocSinh = findViewById(R.id.spnHocSinhHK)
        edtNoiDung = findViewById(R.id.edtNoiDungHK)
        lvHienThi = findViewById(R.id.lvHanhKiem)

        listHS = ArrayList(db.appDao().getAllHocSinh())
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, listHS)
        spnHocSinh.adapter = adapterSpinner

        spnHocSinh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(listHS.isNotEmpty()){
                    selectedMaHS = listHS[p2].maHS
                    loadHanhKiem()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        findViewById<Button>(R.id.btnThemHK).setOnClickListener {
            val noiDung = edtNoiDung.text.toString()
            if (noiDung.isNotEmpty() && selectedMaHS.isNotEmpty()) {
                db.appDao().themHanhKiem(HanhKiem(0, selectedMaHS, noiDung))
                Toast.makeText(this, "Đã thêm!", Toast.LENGTH_SHORT).show()
                edtNoiDung.setText("")
                loadHanhKiem()
            }
        }
    }

    fun loadHanhKiem() {
        listHanhKiemString.clear()
        val rawData = db.appDao().getHanhKiemByHocSinh(selectedMaHS)
        rawData.forEach {
            listHanhKiemString.add("- ${it.noiDung}")
        }
        val adapterListView = ArrayAdapter(this, android.R.layout.simple_list_item_1, listHanhKiemString)
        lvHienThi.adapter = adapterListView
    }
}