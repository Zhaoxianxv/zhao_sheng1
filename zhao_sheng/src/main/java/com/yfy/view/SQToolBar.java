package com.yfy.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Aj Liao on 2016/1/27.
 * <p/>
 * SinQin自己的ToolBar
 * 自定义TitleBar
 * <p/>
 * --------------------------------------
 * |Navi|       TITLE       |menu1|menu2|
 * --------------------------------------
 */
public class SQToolBar extends RelativeLayout {


    //图标菜单的图标显示大小
    private int MENU_ICON_SIZE = 0;
    //菜单项的宽高
    private int MENU_ITEM_SIZE = 0;
    //文字菜单的Padding
    private int TEXT_PADDING_LEFT_RIGHT = 0;

    //導航
    TextView tv_navi;
    ImageView iv_navi;
    //Title
    TextView tv_title;
    //存放所有的菜单
    List<View> menus;


    OnMenuClickListener onMenuClickListener = null;

    public SQToolBar(Context context) {
        this(context, null);
    }

    public SQToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SQToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        MENU_ICON_SIZE = (int) context.getResources().getDimension(R.dimen.margin_35dp);
        MENU_ITEM_SIZE = (int) context.getResources().getDimension(R.dimen.margin_45dp);
        TEXT_PADDING_LEFT_RIGHT = dip2px(14);

