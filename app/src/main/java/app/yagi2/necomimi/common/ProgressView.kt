package app.yagi2.necomimi.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import app.yagi2.necomimi.databinding.ViewProgressBinding

class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        ViewProgressBinding.inflate(LayoutInflater.from(context), this, true)
    }
}