package ru.a_party.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import ru.a_party.mymap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId==R.id.sList){
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragmentContainer.id,ListMarkerFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
                return true
            }

        })

        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainer.id,MapFragment.newInstance())
            .addToBackStack(null)
            .commit()

    }
}