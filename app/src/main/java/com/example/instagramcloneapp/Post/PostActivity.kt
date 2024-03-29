package com.example.instagramcloneapp.Post


import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramcloneapp.HomeActivity
import com.example.instagramcloneapp.Models.Post
import com.example.instagramcloneapp.Models.User
import com.example.instagramcloneapp.databinding.ActivityPostBinding
import com.example.instagramcloneapp.utils.POST
import com.example.instagramcloneapp.utils.POST_FOLDER
import com.example.instagramcloneapp.utils.USER_NODE
import com.example.instagramcloneapp.utils.USER_PROFILE_FOLDER
import com.example.instagramcloneapp.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl:String?=null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {

            uploadImage(uri, POST_FOLDER) {
                url->
                if (url != null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document().get().addOnSuccessListener {
                var user=it.toObject<User>()!!
                val post:Post= Post(
                    postUrl = imageUrl!!,
                    caption = binding.caption.editText?.text.toString(),
                    Uid = Firebase.auth.currentUser!!.uid,
                    time=System.currentTimeMillis().toString()
                )
            }
            val post:Post=Post(imageUrl!!,binding.caption.editText?.text.toString())

            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
                    startActivity(Intent(this,HomeActivity::class.java))
                    finish()
                }
            }
        }

    }
}