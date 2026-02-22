package com.sjshb57.kana.module.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sjshb57.kana.R
import com.sjshb57.kana.module.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        // 隐藏ActionBar
        supportActionBar?.hide()

        // 延迟1.5秒后跳转主页
        window.decorView.postDelayed({
            if (!isFinishing) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 1500)
    }

    // 禁止返回键，防止用户在启动页按返回后又跳回来
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // 不做任何处理
    }
}