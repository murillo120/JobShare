package com.js.jobshare.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.js.jobshare.R
import com.js.jobshare.viewmodels.ViewModelMain
import kotlinx.android.synthetic.main.login_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class Login_Activity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    val context = this
    val viewmodel by viewModel<ViewModelMain>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val window = this.window

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        viewmodel.login_key.observe(this, Observer<Boolean> { t ->
            if (t!!) {
                val intent = Intent(context, Home_activity::class.java)
                intent.putExtra("userdata",edit_reg_userEmail.text.toString())
                login_progressbar.visibility = View.GONE

                val sharedPreference =  getSharedPreferences("JobShare_id",Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()

                val email = edit_reg_userEmail.text.toString()
                val password = edit_reg_PassWord.text.toString()

                editor.putString("username",email)
                editor.putString("password",password)
                editor.apply()

                Log.d("abacaxi", "data saved on shared prefs")

                startActivity(intent)
                finish()
            } else {

                Toast.makeText(context, viewmodel.exception, Toast.LENGTH_SHORT).show()
                login_progressbar.visibility = View.GONE


            }
        })

        login_progressbar.visibility = View.GONE


        auth = FirebaseAuth.getInstance()


        btn_login.setOnClickListener {

            login_progressbar.visibility = View.VISIBLE

            val email = edit_reg_userEmail.text.toString()
            val password = edit_reg_PassWord.text.toString()

            if (email.isEmpty()) {
                edit_reg_userEmail.setError("preencha o Campo de E-mail corretamente")

                return@setOnClickListener
            }

            if (password.isEmpty()) {
                edit_reg_PassWord.setError("preencha o Campo de Senha Corretamente")

                return@setOnClickListener
            }

            viewmodel.executeLoginToHome(email, password)


        }

        flat_register.setOnClickListener {

            val intent = Intent(context, Register_activity::class.java)

            startActivity(intent)
            finish()

        }
    }


}
