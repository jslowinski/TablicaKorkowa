package com.example.tablicakorkowa

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tablicakorkowa.adapters.DashboardListAdapter
import com.example.tablicakorkowa.adapters.HomeListAdapter
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.databinding.FragmentHomeBinding
import com.example.tablicakorkowa.helpers.MyButton
import com.example.tablicakorkowa.helpers.MySwipeHelper
import com.example.tablicakorkowa.helpers.SwipeToDeleteCallback
import com.example.tablicakorkowa.helpers.subscribe
import com.example.tablicakorkowa.listener.MyButtonClickListener
import com.example.tablicakorkowa.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel : HomeViewModel by viewModels()

    private val itemAdapter = ItemAdapter<HomeListAdapter>()

    private val fastAdapter = FastAdapter.with(itemAdapter)

    private lateinit var confirmDialog: AlertDialog

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

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showPopup(itemAdapter.getAdapterItem(viewHolder.adapterPosition).model.id, viewHolder.adapterPosition)

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recycler)

        fastAdapter.onClickListener = {_, _, item, _ -> onItemClicked(item)}

        binding.buttonDetail.setOnClickListener {
            openAddNewMode()
        }
        return binding.root
    }

    private fun onItemClicked(item: HomeListAdapter): Boolean{
        val product = item.model
        openEditMode(product.id)
        return true
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

    private fun openEditMode(id: String){
        val action = HomeFragmentDirections.actionHomeToNewOfferFragment(id)
        findNavController().navigate(action)
    }

    private fun openAddNewMode(){
        val action = HomeFragmentDirections.actionHomeToNewOfferFragment("new")
        findNavController().navigate(action)
    }

    private fun showPopup(id: String, pos: Int){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.confirm_popup, null)
        val buttonYes = dialogLayout.findViewById<Button>(R.id.popupYes)
        val buttonNo = dialogLayout.findViewById<Button>(R.id.popupNo)

        buttonNo.setOnClickListener {
            confirmDialog.cancel()
            binding.recycler.adapter = null
            binding.recycler.layoutManager = null
            binding.recycler.adapter = fastAdapter
            binding.recycler.layoutManager = LinearLayoutManager(context)
            fastAdapter.notifyDataSetChanged()
        }

        buttonYes.setOnClickListener {
            viewModel.deleteCard(id)
            viewModel.deleteResult.observe(requireActivity(), Observer {
                bindUI()
                fastAdapter.notifyAdapterDataSetChanged()
                confirmDialog.cancel()
            })
        }

        builder.setView(dialogLayout)

        confirmDialog = builder.create()
        confirmDialog.show()
    }
}