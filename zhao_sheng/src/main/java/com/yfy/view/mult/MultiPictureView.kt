
package com.yfy.view.multi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.zhao_sheng.R
import com.yfy.final_tag.glide.GlideTools
import java.util.*

/**
 * Created by gaoyufei on 2017/6/22.
 * It not only can display many image
 * in single view, but also support edit.
 * you can custom the max image number,
 * span per line, dynamic or static mode
 *
 * editable 是否可以编辑 （加减图片）
 * addDrawable  加图片显示ziyuan
 * deleteDrawable remove icon 资源
 * ImageLayoutMode DYNAMIC 自动调整图片大小 STATIC 固定大小
 * ratio  横纵比
 * span  每行最多显示多少张
 */

class MultiPictureView : FrameLayout {
    interface ItemClickCallback {

        fun onItemClicked(view: View, index: Int, uris: ArrayList<String>)

    }

    interface AddClickCallback {

        fun onAddClick(view: View)

    }

    interface DeleteClickCallback {

        fun onDeleted(view: View, index: Int)

    }

    /**
     * 共两种布局方式
     */
    class ImageLayoutMode {
        companion object {
            @JvmStatic
            val DYNAMIC = 1
            @JvmStatic
            val STATIC = 2

        }
    }



    //child间隙
    var space = 1.toPx()

    // 每行最多显示多少张
    var span = 3

    // 布局方式，动态和固定
    var imageLayoutMode = ImageLayoutMode.STATIC

    // 最多显示图片个数
    var max = 9

    // 横纵比
    var ratio: Float = 1F


    // 删除图标
    var deleteDrawableId: Int = R.drawable.image_view_delete

    // 添加图标
    var addDrawableId: Int = R.drawable.image_view_add


    var itemClickCallback: ItemClickCallback? = null

    var deleteClickCallback: DeleteClickCallback? = object : DeleteClickCallback {
        override fun onDeleted(view: View, index: Int) {
            if (editable) {
                removeItem(index)
            }
        }

    }

    var addClickCallback: AddClickCallback? = null


    private val imageList = arrayListOf<String>()
    // 测量后实际要显示的行
    private var columnMeasure = 0
    private var rowMeasure = 0
    private var imageWidthMeasure = 0F
    private var imageHeightMeasure = 0F
    // 图片的Padding
    private var imagePaddingMeasure = 0

    // 是否可编辑
    private var editable: Boolean = false;


