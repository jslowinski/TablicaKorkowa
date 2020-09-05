package com.example.tablicakorkowa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tablicakorkowa.adapters.HomeListAdapter
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.databinding.FragmentHomeBinding
import com.example.tablicakorkowa.helpers.subscribe
import com.example.tablicakorkowa.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import timber.log.Timber


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel : HomeViewModel by viewModels()

    private val itemAdapter = ItemAdapter<HomeListAdapter>()

    private val fastAdapter = FastAdapter.with(itemAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeFragment", "onCreate")
        bindUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindUIData()
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            adapter = fastAdapter
        }

        return binding.root
    }

    fun navigateToDetail(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(context, SignInActivity::class.java))
        requireActivity().finish()
    }

    fun showId(view: View) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Timber.e(uid)
    }

    private fun bindUI(){
        viewModel.getAllCards(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    private fun bindUIData(){
        viewModel.cards.subscribe(this, ::addAdapterItem)
    }

    private fun addAdapterItem(model: List<CardsDto>) {
        val sortedModel = model.sortedBy { it.title }
        val items = sortedModel.map{
            HomeListAdapter(it)
        }

        itemAdapter.setNewList(items)

        fastAdapter.onTouchListener
    }
}