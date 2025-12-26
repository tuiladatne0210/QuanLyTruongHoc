package com.example.quanlytruonghoc

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        role = intent.getStringExtra("ROLE") ?: "student"

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
        setPermission()
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close // Cần thêm chuỗi này vào strings.xml hoặc dùng cứng
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_lop -> startActivity(Intent(this, QuanLyLopActivity::class.java))
            R.id.nav_hocsinh -> startActivity(Intent(this, QuanLyHocSinhActivity::class.java))
            R.id.nav_diem -> startActivity(Intent(this, QuanLyDiemActivity::class.java))
            R.id.nav_hanhkiem -> startActivity(Intent(this, QuanLyHanhKiemActivity::class.java))
            R.id.nav_tinh_nhanh -> startActivity(Intent(this, TinhDiemNhanhActivity::class.java))
            R.id.nav_logout -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun setPermission(){
        val menu = navView.menu

        when(role.lowercase()){
            "admin" -> {
                //admin: toan quyen
            }
            "teacher" -> {
                menu.findItem(R.id.nav_lop).isVisible = false
                menu.findItem(R.id.nav_hocsinh).isVisible = false
            }
            "student" -> {
                menu.findItem(R.id.nav_lop).isVisible = false
                menu.findItem(R.id.nav_hocsinh).isVisible = false
                menu.findItem(R.id.nav_diem).isVisible = false
            }
        }
    }
}