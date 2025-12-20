package com.example.quanlytruonghoc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quanlytruonghoc.database.AppDatabase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtUser = findViewById<EditText>(R.id.edtUserLogin)
        val edtPass = findViewById<EditText>(R.id.edtPassLogin)
        val btnLogin = findViewById<Button>(R.id.btnLoginAction)
        val tvRegister = findViewById<TextView>(R.id.tvGoToRegister)

        try {
            val db = AppDatabase.getDatabase(this)

            btnLogin.setOnClickListener {
                try {
                    val user = edtUser.text.toString().trim()
                    val pass = edtPass.text.toString().trim()

                    if (user.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                    } else {
                        val account = db.appDao().loginUser(user, pass)
                        if (account != null) {
                            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Lỗi đăng nhập: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khởi tạo Database: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}