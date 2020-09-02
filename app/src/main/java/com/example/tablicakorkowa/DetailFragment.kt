package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.databinding.FragmentDetailBinding
import com.example.tablicakorkowa.helpers.subscribe
import com.example.tablicakorkowa.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_offert.view.*
import timber.log.Timber
import java.lang.Exception


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val viewModel: DetailViewModel by viewModels()

    private val args: NavGraphHomeArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        bindUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        bindUIData()
        Timber.e(args.cardId)


        return binding.root
    }

    private fun bindUI(){
        viewModel.getCard(args.cardId)
    }

    private fun bindUIData(){
        viewModel.cards.subscribe(this, ::showCardData)
        viewModel.user.subscribe(this, ::showUserData)
    }

    private fun showUserData(model: UserDto) {
        Glide.with(this).load(model.avatar).fitCenter().centerCrop().placeholder(R.drawable.user_avatar).into(binding.detailAvatar)
        binding.detailPhone.text = model.telephone
        binding.detailEmail.text = model.email

        binding.detailEmail.setOnClickListener {
            sendEmail(model.email, "Korepetycje ${viewModel.cards.value?.title}")
        }
    }

    private fun sendEmail(recipient: String, subject: String){

        val mIntent = Intent(Intent.ACTION_SEND)

        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

        try {
            startActivity(Intent.createChooser(mIntent, "Wybierz klienta poczty"))
        }
        catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showCardData(model: CardsDto) {
        binding.detailTitle.text = model.title
        binding.detailPrice.text = model.price.toString() + " zł"
        if ( model.isOnline ) {
            binding.detailLocation.text = "${model.city}, Online"
        } else {
            binding.detailLocation.text = model.city
        }
        binding.detailDesc.text = model.description
    }
}