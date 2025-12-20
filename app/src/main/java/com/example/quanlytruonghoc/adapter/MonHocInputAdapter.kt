package com.example.quanlytruonghoc.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlytruonghoc.R
import com.example.quanlytruonghoc.model.MonHocInput

class MonHocInputAdapter(private val listMon: List<MonHocInput>) :
    RecyclerView.Adapter<MonHocInputAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTen = view.findViewById<TextView>(R.id.tvTenMon)
        val edtDiem = view.findViewById<EditText>(R.id.edtDiemMon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nhap_diem_mon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listMon[position]
        holder.tvTen.text = item.tenMon

        // Quan trọng: Xóa listener cũ để tránh lỗi khi scroll
        holder.edtDiem.setOnFocusChangeListener(null)

        // Set giá trị hiện tại
        holder.edtDiem.setText(item.diem)

        // Lưu dữ liệu khi người dùng nhập
        holder.edtDiem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                item.diem = s.toString()
            }
        })
    }

    override fun getItemCount(): Int = listMon.size
}