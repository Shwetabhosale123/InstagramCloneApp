package com.example.instagramcloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramcloneapp.Models.User
import com.example.instagramcloneapp.databinding.ActivitySignUpBinding
import com.example.instagramcloneapp.utils.USER_NODE
import com.example.instagramcloneapp.utils.USER_PROFILE_FOLDER
import com.example.instagramcloneapp.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {

            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it != null) {
                    user.image = it
                    binding.profileImage.setImageURI(uri)

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text =
            "<font color=#FF000000>Already Have an Account</font> <font color=#1E88E5>Login?</font>"
        binding.login.setText(Html.fromHtml(text))
        user = User()

        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 1) {

                binding.signUpBtn.text = "Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener {

                        user = it.toObject<User>()!!

                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }
                        binding.textFieldName.editText?.setText(user.name)
                        binding.textFieldEmail.editText?.setText(user.email)
                        binding.textFieldPassword.editText?.setText(user.password)
                    }
            }

        }
        binding.signUpBtn.setOnClickListener {

            if (intent.hasExtra("MODE")){
                if (intent.getIntExtra("MODE", -1) == 1){
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                }
            }else {
                if (binding.textFieldName.editText?.text.toString().equals("") or
                    binding.textFieldEmail.editText?.text.toString().equals("") or
                    binding.textFieldPassword.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
                } else {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.textFieldEmail.editText?.text.toString(),
                        binding.textFieldPassword.editText?.text.toString()
                    ).addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            user.name = binding.textFieldName.editText?.text.toString()
                            user.password = binding.textFieldPassword.editText?.text.toString()
                            user.email = binding.textFieldEmail.editText?.text.toString()

                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }

                        } else {
                            Toast.makeText(
                                this,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }
        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}