package com.example.escuteiros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.escuteiros.Models.Adapter
import com.google.android.material.tabs.TabLayout

class GestaoPresencas : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_gestao_presencas)

            val viewPager = findViewById<ViewPager>(R.id.viewPager)
            viewPager.adapter = Adapter(supportFragmentManager)

            val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
            tabLayout.setupWithViewPager(viewPager)
        }
}
