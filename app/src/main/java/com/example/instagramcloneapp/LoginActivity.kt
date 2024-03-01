package com.example.instagramcloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagramcloneapp.Models.User
import com.example.instagramcloneapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener{

            if(binding.textFieldEmail.editText?.text.toString().equals("")or
                binding.textFieldPassword.editText?.text.toString().equals("")){
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }else{
                var user=User(binding.textFieldEmail.editText?.text.toString(),
                               binding.textFieldPassword.editText?.text.toString())


               Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!)
                   .addOnCompleteListener {
                       if(it.isSuccessful){
                           startActivity(Intent(this,HomeActivity::class.java))
                           finish()
                       }else{
                           Toast.makeText(this,it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                       }
                   }
            }
        }
    }
}