    private var deleteBitmap: Bitmap? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.MultiPictureView, defStyleAttr, 0)
        try {
            span = typeArray.getInteger(R.styleable.MultiPictureView_span, span)
            ratio = typeArray.getFloat(R.styleable.MultiPictureView_ratio, ratio)
            space = typeArray.getDimension(R.styleable.MultiPictureView_space, space.toFloat()).toInt()
            imageLayoutMode = typeArray.getInteger(R.styleable.MultiPictureView_imageLayoutMode, imageLayoutMode)
            max = typeArray.getInteger(R.styleable.MultiPictureView_max, max)
            editable = typeArray.getBoolean(R.styleable.MultiPictureView_editable, editable)
            deleteDrawableId = typeArray.getResourceId(R.styleable.MultiPictureView_deleteDrawable, deleteDrawableId)
            addDrawableId = typeArray.getResourceId(R.styleable.MultiPictureView_addDrawable, addDrawableId)
        } finally {
            typeArray.recycle();
        }

        setDeleteResource(deleteDrawableId)
        refresh()
    }

    /**
     * 将所有的图片添加到FrameLayout中
     */
    fun setupView() {
        removeAllViews()

        for (i in 0 until getNeedViewCount()) {
            val image = generateImage(i)
            addView(image)
            image.setOnClickListener {
                val arrayList = arrayListOf<String>()
                arrayList.addAll(imageList)
                itemClickCallback?.onItemClicked(it, i, arrayList)
            }
        }
        if (shouldDrawAddView()) {
            val image = generateImage(-1)
            image.setImageResource(addDrawableId)
            addView(image)
            image.setOnClickListener { addClickCallback?.onAddClick(it) }
        }
    }

    fun generateImage(index: Int): ImageView {
        val bitmap = if (editable) deleteBitmap else null
        val image = CustomImageView(context, index, bitmap, deleteClickCallback)
        image.scaleType = ImageView.ScaleType.FIT_XY
        if (editable) {
            image.setPadding(imagePaddingMeasure, imagePaddingMeasure, imagePaddingMeasure, imagePaddingMeasure)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                image.cropToPadding = true
            }
        }
        return image
    }

    fun measureImageSize(width: Int) {
        var imageSize = 0
        when (imageLayoutMode) {
            ImageLayoutMode.STATIC -> {
                columnMeasure = span
            }
            else -> {
                // 取一个合理的column
                columnMeasure = Math.min(Math.ceil(Math.sqrt(childCount.toDouble())).toInt(), span)
            }
        }
        rowMeasure = childCount / columnMeasure + if (childCount % columnMeasure == 0) 0 else 1
        imageSize = (width - space * (columnMeasure - 1)) / columnMeasure
        imageWidthMeasure = imageSize.toFloat()
        imageHeightMeasure = imageWidthMeasure / ratio
    }

    private fun getNeedViewCount() = Math.min(getCount(), max)

    private fun shouldDrawAddView() = editable && getCount() < max


    fun setDeleteResource(id: Int) {
        this.deleteDrawableId = id
        this.deleteBitmap = drawableToBitmap(resources.getDrawable(id))
        if (deleteBitmap != null) {
            imagePaddingMeasure = deleteBitmap!!.width / 2
        }
    }

    fun setEditable(editable: Boolean) {
        this.editable = editable
        setupView()
        requestLayout()
    }

    fun isEditable(): Boolean = editable

    fun getList(): ArrayList<String> {
        return imageList
    }

    fun refresh() {
        setupView()
        requestLayout()
    }

    fun setList(list: List<String>) {
        imageList.clear()
        imageList.addAll(list)
        refresh()
    }

    fun getCount(): Int {
        return imageList.size
    }

    fun addItem(uri: String) {
        addItem(uri, true)
    }

    fun addItem(uri: String, refresh: Boolean) {
        imageList.add(uri)
        if (refresh)
            refresh()
    }

    fun addItem(uri: List<String>) {
        imageList.addAll(uri)
        refresh()
    }

    fun clearItem() {
        imageList.clear()
        refresh()
    }

    fun removeItem(index: Int, refresh: Boolean) {
        imageList.removeAt(index)
        if (refresh)
            refresh()
    }

    fun removeItem(index: Int) {
        removeItem(index, true)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isInEditMode) {
            setBackgroundColor(Color.rgb(170,170,170))
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 60.toPx())
            return
        }
        if (childCount == 0) {
            setMeasuredDimension(0, 0)
            return
        }
        measureImageSize(MeasureSpec.getSize(widthMeasureSpec))
        val width = imageWidthMeasure * columnMeasure + (columnMeasure - 1) * space
        val height = imageHeightMeasure * rowMeasure + (rowMeasure - 1) * space
        setMeasuredDimension(width.toInt(), height.toInt())

        for (i in 0 until childCount) {
            val child = getChildAt(i) as ImageView
            if (child.visibility == GONE)
                return
            val measureWidth = MeasureSpec.makeMeasureSpec(imageWidthMeasure.toInt(), MeasureSpec.EXACTLY)
            val measureHeight = MeasureSpec.makeMeasureSpec(imageHeightMeasure.toInt(), MeasureSpec.EXACTLY)
            child.measure(measureWidth, measureHeight)
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == GONE)
                return

            val horizontalIndex = i % columnMeasure
            val verticalIndex = i / columnMeasure
            // 左边距
            val l = horizontalIndex * imageWidthMeasure + horizontalIndex * space
            // 上边距
            val t = verticalIndex * imageHeightMeasure + verticalIndex * space

            // 右边
            val r = l + imageWidthMeasure
            // 下边
            val b = t + imageHeightMeasure

            child.layout(l.toInt(), t.toInt(), r.toInt(), b.toInt())//设置每个child的占位
//            child.layout(5, t.toInt(), r.toInt(), b.toInt())错

            if (i < imageList.size) {
                bindImage(child as ImageView, imageList[i])
            }
        }
    }


    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)

        drawable.draw(canvas)

        return bitmap
    }

    /**
     * 将DP转换为PX
     */
    fun Int.toPx(): Int {
        return this.toFloat().toPx()
    }

    fun Float.toPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    private fun bindImage(imageView: ImageView, uri: String) {
        GlideTools.chanMult(context,uri,imageView,R.drawable.icon_gra_error);
    }
}
