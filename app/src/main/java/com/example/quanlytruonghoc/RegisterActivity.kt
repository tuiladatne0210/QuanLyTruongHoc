package com.example.quanlytruonghoc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quanlytruonghoc.database.AppDatabase
import com.example.quanlytruonghoc.database.User

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edtUser = findViewById<EditText>(R.id.edtUserReg)
        val edtPass = findViewById<EditText>(R.id.edtPassReg)
        val btnReg = findViewById<Button>(R.id.btnRegisterAction)
        val tvBack = findViewById<TextView>(R.id.tvBackLogin)
        val db = AppDatabase.getDatabase(this)

        btnReg.setOnClickListener {
            val user = edtUser.text.toString().trim()
            val pass = edtPass.text.toString().trim()
            if (user.isNotEmpty() && pass.isNotEmpty()) {
                val check = db.appDao().registerUser(User(user, pass))
                if (check != -1L) {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        tvBack.setOnClickListener { finish() }
    }
}