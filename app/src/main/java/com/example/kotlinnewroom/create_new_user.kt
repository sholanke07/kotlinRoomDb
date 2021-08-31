package com.example.kotlinnewroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_new_user.*

class create_new_user : AppCompatActivity() {
    lateinit var viewModel: CreateNewUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_user)

        val user_id = intent.getStringExtra("user_id")
        initViewModel()
        createUserObservable()

        if (user_id != null){
            loadUserDta(user_id)
        }

        createButton.setOnClickListener {
            createUser(user_id)
        }
        deleteButton.setOnClickListener {
            deleteUser(user_id)
        }
    }
    private fun deleteUser(user_id: String?){
        viewModel.getdeleteUserObservable().observe(this, Observer<UserResponse?> {
            if (it != null) {
                Toast.makeText(this@create_new_user, "Fail to delete user....", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@create_new_user, "Successfully deleted user....", Toast.LENGTH_LONG).show()
                finish()
            }
        })
        viewModel.deleteUser(user_id)

    }
    private fun loadUserDta(user_id: String?){
        viewModel.getLoadUserObservable().observe(this, Observer<UserResponse?> {
            if (it != null) {
                editTextName.setText(it.data?.name)
                editTextEmail.setText(it.data?.email)
                createButton.setText("update")
                deleteButton.visibility = View.VISIBLE
            }
        })
        viewModel.getUesrData(user_id)
    }
    private fun createUser(user_id: String?){
        val user = User(" ", editTextName.text.toString(), editTextEmail.text.toString(), "Active", "Male")

        if (user_id == null){
            viewModel.createUser(user)
        }else{
            viewModel.updateUser(user_id, user)
        }

        viewModel.createUser(user)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(CreateNewUserViewModel::class.java)
    }
    private fun createUserObservable(){
        viewModel.getCreateNewUserObservable().observe(this, Observer<UserResponse?> {
            if (it == null){
                Toast.makeText(this@create_new_user, "Fail to create/update new user....", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@create_new_user, "Successfully created/update new user....", Toast.LENGTH_LONG).show()
            finish()
            }
        })

    }
}