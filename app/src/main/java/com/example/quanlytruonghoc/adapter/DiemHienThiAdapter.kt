package com.example.quanlytruonghoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlytruonghoc.R
import com.example.quanlytruonghoc.database.BangDiem

class DiemHienThiAdapter(private var listDiem: List<BangDiem>) :
    RecyclerView.Adapter<DiemHienThiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMon: TextView = view.findViewById(R.id.tvMonHoc)
        val tvDiem: TextView = view.findViewById(R.id.tvDiemSo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diem_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDiem[position]
        holder.tvMon.text = item.monHoc
        holder.tvDiem.text = item.diemSo.toString()
    }

    override fun getItemCount(): Int = listDiem.size

    fun updateData(newList: List<BangDiem>) {
        listDiem = newList
        notifyDataSetChanged()
    }
}