package com.example.quanlytruonghoc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.quanlytruonghoc.R
import com.example.quanlytruonghoc.database.HocSinh

class HocSinhAdapter(val context: Context, var ds: List<HocSinh>) : BaseAdapter() {

    override fun getCount(): Int = ds.size
    override fun getItem(position: Int): Any = ds[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_hocsinh, parent, false)

        val txtMaHS = row.findViewById<TextView>(R.id.txtMaHS)
        val txtTenHS = row.findViewById<TextView>(R.id.txtTenHS)
        val txtNgaySinh = row.findViewById<TextView>(R.id.txtNgaySinh)

        val hs = ds[position]
        txtMaHS.text = "Mã: ${hs.maHS}"
        txtTenHS.text = "Tên: ${hs.tenHS}"
        txtNgaySinh.text = "Ngày sinh: ${hs.ngaySinh}"

        return row
    }

    // Hàm cập nhật dữ liệu mới
    fun updateData(newList: List<HocSinh>) {
        ds = newList
        notifyDataSetChanged()
    }
}