package com.haidil.udemyshops.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.DialogProgressBinding

open class BaseActivity : AppCompatActivity() {
    private lateinit var mProgressDialog: Dialog
    private lateinit var binding: DialogProgressBinding

    private var doubleBackToExitPressedOnce = false

    fun showErrorSnackbar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.snackBarError
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.snackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)
        binding = DialogProgressBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(binding.root)

        binding.tvProgressBar.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun setupActionBar(toolbar: androidx.appcompat.widget.Toolbar, isDefault: Boolean) {
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            if (isDefault) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
            } else {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
            }
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}