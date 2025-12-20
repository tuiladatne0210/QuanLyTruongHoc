package com.example.quanlytruonghoc.model

data class MonHocInput(
    val tenMon: String,
    var diem: String = "" // Lưu điểm dạng chuỗi để hứng từ EditText
)