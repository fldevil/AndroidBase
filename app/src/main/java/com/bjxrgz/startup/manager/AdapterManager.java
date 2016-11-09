package com.bjxrgz.startup.manager;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016-11-9.
 * describe BaseRecyclerViewAdapterHelper管理类
 */

public class AdapterManager {

    public interface MoreListener {
        void more(int itemCount);
    }

    public interface RefreshListener {
        void refresh();
    }

    public static void refresh(final SwipeRefreshLayout srl, final RefreshListener listener) {
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true); // 执行等待动画
                if (listener != null) {
                    listener.refresh();
                }
            }
        });
    }

    public static <T> void setData(BaseQuickAdapter<T> adapter, List<T> list) {
        setData(adapter, null, list, false);
    }

    public static <T> void setData(BaseQuickAdapter<T> adapter, List<T> list, boolean more) {
        setData(adapter, null, list, more);
    }

    public static <T> void setData(BaseQuickAdapter<T> adapter, final SwipeRefreshLayout srl,
                                   List<T> list, boolean more) {
        if (null == list || 0 == list.size()) { // 没有更多数据
            if (!more) {
                adapter.setNewData(new ArrayList<T>());
                if (srl != null) {
                    srl.setRefreshing(false);
                }
            }
            adapter.loadComplete();
        } else { // 有数据返回
            if (more) { // 加载更多
                adapter.addData(list);
            } else { // 初次加载
                adapter.setNewData(list);
                if (srl != null) {
                    srl.setRefreshing(false);
                }
            }
        }
    }

    public static void setRefreshListener(final SwipeRefreshLayout srl, final RefreshListener listener) {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.refresh();
                        }
                    }
                });
            }
        });
    }

    public static <T> void setMoreListener(final BaseQuickAdapter<T> adapter, final RecyclerView rv,
                                           final MoreListener listener) {
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rv.post(new Runnable() {
                    @Override
                    public void run() {
                        int itemCount = adapter.getItemCount();
                        if (listener != null) {
                            listener.more(itemCount);
                        }
                    }
                });
            }
        });
    }

    public static void setEmpty(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.setEmptyView(view);
        }
    }

    public static void addHeader(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.addHeaderView(view);
        }
    }

    public static void addFooter(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.addFooterView(view);
        }
    }

    public static void removeHeader(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.removeHeaderView(view);
        } else {
            adapter.removeAllHeaderView();
        }
    }

    public static void removeFooter(BaseQuickAdapter adapter, View view) {
        if (null != view) {
            adapter.removeFooterView(view);
        } else {
            adapter.removeAllFooterView();
        }
    }

}
