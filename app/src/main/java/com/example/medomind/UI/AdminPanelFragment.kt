package com.example.medomind.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.medomind.R
import com.example.medomind.UI.Fragment.FragmentResource
import com.example.medomind.UI.Login.DOctor.DoctorLoginActivity
import com.example.medomind.utils.Utils.Companion.USER_NAME
import com.example.medomind.utils.Utils.Companion.getUserName
import com.example.medomind.utils.Utils.Companion.logout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*

class AdminPanelFragment : AppCompatActivity() {
    val adminName = getUserName(USER_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        loggedInAdminName.text = adminName

        btnAdminLogout0.setOnClickListener {
            logout()
            startActivity(Intent(this, DoctorLoginActivity::class.java))
            finish()
        }
        renderViewPager()
        renderTabLayout()
    }

    private fun renderViewPager() {
        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return FragmentResource.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return FragmentResource.tabList.size
            }
        }
    }

    private fun renderTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = FragmentResource.tabList[position]
        }.attach()
    }
}