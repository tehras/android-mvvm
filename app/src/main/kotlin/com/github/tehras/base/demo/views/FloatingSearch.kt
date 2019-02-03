package com.github.tehras.base.demo.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.github.tehras.base.demo.R
import com.github.tehras.base.ext.views.hideKeyboard
import com.github.tehras.base.ext.views.showKeyboard
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.view_search_layout.view.*
import java.util.concurrent.TimeUnit

class FloatingSearch @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val rxDisposables = CompositeDisposable()
    private val focusDisposable = CompositeDisposable()
    private val searchRelay = PublishRelay.create<CharSequence>()
    private val exitRelay = PublishRelay.create<Unit>()

    init {
        inflate(context, R.layout.view_search_layout, this)
    }

    fun show() {
        if (visibility == View.VISIBLE) return

        visibility = View.VISIBLE
        resetText()

        startListeningToChanges()

        alpha = 0.5f
        translationY = 100f
        animate()
            .alpha(1f)
            .translationY(0f)
            .start()

        focus(true)
    }

    fun hide() {
        if (visibility == View.GONE) return

        rxDisposables.clear()
        searchRelay.accept("")
        focus(false)

        visibility = View.GONE
    }

    fun textChanges(): Observable<CharSequence> = searchRelay
    fun stopSearch(): Observable<Unit> = exitRelay

    private fun resetText() {
        search_layout_input_text.setText("")
    }

    private fun focus(shouldFocus: Boolean) {
        val delay =
            if (shouldFocus) context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong() else 0L

        focusDisposable += Single
            .fromCallable { search_layout_input_text }
            .delay(
                delay,
                TimeUnit.MILLISECONDS
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                if (shouldFocus) {
                    it?.requestFocus()
                    it?.showKeyboard()
                } else {
                    it?.clearFocus()
                    it?.hideKeyboard()
                }
            }
    }

    private fun startListeningToChanges() {
        rxDisposables += search_layout_input_text
            .textChanges()
            .filter { visibility == View.VISIBLE }
            .subscribe(searchRelay)

        rxDisposables += search_layout_clear
            .clicks()
            .subscribe(exitRelay)
    }
}