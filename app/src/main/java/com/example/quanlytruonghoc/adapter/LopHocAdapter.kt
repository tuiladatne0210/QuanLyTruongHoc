package com.example.quanlytruonghoc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.quanlytruonghoc.R
import com.example.quanlytruonghoc.database.LopHoc

class LopHocAdapter(var context: Context, var ds: ArrayList<LopHoc>): BaseAdapter() {
    override fun getCount(): Int  = ds.size


    override fun getItem(position: Int): Any = ds[position]


    override fun getItemId(position: Int): Long = position.toLong()


    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val row = convertView?: LayoutInflater.from(context).inflate(R.layout.item_lophoc, parent, false)
        val txtTenLop = row.findViewById<TextView>(R.id.txtTenLop)
        val txtGiaoVien = row.findViewById<TextView>(R.id.txtGiaoVien)
        val lh = ds[position]
        txtTenLop.text = "Ten lop: ${lh.tenLop}"
        txtGiaoVien.text = "GVCN: ${lh.giaoVien}"
        return row
    }

}