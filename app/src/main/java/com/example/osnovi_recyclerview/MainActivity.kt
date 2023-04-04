package com.example.osnovi_recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osnovi_recyclerview.databinding.ActivityMainBinding
import com.example.osnovi_recyclerview.model.User
import com.example.osnovi_recyclerview.model.UserService
import com.example.osnovi_recyclerview.model.UsersListener

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding //добавляем биндинг
    lateinit var adapter: UsersAdapter // добавляем адаптер

    private val usersService: UserService
    get() = (applicationContext as App).usersService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                usersService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity,"User: ${user.naame}",Toast.LENGTH_LONG).show()
            }

            override fun onUserFire(user: User) {
                usersService.fireUser(user)
            }

        })

        val layoutManager = LinearLayoutManager(this)
        binding.rcView.layoutManager = layoutManager
        binding.rcView.adapter = adapter


        usersService.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
    adapter.users = it
    }

}