// sign up
package com.example.lineup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.lineup.RetrofitApi.apiInterface
import com.example.lineup.models.SignUp
import com.example.lineup.models.SignUp2
import com.example.lineup.databinding.ActivitySignUpBinding
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var databaseReference = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullname = binding.name
        val Email = binding.email
        val zealid = binding.zeal
        val Password = binding.password
        val Registbutton = binding.regtbtn

        val sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()



        Registbutton.setOnClickListener {

            val fullnametxt = fullname.text.trim().toString()
            val emailtxt = Email.text.toString()
            val zealidtxt = zealid.text.trim().toString()
            val passwordtxt = Password.text.trim().toString()


            val userSignUp = SignUp(emailtxt, passwordtxt, fullnametxt, zealidtxt)
            val call = apiInterface.signup(userSignUp)
            if (fullnametxt.isEmpty() || emailtxt.isEmpty() || zealidtxt.isEmpty() || passwordtxt.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                call.enqueue(object : Callback<SignUp2> {
                    override fun onResponse(call: Call<SignUp2>, response: Response<SignUp2>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                editor.putString("Token", responseBody.token)
                            }
                            editor.apply()
                            //   if(response.body().code)

                            //Log.e("id123", "$response")

                            if (responseBody != null) {
                                //  Log.e("id123", "$responseBody")
                                Log.e("id123", "${responseBody.code}")
                                if (responseBody.message == "Signup successful") {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Registered Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(this@SignUpActivity, CharacterSelect::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                        } else {
                            Log.e("id123", "${response.code()} - ${response.message()}")

                        }
                    }

                    override fun onFailure(call: Call<SignUp2>, t: Throwable) {
                        Toast.makeText(
                            this@SignUpActivity, t.message.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

//            if (fullnametxt.isEmpty() || emailtxt.isEmpty() || zealidtxt.isEmpty() || passwordtxt.isEmpty()) {
//                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
//            } else {
//                Log.e("error6" , "abcder")
//                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailtxt,passwordtxt).addOnCompleteListener{
//                    if(it.isSuccessful){
//                        val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
//                        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
//                            //  @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                if (dataSnapshot.hasChild(userid)) {
//                                    Log.e("error2" , "abcder")
//                                    Toast.makeText(this@SignUpActivity, "Zeal id is already registered..", Toast.LENGTH_SHORT).show()
//                                } else {
//                                    Log.e("error3" , "abcder")
//                                    // Store user data without whitespaces in keys
//                                    databaseReference.child(userid).child("FullName").setValue(fullnametxt)
//                                    databaseReference.child(userid).child("Email").setValue(emailtxt)
//                                    databaseReference.child(userid).child("Password").setValue(passwordtxt)
//                                    databaseReference.child(userid).child("zealid").setValue(zealidtxt)
//                                    Toast.makeText(this@SignUpActivity, "User Registered Successfully.", Toast.LENGTH_SHORT).show()
//                                    val i= Intent(this@SignUpActivity,bottom_activity::class.java)
//                                    startActivity(i)
//                                    finish()
//                                }
//                            }
//
//                            override fun onCancelled(databaseError: DatabaseError) {
//                                Log.e("error4" , "abcder")
//                                Toast.makeText(this@SignUpActivity, "Failed to register user...", Toast.LENGTH_SHORT).show()
//                            }
//                        })
//                                }else{
//                        Toast.makeText(this,"Error! Please choose a strong password",Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
        }
    }
}
