/*
 * MIT License
 *
 * Copyright (c) 2021 Gourav Khunger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gouravkhunger.accolib.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import com.gouravkhunger.accolib.R

class Accordion(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    // variables
    private var isOpen = false
    private var isClicked = false

    // UI elements
    private val icon: ImageView
    private val titleTv: TextView
    private val textTv: TextView
    private val titleHolder: CardView
    private val textHolder: CardView

    // accordion properties
    private var _title = ""
    private var _text = ""
    private var _titleColor = 0XFF000000.toInt()
    private var _textColor = 0XFFaaaaaa.toInt()

    var title = _title
        get() {
            return _title
        }
        set(value) {
            field = value
            _title = value
            titleTv.text = value
        }

    var text = _text
        get() {
            return _text
        }
        set(value) {
            field = value
            _text = value
            textTv.text = value
        }

    // colors for title and text
    var titleColor = _titleColor
        get() {
            return _titleColor
        }
        set(@ColorInt colorInt) {
            field = colorInt
            _titleColor = colorInt
            titleTv.setTextColor(colorInt)
        }

    var textColor = _textColor
        get() {
            return _textColor
        }
        set(@ColorInt colorInt) {
            field = colorInt
            _textColor = colorInt
            textTv.setTextColor(colorInt)
        }

    // size of title and text
    private var titleSize = 10f
        set(value) {
            field = value
            invalidate()
        }

    private var textSize = 10f
        set(value) {
            field = value
            invalidate()
        }

    init {
        // get values from custom parameters passed in xml declaration of view
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Accordion, 0, 0)

        _title = typedArray.getString(R.styleable.Accordion_title) ?: _title
        _text = typedArray.getString(R.styleable.Accordion_text) ?: _text

        titleSize = typedArray.getDimension(R.styleable.Accordion_titleSize, titleSize)
        textSize = typedArray.getDimension(R.styleable.Accordion_textSize, textSize)

        _titleColor = typedArray.getColor(R.styleable.Accordion_titleColor, _titleColor)
        _textColor = typedArray.getColor(R.styleable.Accordion_textColor, _textColor)

        // recycle the values
        typedArray.recycle()

        // inflate the default accordion layout
        inflate(context, R.layout.layout_default, this)

        titleTv = findViewById(R.id.title)
        textTv = findViewById(R.id.text)
        icon = findViewById(R.id.arrow)

        icon.layoutParams.height = titleSize.toInt()
        icon.layoutParams.width = titleSize.toInt()
        icon.requestLayout()

        // set the styles of the values as passed
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
        textTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        titleTv.setTextColor(_titleColor)
        textTv.setTextColor(_textColor)

        titleTv.text = _title
        textTv.text = _text

        titleHolder = findViewById(R.id.titleHolder)
        textHolder = findViewById(R.id.textHolder)

        textHolder.visibility = View.GONE

        // animate the text when title is clicked
        titleHolder.setOnClickListener {
            handleAccordion()
        }
    }

    // open/close the accordion on title press
    private fun handleAccordion() {
        // don't do anything if it has already been clicked
        // -> prevents messing up with multiple clicks before
        // animation ends
        if (!isClicked) {
            isClicked = true

            textHolder.visibility = if (isOpen) View.GONE else View.VISIBLE
            turn(icon)

            isOpen = !isOpen
            isClicked = false
        }
    }

    // flips any view upside down/downside up
    private fun turn(view: View) {
        if (!isOpen) view.animate().rotation(180f).start()
        else view.animate().rotation(0f).start()
    }
}
