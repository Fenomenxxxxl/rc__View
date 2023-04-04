package com.example.osnovi_recyclerview

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.osnovi_recyclerview.databinding.ItemUserBinding
import com.example.osnovi_recyclerview.model.User

interface UserActionListener {

    fun onUserMove(user: User, moveBy: Int) //когда пользователь хочет переместить человека вверх или вниз

    fun onUserDelete(user: User) //когда пользователь хочет удалить человека

    fun onUserDetails(user: User)   //Когда пользователь нажимаем на человека

    fun onUserFire(user: User) //Увольнение человека

}

class UsersDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>
): DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.id == newUser.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser == newUser
    }

}


class UsersAdapter(
    private val actionListener: UserActionListener
): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener{

    var users: List<User> = emptyList()
    set(newValue) {
        val diffCallback = UsersDiffCallback(field, newValue)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        field = newValue
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onClick(p0: View) {
        val user = p0.tag as User

        when(p0.id) {
            R.id.moreImageViewButton -> {
                showPopupMenu(p0)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }


    class UsersViewHolder(
        val binding: ItemUserBinding
    ): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater,parent,false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        val context = holder.itemView.context
        with(holder.binding) {
            holder.itemView.tag = user
            moreImageViewButton.tag = user
            userNameTextView.text = user.naame
            userCompanyTextView.text = if (user.company.isNotBlank()) user.company else context.getString(R.string.unemployed)
            if (user.photo.isNotBlank()) {
                Glide.with(photoImageView.context) //Сюда передаем контекст который вытянули из photoImageView
                    .load(user.photo) // в метод load передаем ссылку на фотографию которую нужно загрузить
                    .circleCrop() //Делаем её кругой
                    .placeholder(R.drawable.ic_user_avatar) //Очевидно
                    .error(R.drawable.ic_user_avatar) //Очевидно
                    .into(photoImageView)

                } else {
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
                }
        }
    }

    private fun showPopupMenu(p0: View) {
    val popupMenu = PopupMenu(p0.context,p0)
    val context = p0.context
    val user = p0.tag as User
    val position = users.indexOfFirst { it.id == user.id }

        popupMenu.menu.add(0, ID_MORE_UP, Menu.NONE,context.getString(R.string.Move_UP)).apply {
        isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MORE_DOWN, Menu.NONE,context.getString(R.string.Move_DOWN)).apply {
        isEnabled = position < users.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE,context.getString(R.string.Remove))
        if (user.company.isNotBlank()) { //Если у нас компания не пустая
        popupMenu.menu.add(0, ID_FIRE,Menu.NONE,context.getString(R.string.fire))
        }

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                ID_MORE_UP -> {
                    actionListener.onUserMove(user,-1)
                }
                ID_MORE_DOWN -> {
                    actionListener.onUserMove(user,1)
                }
                ID_REMOVE -> {
                    actionListener.onUserDelete(user)
                }
                ID_FIRE -> {
                    actionListener.onUserFire(user)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    override fun getItemCount(): Int {
        return users.size
    }

    companion object {
    private const val ID_MORE_UP = 1
    private const val ID_MORE_DOWN = 2
    private const val ID_REMOVE = 3
    private const val ID_FIRE = 4
    }

}