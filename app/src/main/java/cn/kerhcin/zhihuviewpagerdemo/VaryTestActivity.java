package cn.kerhcin.zhihuviewpagerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.badoo.mobile.util.WeakHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hkq325800 on 2017/4/19.
 */

public class VaryTestActivity extends FragmentActivity {
    private final static String TAG = VaryTestActivity.class.getSimpleName();
    protected MyFragmentPagerAdapter adapter;
    protected ViewPager mViewPager;
    protected int lastTabIndex;
    protected int nextTabIndex;
    protected int lastState;
    protected ArrayList<Fragment> fragments = new ArrayList<>();
    protected WeakHandler handler;

    private static final int pageSize = 10;//每一组数据的数量 自定义
    private static final int startLoadBefore = 0;//提前这个数值进行下一组的数据获取 最小为0 需小于pageSize 自定义
    private static final int postDelay = 300;//防止卡顿
    private static final int maxPage = 26;//最大页数 按实际值获取
    private static final int maxPagePos = maxPage - 1;//最大页数位置
    private int initPos = 8;//初始化时的position
    private int nowMaxPos;//当前的最大页数开始时为pageSize 应该与initPos也有关
    //    private int nowMinPos;//当前的最小页数开始时为pageSize 应该与initPos也有关
    int page = 0;
    int zan = 40;
    int comment = 80;
    int id = 100;
    @BindView(R.id.mVaryZanTxt)
    TextView mVaryZanTxt;
    @BindView(R.id.mVaryCommentTxt)
    TextView mVaryCommentTxt;
    List<VaryModel> list = new ArrayList<>();
    String url1 = "http://i4.buimg.com/567571/4f761b83cdb56f54.jpg";
    String url2 = "http://i1.piimg.com/567571/427807e82e03b041.jpg";

    public static final String EXTRA_MODEL = "model";
//    public static final String EXTRA_AMOUNT_ZAN = "amount_zan";
//    public static final String EXTRA_AMOUNT_COMMENT = "amount_comment";
//    public static final String EXTRA_TOPIC = "topic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSthBeforeSetView(savedInstanceState);
        setContentView(provideContentViewId());
        ButterKnife.bind(this);
        initFragments(savedInstanceState);
        initView(savedInstanceState);
        initData(savedInstanceState);
        initEvent(savedInstanceState);
    }

    private void initEvent(Bundle savedInstanceState) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e(TAG, "pos-" + position + "positionOffset-" + positionOffset + "positionOffsetPixels-" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                lastTabIndex = nextTabIndex;
                nextTabIndex = position;
                onViewPagerSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (nextTabIndex + 1 == adapter.getCount() && state == 0 && lastState == 1) {
                    onViewPagerScrolled(nextTabIndex);
                }
                lastState = state;
            }
        });
    }

    private void initView(Bundle savedInstanceState) {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mViewPager.setOffscreenPageLimit(fragments.size());
        adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
    }

    protected boolean initCallback(Message msg) {
        switch (msg.what) {
            case 0:
                Bundle b = (Bundle) msg.obj;
                fragments.add(msg.arg1, VaryTestFragment.getInstance(b));
                break;
            case 1:
                adapter.notifyDataSetChanged();
//                mViewPager.arrowScroll(FOCUS_RIGHT);//模拟手动滑动
                break;
        }
        return false;
    }

    protected void doSthBeforeSetView(Bundle savedInstanceState) {
        handler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return initCallback(msg);
            }
        });
        if (initPos < pageSize - 1) {
            nowMaxPos = maxPage < pageSize ? maxPagePos : pageSize - 1;
        } else if (initPos >= pageSize - 1) {//如果初始化位置比规定的每页页数还要大 则初始化的数据包含之前的所有数据
            nowMaxPos = initPos + pageSize - initPos % pageSize - 1;//最大页数位置
            //如果初始位置initPos等于当前最大页数位置nowMaxPos且加上pageSize小于maxPage-1则nowMaxPos多取一页
            //如果初始位置initPos等于当前最大页数位置nowMaxPos但是大于等于maxPage-1则nowMaxPos取最大页数的位置maxPage-1
            if (initPos == nowMaxPos && initPos + pageSize < maxPagePos) nowMaxPos += pageSize;
            else if (initPos == nowMaxPos) nowMaxPos = maxPagePos;
        }
    }

    protected int provideContentViewId() {
        return R.layout.activity_vary;
    }

    protected void initFragments(Bundle savedInstanceState) {
        for (int i = 0; i < nowMaxPos + 1; i++) {
            list.add(i, (new VaryModel(id, zan, comment, page, "detail" + id, id % 2 == 0 ? url1 : url2)));
            id++;
            zan++;
            comment++;
            page++;
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_MODEL, list.get(i));//传id用它获取详情
            fragments.add(i, VaryTestFragment.getInstance(bundle));
        }
    }

    protected void initData(Bundle savedInstanceState) {
        mVaryZanTxt.setText("页面赞数：" + list.get(initPos).getZan());
        mVaryCommentTxt.setText("/页面评论数：" + list.get(initPos).getComment());
        mViewPager.setCurrentItem(initPos);//模拟初次点击不是0
        nextTabIndex = initPos;
    }

    //这种方法在initPos%pageSize == 0时就失效了
    protected void onViewPagerSelected(int position) {
        mVaryZanTxt.setText("页面赞数：" + list.get(position).getZan());
        mVaryCommentTxt.setText("/页面评论数：" + list.get(position).getComment());
        //往下加载更多是这样的
        if (position == nowMaxPos - startLoadBefore && nowMaxPos < maxPagePos) {
            getNextData();
        }
    }

    protected void onViewPagerScrolled(int position) {
//        //往下加载更多是这样的
//        if (position == nowMaxPage - startLoadBefore - 1 && nowMaxPage < maxPage) {
//            getNextData();
//        }
    }

    public void getNextData() {
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                final int start = nowMaxPos;
                if (nowMaxPos + pageSize <= maxPagePos)
                    nowMaxPos += pageSize;
                else
                    nowMaxPos = maxPagePos;
                Log.e(TAG, "start to add " + start + "-" + nowMaxPos);
                Log.e(TAG, "nowMaxPage" + nowMaxPos);
                for (int i = start + 1; i < nowMaxPos + 1; i++) {
                    list.add(i, new VaryModel(id, zan, comment, page, "detail" + id, id % 2 == 0 ? url1 : url2));
                    id++;
                    zan++;
                    comment++;
                    page++;

                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_MODEL, list.get(i));//传id用它获取详情
                    msg.what = 0;
                    msg.arg1 = i;
                    msg.obj = bundle;
                    handler.sendMessage(msg);
                    if (i == nowMaxPos) {
                        Message msg1 = Message.obtain();
                        msg1.what = 1;
                        handler.sendMessage(msg1);
                    }
                }
            }
        }, postDelay);
    }
}
