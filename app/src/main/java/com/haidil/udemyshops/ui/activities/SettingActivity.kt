package com.haidil.udemyshops.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ActivitySettingBinding
import com.haidil.udemyshops.firestore.FirestoreClass
import com.haidil.udemyshops.model.User
import com.haidil.udemyshops.util.Constants
import com.haidil.udemyshops.util.GlideLoader

class SettingActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        binding.tvEdit.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.llAddress.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp)
        }

        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        mUserDetails = user

        hideProgressDialog()
        GlideLoader(this@SettingActivity).loadingUserPicture(user.image, binding.ivUserPhoto)
        binding.tvName.text = getString(R.string.bind_fullName, user.firstName, user.lastName)
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = getString(R.string.bind_formatted_mobileNumber, user.mobile)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.tv_edit -> {
                    val intent = Intent(this@SettingActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }

                R.id.ll_address -> {
                    val intent = Intent(this, AddressListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}