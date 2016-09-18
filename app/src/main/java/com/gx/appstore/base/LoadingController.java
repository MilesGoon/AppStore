package com.gx.appstore.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.gx.appstore.R;
import com.gx.appstore.factory.ThreadFactory;
import com.gx.appstore.utils.UIUtils;

public abstract class LoadingController extends FrameLayout {

    public static final int STATE_NONE = -1;            // 默认状态
    public static final int STATE_LOADING = 0;          // 加载中
    public static final int STATE_ERROR = 1;            // 错误
    public static final int STATE_EMPTY = 2;            // 空
    public static final int STATE_SUCCESS = 3;          // 成功

    public int mCurState = STATE_NONE;

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    /**
     * 任何应用其实就只有4种页面类型
     * ① 加载页面
     * ② 错误页面
     * ③ 空页面
     * <p/>
     * ④ 成功页面
     * ①②③三种页面一个应用基本是固定的->常规的页面
     * <p/>
     * 然而每一个fragment/activity对应的页面④不一样
     * 进入应用的时候显示①,②③④需要加载数据之后才知道显示哪个
     */

    public LoadingController(Context context) {
        super(context);
        initCommonView();
    }

    /**
     * 初始化常规视图①加载页面 ②错误页面 ③空页面
     * LoadingPager初始化的时候被调用
     */
    private void initCommonView() {
        // ① 加载页面
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        this.addView(mLoadingView);

        // ② 错误页面
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        this.addView(mErrorView);

        // ③ 空页面
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        this.addView(mEmptyView);
        // 第一次
        refreshUIByState();
    }

    /**
     * 根据当前的状态显示不同的View
     * 1.LoadingPager初始化的时候被调用, 当前状态为STATE_NONE
     * 2.正在开始加载前, 重置当前状态为STATE_LOADING, 会刷新ui
     * 3.数据加载完成之后被调用根据结果返回不同状态
     */
    private void refreshUIByState() {
        // 控制加载页面的显示/隐藏
        mLoadingView.setVisibility((mCurState == STATE_LOADING) || (mCurState == STATE_NONE) ? View.VISIBLE : View.GONE);

        // 控制错误页面的显示/隐藏
        mErrorView.setVisibility((mCurState == STATE_ERROR) ? View.VISIBLE : View.GONE);

        // 控制空页面的显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        // 控制成功页面的初始化
        if (mSuccessView == null && mCurState == STATE_SUCCESS) {
            mSuccessView = initSuccessView();
            this.addView(mSuccessView);
        }

        if (mSuccessView != null) {
            // 控制成功页面的显示/隐藏
            mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * 数据加载的流程：
     * ① 触发加载  	ViewPager选中后触发加载
     * ② 异步加载数据  ->显示加载视图
     * ③ 处理加载结果
     * ① 成功-->显示成功视图
     * ② 失败
     *  ① 数据为空-->显示空视图
     *  ② 数据加载失败-->显示加载失败的视图
     */

    // 触发加载, 通过网络获取数据
    public void LoadDataTrigger() {
        if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADING) {
            // 异步加载数据 加载开始前,重置状态为LOADING
            mCurState = STATE_LOADING;
            // 第二次
            refreshUIByState();

            ThreadFactory.getNormalPool().execute(new LoadDataTask());
        }
    }

    class LoadDataTask implements Runnable {
        @Override
        public void run() {
            // 子线程中
            // 真正的开始加载数据
            // 加载数据数据之后-->返回一个临时状态
            // 临时状态==当前的状态
            mCurState = initData().getState();
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    // 再次刷新视图显示
                    // 第三次
                    refreshUIByState();
                }
            });
        }
    }


    /**
     * refreshUIByState()中智能更新, 让BaseFragment的子类自行表现成功视图
     */
    protected abstract View initSuccessView();

    /**
     * LoadDataTrigger方法被调用的时候调用, 让BaseFragment的子类自行加载数据
     */
    protected abstract LoadedResult initData();

    public enum LoadedResult {
        SUCCESS(STATE_SUCCESS), EMPTY(STATE_EMPTY), ERROR(STATE_ERROR);

        int mState;

        public int getState() {
            return mState;
        }

        private LoadedResult(int state) {
            mState = state;
        }
    }
}
