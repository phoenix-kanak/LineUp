// login up
package com.example.lineup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.lineup.models.Login
import com.example.lineup.models.Login2
import com.example.lineup.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


public class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var database = FirebaseDatabase.getInstance()
    val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val userInfo = database.getReference("users/$userid")
    private lateinit var zeal_id: String
    private lateinit var auth_password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root);

        val loginbtn = binding.loginBtn
        //   Log.e("zealID","$zeal")
//        userInfo.get().addOnCompleteListener { task ->
//            if(task.isSuccessful){
//                val snapshot=task.result
//                zeal_id=snapshot.child("zealid").value.toString()
//                auth_password=snapshot.child("Password").value.toString()
//
        loginbtn.setOnClickListener {
            val zeal = binding.zeal.text.trim().toString()
            val password = binding.password.text.trim().toString()

            if (zeal.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your ZealId and Password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val userLogin = Login(password, zeal)
                val call = RetrofitApi.apiInterface.login(userLogin)
                call.enqueue(object : Callback<Login2> {
                    override fun onResponse(call: Call<Login2>, response: Response<Login2>) {
                        if (response.isSuccessful) {
                            val bodyReponse = response.body()
                            //    Log.e("id345", "${response.headers()}")
                            Log.e("id234", "$response")
                            if (bodyReponse != null) {
                                Log.e("id234", "$bodyReponse")
                                if (bodyReponse.message == "Login successful") {
                                    Toast.makeText(
                                        this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(this@LoginActivity, CharacterSelect::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity, "User Not Found!", Toast.LENGTH_SHORT
                            ).show()
                            //      Log.e("id3456" , "${response.code()} - ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Login2>, t: Throwable) {
                        Toast.makeText(
                            this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }


//                        if(zeal == zeal_id && password == auth_password){
//                            Toast.makeText(this,"User verified",Toast.LENGTH_SHORT).show()
//                            startActivity(Intent(this,bottom_activity::class.java))
//                            finish()
//                        }else{
//                            Toast.makeText(this,"Oops! User not registered ",Toast.LENGTH_SHORT).show()
//                        }


            //   Log.e("id123", zeal_id)

        }

    }


}