package com.github.tehras.base.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.tehras.base.demo.ui.doglist.BreedListFragment
import kotlinx.android.synthetic.main.activity_demo.*

/**
 * @author tkoshkin
 */
class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_demo)

        if (savedInstanceState == null) {
            showFragment(BreedListFragment.instance())
        }
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(fragment_container.id, fragment)
            .addToBackStack(fragment::class.simpleName)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
 