package com.haidil.udemyshops.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar(binding.toolbarForgotPasswordActivity, false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.btnSubmit.setOnClickListener {
            val email = binding.etEmail.text.toString().trim { it <= ' ' }

            if (email.isEmpty()) {
                showErrorSnackbar(
                    resources.getString(R.string.err_msg_enter_email),
                    true
                )
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))

                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    hideProgressDialog()

                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            resources.getString(R.string.email_sent_success),
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    } else {
                        showErrorSnackbar(task.exception!!.message.toString(), true)
                    }
                }
            }
        }
    }
}