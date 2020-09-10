package com.example.tablicakorkowa.adapters

import android.view.View
import com.example.tablicakorkowa.R
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.ModelAbstractItem
import kotlinx.android.synthetic.main.list_item_user_offert.view.*

open class UserCardsListAdapter (model: CardsDto): ModelAbstractItem<CardsDto, UserCardsListAdapter.UserCardsViewHolder>(model){

    override val layoutRes: Int
        get() = R.layout.list_item_user_offert

    override val type: Int
        get() = R.id.user_cards_type_id

    override fun getViewHolder(v: View): UserCardsViewHolder {
        return UserCardsViewHolder(v)
    }

    class UserCardsViewHolder(itemView: View) : FastAdapter.ViewHolder<UserCardsListAdapter>(itemView){

        override fun bindView(item: UserCardsListAdapter, payloads: MutableList<Any>) {
            val model = item.model

            itemView.userCardsTitle.text = model.title
            itemView.userCardsCity.text = model.city
            itemView.userCardsPrice.text = "${model.price} zł/h"
        }

        override fun unbindView(item: UserCardsListAdapter) {
            itemView.userCardsTitle.text = null
            itemView.userCardsCity.text = null
            itemView.userCardsPrice.text = null
        }
    }
}