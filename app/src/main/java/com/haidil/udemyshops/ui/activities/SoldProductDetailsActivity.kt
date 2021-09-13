package com.haidil.udemyshops.ui.activities

import android.os.Bundle
import android.view.View
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ActivitySoldProductDetailsBinding
import com.haidil.udemyshops.model.SoldProduct
import com.haidil.udemyshops.util.Constants
import com.haidil.udemyshops.util.GlideLoader
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivitySoldProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        var productDetails: SoldProduct = SoldProduct()
        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            productDetails =
                intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }

        setupUI(productDetails)

    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSoldProductDetailsActivity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
        }

        binding.toolbarSoldProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupUI(productDetails: SoldProduct) {
        binding.tvSoldProductDetailsId.text = productDetails.orderID

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.orderDateTime
        binding.tvSoldProductDetailsDate.text = formatter.format(calendar.time)

        GlideLoader(this).loadProductPicture(
            productDetails.image,
            binding.ivProductItemImage
        )

        binding.tvProductItemName.text = productDetails.title
        binding.tvProductItemPrice.text = "RM${productDetails.price}"
        binding.tvSoldProductQuantity.text = productDetails.soldQuantity.toString()

        binding.tvSoldDetailsAddressType.text = productDetails.address.type
        binding.tvSoldDetailsFullName.text = productDetails.address.name
        binding.tvSoldDetailsAddress.text = "${productDetails.address.address}, ${productDetails.address.zipCode}"
        binding.tvSoldDetailsAdditionalNote.text = productDetails.address.additionalNote

        if(productDetails.address.otherDetails.isNotEmpty()) {
            binding.tvSoldDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvSoldDetailsOtherDetails.text = productDetails.address.otherDetails
        } else {
            binding.tvSoldDetailsOtherDetails.visibility = View.GONE
        }

        binding.tvSoldDetailsMobileNumber.text = resources.getString(R.string.bind_formatted_mobileNumber, productDetails.address.mobileNumber)
        binding.tvSoldProductSubTotal.text = "RM${productDetails.subTotalAmount}"
        binding.tvSoldProductShippingCharge.text = "RM${productDetails.shippingCharge}"
        binding.tvSoldProductTotalAmount.text = "RM${productDetails.totalAmount}"
    }
}