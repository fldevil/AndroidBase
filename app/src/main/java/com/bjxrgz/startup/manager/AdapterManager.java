package com.bjxrgz.startup.manager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016-11-9.
 * describe BaseRecyclerViewAdapterHelper管理类
 */
public class AdapterManager {

    public interface MoreListener {
        void onMore(int currentCount);
    }

    public interface RefreshListener {
        void onRefresh();
    }

    /* 刷新监听 回调时newData() 记得offset重置 */
    public static void setRefreshListener(final SwipeRefreshLayout srl,
                                          final RefreshListener listener) {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onRefresh();
                        }
                    }
                });
            }
        });
    }

    /* 加载更多监听 回调时addData() 记得offset叠加 */
    public static <T> void setMoreListener(final BaseQuickAdapter<T> adapter, final RecyclerView rv,
                                           final MoreListener listener) {
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rv.post(new Runnable() {
                    @Override
                    public void run() {
                        int currentCount = adapter.getItemCount();
                        if (listener != null) {
                            listener.onMore(currentCount);
                        }
                    }
                });
            }
        });
    }

    /* RecyclerView的item点击监听 */
    public static void setClickListener(RecyclerView rv, RecyclerView.OnItemTouchListener listener) {
        rv.addOnItemTouchListener(listener);
    }

    /* 刷新 页面进入时调用 记得offset重置 */
    public static void refresh(final SwipeRefreshLayout srl, final RefreshListener listener) {
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true); // 执行等待动画
                if (listener != null) {
                    listener.onRefresh();
                }
            }
        });
    }

    /* 刷新数据 */
    public static <T> void newData(BaseQuickAdapter<T> adapter, List<T> list) {
        newData(adapter, null, list);
    }

    /* 刷新数据 */
    public static <T> void newData(BaseQuickAdapter<T> adapter,
                                   SwipeRefreshLayout srl, List<T> list) {
        newData(adapter, srl, list, -1);
    }

    /* 刷新数据 */
    public static <T> void newData(BaseQuickAdapter<T> adapter, List<T> list, int totalCount) {
        newData(adapter, null, list, totalCount);
    }

    /* 刷新数据 */
    public static <T> void newData(BaseQuickAdapter<T> adapter, SwipeRefreshLayout srl,
                                   List<T> list, int totalCount) {
        if (null == list || 0 == list.size()) { // 没有数据
            adapter.setNewData(new ArrayList<T>());
            adapter.loadComplete(); // 关闭更多
        } else { // 有数据
            adapter.setNewData(list);
            if (totalCount != -1 && list.size() >= totalCount) {
                adapter.loadComplete(); // 关闭更多
            }
        }
        if (null != srl) { // 停止刷新
            srl.setRefreshing(false);
        }
    }

    /* 更多数据 */
    public static <T> void addData(BaseQuickAdapter<T> adapter, List<T> list) {
        addData(adapter, list, -1);
    }

    /* 更多数据 */
    public static <T> void addData(BaseQuickAdapter<T> adapter, List<T> list, int totalCount) {
        if (null == list || 0 == list.size()) { // 没有数据
            adapter.loadComplete();
        } else { // 有数据
            adapter.addData(list);
            if (totalCount != -1 && adapter.getItemCount() >= totalCount) {
                adapter.loadComplete(); // 关闭更多
            }
        }
    }

    /* 无Data时显示的view */
    public static void setEmpty(BaseQuickAdapter adapter, Context context, int layoutId, ViewGroup root) {
        if (layoutId != 0) {
            View emptyView = LayoutInflater.from(context).inflate(layoutId, root, false);
            setEmpty(adapter, emptyView);
        }
    }

    /* 无Data时显示的view */
    public static void setEmpty(BaseQuickAdapter adapter, View emptyView) {
        if (null != emptyView) {
            adapter.setEmptyView(emptyView);
        }
    }

    public static void addHeader(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.addHeaderView(view);
        }
    }

    public static void removeHeader(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.removeHeaderView(view);
        }
    }

    public static void removeHeader(BaseQuickAdapter adapter) {
        adapter.removeAllHeaderView();
    }

    public static void addFooter(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.addFooterView(view);
        }
    }

    public static void removeFooter(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.removeFooterView(view);
        }
    }

    public static void removeFooter(BaseQuickAdapter adapter) {
        adapter.removeAllFooterView();
    }

}