        menus = new ArrayList<>();
        //设置要给默认导航
        setNavi(R.drawable.app_selector_head_back);
    }
    /**
     * 设置文字导航（左上角文字）
     *
     * @param resid 文字资源ID
     */
    public TextView setNaviText(@StringRes int resid) {
        return setNaviText(getResources().getString(resid));
    }

    /**
     * 设置文字导航（左上角文字）
     *
     * @param text 文本
     */
    public TextView setNaviText(CharSequence text) {
        if (text == null || text.equals("")) {
            return null;
        }
        if (tv_navi != null) {
            tv_navi.setVisibility(VISIBLE);
            tv_navi.setText(text);
            return tv_navi;
        }
        tv_navi = new TextView(getContext());
        //布局
        tv_navi.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, MENU_ITEM_SIZE));
        //参数
        tv_navi.setPadding(TEXT_PADDING_LEFT_RIGHT, 0, TEXT_PADDING_LEFT_RIGHT, 0);
        tv_navi.setTextSize(16);//16sp
        tv_navi.setText(text);
        tv_navi.setTextColor(Color.WHITE);
        tv_navi.setGravity(Gravity.CENTER);
        tv_navi.setClickable(true);
        tv_navi.setBackgroundDrawable(createSelector());

        //设置默认点击事件
        setDefaultNaviClickListener();
        //添加
        addView(tv_navi);
        //将自己返回
        return tv_navi;
    }


    /**
     * 设置导航（左上角图标）
     *
     * @param resid 图片资源ID
     */
    public ImageView setNavi(@DrawableRes int resid) {
        return setNavi(getResources().getDrawable(resid));
    }

    /**
     * 设置导航（左上角图标）
     *
     * @param drawable 图片
     */
    public ImageView setNavi(Drawable drawable) {

        if (iv_navi != null) {
            iv_navi.setVisibility(VISIBLE);
            iv_navi.setImageDrawable(drawable);
            return iv_navi;
        }
        iv_navi = new ImageView(getContext());
        //布局
        iv_navi.setLayoutParams(new LayoutParams((int) (MENU_ITEM_SIZE * 1.2), MENU_ITEM_SIZE));
        //参数
        int p = (MENU_ITEM_SIZE - MENU_ICON_SIZE) / 2;
        iv_navi.setPadding(p, p, p, p);
        iv_navi.setImageDrawable(drawable);
        iv_navi.setClickable(true);
        iv_navi.setBackgroundDrawable(createSelector());

        //设置默认点击事件
        setDefaultNaviClickListener();

        addView(iv_navi);
        //将自己返回
        return iv_navi;
    }

    /**
     * 取消导航
     */
    public void cancelNavi() {
        if (iv_navi != null) {
            iv_navi.setVisibility(GONE);
        }
        if (tv_navi != null) {
            tv_navi.setVisibility(GONE);
        }
    }


    /**
     * 设置Title
     *
     * @param resid Title文本资源ID
     */
    public TextView setTitle(@StringRes int resid) {
        return setTitle(getResources().getString(resid));
    }

    /**
     * 设置Title
     *
     * @param text Title文本
     */
    public TextView setTitle(CharSequence text) {
        if (tv_title != null) {
            tv_title.setText(text);
            return tv_title;
        }
        tv_title = new TextView(getContext());
        //佈局
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv_title.setLayoutParams(layoutParams);
        //參數
        tv_title.setTextSize(20);//17sp
        tv_title.setText(text);
        tv_title.setTextColor(Color.WHITE);

        //添加
        addView(tv_title);

        //将自己返回
        return tv_title;
    }

    /**
     * 添加文本菜單
     *
     * @param position 菜单的位置
     * @param resid    文本内容
     */
    public TextView addMenuText(int position, @StringRes int resid) {
        return addMenuText(position, getResources().getString(resid));
    }

    /**
     * 添加文本菜單
     *
     * @param position 菜单的位置
     * @param text     文本内容
     */
    public TextView addMenuText(int position, String text) {

        TextView tv_menu = new TextView(getContext());
        tv_menu.setTag(position);
        //佈局
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, MENU_ITEM_SIZE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.rightMargin = calculateMarginRight();
        tv_menu.setLayoutParams(layoutParams);
        tv_menu.setPadding(TEXT_PADDING_LEFT_RIGHT, 0, TEXT_PADDING_LEFT_RIGHT, 0);
        tv_menu.setGravity(Gravity.CENTER);
        //參數;
        tv_menu.setTextSize(16);//16sp
        tv_menu.setText(text);
        tv_menu.setTextColor(Color.WHITE);
        tv_menu.setClickable(true);
//        tv_menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_icon_right_btn));
        //添加监听
        setMenuListener(tv_menu);
        //添加
        addView(tv_menu);
        menus.add(tv_menu);

        //将自己返回
        return tv_menu;
    }

    /**
     * 添加图片菜單
     *
     * @param position 菜单的位置
     * @param resid    图片ID
     */
    public ImageView addMenu(int position, @DrawableRes int resid) {
        return addMenu(position, getResources().getDrawable(resid));
    }
    public ImageView addMenu(int position, @DrawableRes int resid,int res_color) {
        Drawable drawable=getResources().getDrawable(resid);
        drawable.setTint(res_color);
        return addMenu(position, drawable);
    }

    /**
     * 添加图片菜單
     *
     * @param position 菜单的位置
     * @param drawable 图片内容
     */
    public ImageView addMenu(int position, Drawable drawable) {

        ImageView iv_menu = new ImageView(getContext());
        iv_menu.setTag(position);
        //佈局
        LayoutParams layoutParams = new LayoutParams((int) (MENU_ITEM_SIZE * 1.2), MENU_ITEM_SIZE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.rightMargin = calculateMarginRight();
        iv_menu.setLayoutParams(layoutParams);
        //参数
        int p = (MENU_ITEM_SIZE - MENU_ICON_SIZE) /2;
        iv_menu.setPadding((int) (p * 1), p, (int) (p * 1), p);

        iv_menu.setImageDrawable(drawable);
        iv_menu.setClickable(true);
        iv_menu.setBackgroundDrawable(createSelector());
        //添加监听
        setMenuListener(iv_menu);
        //添加
        addView(iv_menu);
        menus.add(iv_menu);

        //将自己返回
        return iv_menu;
    }

    private void setMenuListener(View menu) {
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMenuClickListener != null) {
                    onMenuClickListener.onClick(v, (int) v.getTag());
                }
            }
        });
    }

    /**
     * 计算菜单MarginRight的距离
     */
    private int calculateMarginRight() {

        int marginRight = 0;

        for (int i = 0; i < menus.size(); i++) {
            View view = menus.get(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                //计算TextView文字的宽度
                Rect bounds = new Rect();
                TextPaint paint = textView.getPaint();
                String text = textView.getText().toString();
                paint.getTextBounds(text, 0, text.length(), bounds);

                //计算TextView总宽度
                marginRight += bounds.width() + TEXT_PADDING_LEFT_RIGHT * 2;//字体宽度 + paddingLeft + paddingRight
            } else {//if (child instanceof ImageView)

                marginRight += MENU_ITEM_SIZE;
            }
        }
        return marginRight;
    }

    /**
     * 销毁时系统会调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        tv_navi = null;
        if (iv_navi != null) {
            iv_navi.setImageResource(0);
        }
        iv_navi = null;

        tv_title = null;

        menus.clear();
        onMenuClickListener = null;
    }

    /**
     * 为导航设置默认的点击事件
     * <p/>
     * 默认点击导航后，返回上一个页面
     */
    private void setDefaultNaviClickListener() {
        setOnNaviClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    /**
     * 设置左上角的导航点击事件
     *
     * @param onNaviClickListener
     */
    public void setOnNaviClickListener(OnClickListener onNaviClickListener) {
        if (iv_navi != null) {
            iv_navi.setOnClickListener(onNaviClickListener);
        }
        if (tv_navi != null) {
            tv_navi.setOnClickListener(onNaviClickListener);
        }
    }

    /**
     * 取得导航的对象
     *
     * @return
     */
    public TextView getNaviTextView() {
        return tv_navi;
    }

    /**
     * 取得导航的对象
     *
     * @return
     */
    public ImageView getNavi() {
        return iv_navi;
    }

    /**
     * 取得Title的对象
     *
     * @return
     */
    public TextView getTitleTextView() {
        return tv_title;
    }

    /**
     * 取得够一个按钮的View
     *
     * @param position Menu的唯一标识
     */
    public View getMenu(int position) {
        for (View menu : menus) {
            if ((int) menu.getTag() == position) {
                return menu;
            }
        }
        return null;
    }

    /**
     * 取得所有的菜单控件
     *
     * @return
     */
    public List<View> getMenus() {
        return menus;
    }

    public int px2dip(float pxValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue,
                getResources().getDisplayMetrics());
    }

    public int dip2px(float dipValue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (density * dipValue);
    }

    /**
     * 为每一个Menu设置背景色，背景是一个Selector
     * @return
     */
    private StateListDrawable createSelector() {
        StateListDrawable bg = new StateListDrawable();
        Resources resources = getContext().getResources();

        ColorDrawable pressed = new ColorDrawable(resources.getColor(R.color.app_base_color));
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        ColorDrawable normal = new ColorDrawable(resources.getColor(R.color.app_base_color));
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 为每一个Menu设置背景图，背景是一个Selector
     *
     * @return
     */
    private StateListDrawable createSelectorPic() {
        StateListDrawable bg = new StateListDrawable();
        Resources resources = getContext().getResources();
        Drawable pressed = resources.getDrawable(R.drawable.app_title_bg_shape);
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        Drawable normal = resources.getDrawable(R.drawable.app_title_bg_shape);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 按钮的点击事件
     */
    public interface OnMenuClickListener {
        void onClick(View view, int position);
    }

    public OnMenuClickListener getOnMenuClickListener() {
        return onMenuClickListener;
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }
}
