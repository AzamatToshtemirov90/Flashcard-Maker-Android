package com.piapps.flashcardpro.features.study

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kent.layouts.*
import com.kent.layouts.viewgroup.frameLayout
import com.piapps.flashcardpro.R
import jp.wasabeef.recyclerview.animators.LandingAnimator

/**
 * Created by abduaziz on 2019-10-29 at 13:02.
 */

fun StudyFragment.UI(): View {
    return ctx.frameLayout {
        backgroundColorResource = theme.colorBackground

        ivClose = imageView {
            layoutParams = FrameLayout.LayoutParams(dip(56), dip(56))
            padding = dip(16)
            setImageResource(R.drawable.ic_arrow_back)
            setIconColor(ctx, theme.colorIconActive)
            setOnClickListener { close() }
        }

        rv = recyclerView {
            layoutParams = FrameLayout.LayoutParams(matchParent, matchParent).apply {
                topMargin = dip(56)
                bottomMargin = dip(56)
            }
            itemAnimator = LandingAnimator()
            this@UI.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = this@UI.layoutManager
            adapter = this@UI.adapter
            snapHelper.attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        showCurrentCardPosition()
                    }
                }
            })
        }

        ivPrevious = imageView {
            layoutParams = FrameLayout.LayoutParams(dip(56), dip(56)).apply {
                gravity = Gravity.BOTTOM or Gravity.START
            }
            padding = dip(16)
            setImageResource(R.drawable.ic_left)
            setIconColor(ctx, theme.colorIconActive)
            setRippleEffectBorderless()
        }

        tvLietnerScore = textView {
            layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent).apply {
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                margin = dip(16)
            }
            textSize = 10F
            textColorResource = theme.colorSecondaryText
        }

        ivNext = imageView {
            layoutParams = FrameLayout.LayoutParams(dip(56), dip(56)).apply {
                gravity = Gravity.BOTTOM or Gravity.END
            }
            padding = dip(16)
            setImageResource(R.drawable.ic_right)
            setIconColor(ctx, theme.colorIconActive)
            setRippleEffectBorderless()
        }

        ivShuffle = imageView {
            layoutParams = FrameLayout.LayoutParams(dip(56), dip(56)).apply {
                gravity = Gravity.END
            }
            padding = dip(16)
            setImageResource(R.drawable.ic_shuffle)
            setIconColor(ctx, theme.colorIconActive)
            setRippleEffectBorderless()
        }

        tvCurrentCard = textView {
            layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                margin = dip(16)
            }
            textSize = 12F
            textColorResource = theme.colorSecondaryText
        }
    }
}