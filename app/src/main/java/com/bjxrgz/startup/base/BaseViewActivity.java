package com.bjxrgz.startup.base;

/**
 * Created by Fan on 2016/7/16.
 * BaseViewActivity
 */
public abstract class BaseViewActivity<T> extends BaseActivity<T> {
//
//    @ViewInject(R.id.tvTopTitle)
//    private TextView tvTopTitle;
//    // left
//    @ViewInject(R.id.tvTopLeft)
//    protected TextView tvTopLeft;
//    @ViewInject(R.id.ibTopLeft)
//    protected ImageButton ibTopLeft;
//    // right
//    @ViewInject(R.id.tvTopRightShall)
//    protected TextView tvTopRightShall;
//    @ViewInject(R.id.tvTopRightDark)
//    protected TextView tvTopRightDark;
//    @ViewInject(R.id.ivAdd)
//    protected ImageView ivAdd;
//    @ViewInject(R.id.cbTopRight)
//    protected CheckBox cbTopRight;
//
//    @Event(value = {R.id.ibTopLeft, R.id.tvTopLeft}, type = View.OnClickListener.class)
//    private void onClick(View view) {
//        if (mFragmentManager.getBackStackEntryCount() > 0) { //fragment栈中有fragment时，回退fragment
//            mFragmentManager.popBackStack();
//        } else {
//            finish();
//        }
//    }
//
//    protected void initTop(String topTitle) {
//        tvTopTitle.setText(topTitle);
//    }
//
//    protected void initTopBack(String title) {
//        ibTopLeft.setVisibility(View.VISIBLE);
//        initTop(title);
//    }
//
//    protected void initTopBack(String title, String back) {
//        tvTopLeft.setVisibility(View.VISIBLE);
//        tvTopLeft.setText(back);
//        initTop(title);
//    }
//
//    protected void initTopRightShall(String title, String right) {
//        tvTopRightShall.setVisibility(View.VISIBLE);
//        tvTopRightShall.setText(right);
//        initTopBack(title);
//    }
//
//    protected void initTopRightShall(String left, String title, String right) {
//        tvTopRightShall.setVisibility(View.VISIBLE);
//        tvTopRightShall.setText(right);
//        initTopBack(title, left);
//    }
//
//    protected void initTopRightDark(String title, String right) {
//        tvTopRightDark.setVisibility(View.VISIBLE);
//        tvTopRightDark.setText(right);
//        initTopBack(title);
//    }
//
//    protected void initTopAdd(String title) {
//        ibTopLeft.setVisibility(View.VISIBLE);
//        ivAdd.setVisibility(View.VISIBLE);
//        initTop(title);
//    }
//
//    protected void initTopRight(String title, int rightRes) {
//        initTopAdd(title);
//        ivAdd.setImageResource(rightRes);
//    }
//
//    protected void initTopCheck(String title) {
//        ibTopLeft.setVisibility(View.VISIBLE);
//        cbTopRight.setVisibility(View.VISIBLE);
//        initTop(title);
//    }

}
