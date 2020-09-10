package com.example.tablicakorkowa

import android.R.attr.data
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tablicakorkowa.adapters.UserCardsListAdapter
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.databinding.FragmentUserCardsBinding
import com.example.tablicakorkowa.helpers.buttonEffect
import com.example.tablicakorkowa.helpers.hideProgress
import com.example.tablicakorkowa.helpers.subscribe
import com.example.tablicakorkowa.viewmodel.UserCardsViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import timber.log.Timber
import java.util.*
import kotlin.Comparator


class UserCardsFragment : Fragment() {

    private lateinit var binding: FragmentUserCardsBinding

    private val args: NavGraphDashboardArgs by navArgs()

    private val viewModel: UserCardsViewModel by viewModels()

    private val itemAdapter = ItemAdapter<UserCardsListAdapter>()

    private val fastAdapter = FastAdapter.with(itemAdapter)

    private lateinit var mHandler: Handler

    private lateinit var mRunnable:Runnable
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindUI()
        Timber.e(args.userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserCardsBinding.inflate(inflater, container, false)
        bindUIData()
        binding.userCardsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            adapter = fastAdapter
        }

        buttonEffect(binding.userCardsBackButton)

        binding.userCardsBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        mHandler = Handler()

        binding.userCardsSwipeToRefresh.setOnRefreshListener {
            mRunnable = Runnable {
                    itemAdapter.clear()
                    bindUI()
                    bindUIData()
            }
            mHandler.post(mRunnable)
        }

        return binding.root
    }


    private fun bindUI(){
        viewModel.getAllInfo(args.userId)
    }

    private fun bindUIData() {
        viewModel.cards.subscribe(this, ::addAdapterItem)
        viewModel.users.subscribe(this, ::showUserData)
        hideProgress(binding.userCardsSwipeToRefresh)
    }

    private fun addAdapterItem(model: List<CardsDto>){
        val sortedModel = model.sortedBy { it.title }

        val items = sortedModel.map {
            UserCardsListAdapter(it)
        }


        itemAdapter.setNewList(items)

        fastAdapter.onClickListener = { _, _, item, _ -> onItemClicked(item)}
    }

    private fun onItemClicked(item: UserCardsListAdapter): Boolean{

        val product = item.model

        val id = product.id
        val action = UserCardsFragmentDirections.actionUserCardsFragmentToDetail(id)
        findNavController().navigate(action)

        return true
    }

    private fun showUserData(model: UserDto){
        binding.userCardsName.text = "Oferta ogłoszeń ${model.firstname} ${model.lastname}"
        Glide.with(this).load(model.avatar).fitCenter().centerCrop().placeholder(R.drawable.user_avatar).into(
            binding.usersCardsAvatar
        )
    }


}