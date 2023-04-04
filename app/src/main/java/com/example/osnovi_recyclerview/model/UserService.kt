package com.example.osnovi_recyclerview.model

import com.example.osnovi_recyclerview.R
import com.github.javafaker.Faker
import java.util.*
import kotlin.collections.ArrayList

typealias UsersListener = (users: List<User>) -> Unit
class UserService() {

    private var users = mutableListOf<User>()

    private val listeners = mutableSetOf<UsersListener>()

    init {
        val faker = Faker.instance()
        IMAGES.shuffle()
        users = (1..100).map {
            User(
                id = it.toLong(),
                naame = faker.name().name(),
                company = faker.company().name(),
                photo = IMAGES[it % IMAGES.size]
            )
        }.toMutableList()
    }

    fun getUsers(): List<User> {
        return users
    }

    fun deleteUser(user: User) {
    val indexToDelete = users.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            users = ArrayList(users)
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun moveUser(user:User, moveBy:Int) {
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= users.size) return
        users = ArrayList(users)
        Collections.swap(users,oldIndex,newIndex)
        notifyChanges()
    }

    fun fireUser(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index == -1) return
        val updateUser = users[index].copy(company = "")
        users = ArrayList(users) //создали новый список на базе старого
        users[index] = updateUser
        notifyChanges()
    }



    fun addListener(listener: UsersListener) {
    listeners.add(listener)
        listener.invoke(users)
    }

    fun removeListener(listener: UsersListener) {
    listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach {it.invoke(users)}
    }


    companion object {
        private val IMAGES = mutableListOf(
            "https://ru.fishki.net/picsw/032011/02/post/portret/portret-022.jpg",
            "https://4tololo.ru/sites/default/files/inline/images/2019/03/27-1532-1218881252.jpg",
            "https://ru.fishki.net/picsw/032011/02/post/portret/portret-002.jpg",
            "https://img1.goodfon.ru/original/1400x1050/1/8f/dzhud-lou-akter-muzhchina-lico.jpg",
            "https://cdn.fishki.net/upload/post/201408/07/1291681/portret-7.jpg",
           "https://i.pinimg.com/originals/dd/e6/5f/dde65fd3488ea37fdd5c2a9bcb473df4.jpg",
        )
    }
}