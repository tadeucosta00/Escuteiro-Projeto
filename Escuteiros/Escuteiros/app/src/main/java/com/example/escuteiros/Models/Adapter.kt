package com.example.escuteiros.Models


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.escuteiros.ui.gerirPresencas.VerQuemNaoVaiFragment
import com.example.escuteiros.ui.gerirPresencas.VerQuemVaiFragment

class Adapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return VerQuemVaiFragment()
            }
            1 -> {
                return VerQuemNaoVaiFragment()
            }
            else -> {
                return VerQuemNaoVaiFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Presente"
            }
            1 -> {
                return "Não Presente"
            }

        }
        return super.getPageTitle(position)
    }
}