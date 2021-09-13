package com.haidil.udemyshops.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ActivityAddressListBinding
import com.haidil.udemyshops.firestore.FirestoreClass
import com.haidil.udemyshops.model.Address
import com.haidil.udemyshops.ui.adapters.AddressListAdapter
import com.haidil.udemyshops.util.Constants
import com.myshoppal.utils.SwipeToDeleteCallback
import com.myshoppal.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {
    private lateinit var binding: ActivityAddressListBinding
    private var mSelectedAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this, AddEditAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        getAddressList()

        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)) {
            mSelectedAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        if (mSelectedAddress) {
            binding.tvTitle.text = resources.getString(R.string.title_select_address)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            getAddressList()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.tbAddressListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp)
        }

        binding.tbAddressListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getAddressList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAddressesList(this)
    }

    fun successGetAddress(addressList: ArrayList<Address>) {
        hideProgressDialog()

        if (addressList.size > 0) {
            binding.tvNoAddressFound.visibility = View.GONE
            binding.rvAddressList.visibility = View.VISIBLE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList, mSelectedAddress)
            binding.rvAddressList.adapter = addressAdapter

            if (!mSelectedAddress) {
                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = binding.rvAddressList.adapter as AddressListAdapter
                        adapter.notifyEditItem(this@AddressListActivity, viewHolder.adapterPosition)
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)

                val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        FirestoreClass().deleteAddress(
                            this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id
                        )
                    }

                }

                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.rvAddressList)
            }

        } else {
            binding.tvNoAddressFound.visibility = View.VISIBLE
            binding.rvAddressList.visibility = View.GONE
        }
    }

    fun deleteAddressSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this, resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }

}