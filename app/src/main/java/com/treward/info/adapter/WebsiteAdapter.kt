package com.treward.info.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.treward.info.R
import com.treward.info.listener.UpdateListener
import com.treward.info.models.WebsiteModel
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class WebsiteAdapter(
    private val mList: List<WebsiteModel>,
    private val updateListener: UpdateListener,
    private val type: String,
    private val context: Context
) : RecyclerView.Adapter<WebsiteAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val visit_coin: TextView = ItemView.findViewById(R.id.visit_coin)
        val visit_desc: TextView = ItemView.findViewById(R.id.visit_desc)
        val visit_title: TextView = ItemView.findViewById(R.id.visit_title)
        val visitCard: CardView = ItemView.findViewById(R.id.visit)
        val visitLogo: ImageView = ItemView.findViewById(R.id.visit1_logo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.website_visit_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(mList[position]) {
            holder.visit_title.text = visit_title
            holder.visit_coin.text = visit_coin
            holder.visitCard.setOnClickListener {
                updateListener.UpdateListener(
                    coin = visit_coin,
                    time = visit_timer,
                    link = visit_link,
                    browser = browser,
                    id = id,
                    index = position,
                    description = description,
                    logo = logo,
                    packages = packages,
                )
            }
            when {
                type.equals("website", ignoreCase = true) -> {
                    holder.visit_desc.text = "Visit for $visit_timer minutes"
                    holder.visitLogo.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.visit
                        )
                    )
                }
                type.equals("video", ignoreCase = true) -> {
                    holder.visit_desc.text = "Watch for $visit_timer minutes"
                    holder.visitLogo.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.showadd
                        )
                    )
                }
                type.equals("app", ignoreCase = true) -> {
                    holder.visit_desc.text = description?.let { HtmlCompat.fromHtml(it,HtmlCompat.FROM_HTML_MODE_LEGACY) }
                    Glide.with(context)
                        .load(logo)
                        .placeholder(R.drawable.loading)
                        .into(holder.visitLogo)
                }
                else -> {}
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}