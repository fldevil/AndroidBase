package com.user.project.threelibrary;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Fan on 2017/6/22.
 * recycler view 适配器封装
 */

public class AdapterUtil {

    public static void initNormal(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                                  BaseQuickAdapter adapter) {
        init(recyclerView, layoutManager, adapter, null, null, null, null, null);
    }

    public static void initNormalMore(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                                      BaseQuickAdapter adapter, BaseQuickAdapter.RequestLoadMoreListener moreListener) {
        init(recyclerView, layoutManager, adapter, null, null, null, null, moreListener);
    }

    public static void initClick(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                                 BaseQuickAdapter adapter, RecyclerView.OnItemTouchListener listener) {
        init(recyclerView, layoutManager, adapter, null, null, null, listener, null);
    }

    public static void initHeaderMore(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                                      BaseQuickAdapter adapter, View headView, BaseQuickAdapter.RequestLoadMoreListener moreListener) {
        init(recyclerView, layoutManager, adapter, null, headView, null, null, moreListener);
    }

    public static void initEmptyMore(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                                     BaseQuickAdapter adapter, View emptyView, BaseQuickAdapter.RequestLoadMoreListener moreListener) {
        init(recyclerView, layoutManager, adapter, emptyView, null, null, null, moreListener);
    }

    public static void initEmptyMoreClick(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                                          BaseQuickAdapter adapter, View emptyView, RecyclerView.OnItemTouchListener listener,
                                          BaseQuickAdapter.RequestLoadMoreListener moreListener) {
        init(recyclerView, layoutManager, adapter, emptyView, null, null, listener, moreListener);
    }

    private static void init(final RecyclerView recyclerView,
                             RecyclerView.LayoutManager layoutManager,
                             final BaseQuickAdapter adapter,
                             View emptyView,
                             View headView,
                             View footView,
                             RecyclerView.OnItemTouchListener listener,
                             BaseQuickAdapter.RequestLoadMoreListener moreListener) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerView);
        if (emptyView != null) {
            adapter.setEmptyView(emptyView);
        }
        if (headView != null) {
            adapter.setHeaderView(headView);
            adapter.setHeaderFooterEmpty(true, true);
        }
        if (footView != null) {
            adapter.setFooterView(footView);
            adapter.setHeaderFooterEmpty(true, true);
        }
        if (listener != null) {
            recyclerView.addOnItemTouchListener(listener);
        }
        if (moreListener != null) {
            adapter.setOnLoadMoreListener(moreListener, recyclerView);
        }
    }

    public static <T, K extends BaseViewHolder> void headView(BaseQuickAdapter<T, K> adapter, View headView) {
        adapter.setHeaderView(headView);
        adapter.setHeaderFooterEmpty(true, true);
    }

    public static <T, K extends BaseViewHolder> void data(BaseQuickAdapter<T, K> adapter, List<T> list, boolean more) {
        if (more) {
            dataAdd(adapter, list);
        } else {
            dataNew(adapter, list);
        }
    }

    /**
     * 刷新数据
     */
    public static <T, K extends BaseViewHolder> void dataNew(BaseQuickAdapter<T, K> adapter, List<T> list) {
        if (list == null) return;
        adapter.setNewData(list);
        if (adapter.isLoadMoreEnable()) {
            adapter.disableLoadMoreIfNotFullPage();
        }
        adapter.loadMoreComplete();
    }

    /**
     * 更多数据
     */
    public static <T, K extends BaseViewHolder> void dataAdd(BaseQuickAdapter<T, K> adapter, List<T> list) {
        if (list == null) return;
        adapter.addData(list);
        adapter.loadMoreComplete();
    }

    /**
     * 在末尾添加一条数据
     */
    public static <T, K extends BaseViewHolder> void dataAdd(BaseQuickAdapter<T, K> adapter, T t) {
        adapter.addData(t);
    }
}
