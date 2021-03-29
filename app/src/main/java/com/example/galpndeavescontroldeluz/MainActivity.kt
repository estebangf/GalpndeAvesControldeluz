package com.example.galpndeavescontroldeluz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.example.galpndeavescontroldeluz.R
import com.example.galpndeavescontroldeluz.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in))
        Handler().postDelayed({
            ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_out))
            Handler().postDelayed({
                ic_logo.visibility = View.GONE
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            },500)
        },1500)
    }
}