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

                val user = db.appDao().loginUser(
                    edtUser.text.toString().trim(),
                    edtPass.text.toString().trim()
                )
                if(user == null){
                    Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
               val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("ROLE", user.role)
                intent.putExtra("USERNAME", user.username)
                startActivity(intent)
                finish()
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khởi tạo Database: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}