package zj.health.fjzl.hnrm.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zj.health.fjzl.hnrm.R;
import zj.health.fjzl.hnrm.ui.VaryModel;
import zj.health.fjzl.hnrm.ui.fragment.VaryTestFragment;
import zj.health.fjzl.pt.global.base.MyBaseFragmentActivityVP;
import zj.remote.baselibrary.util.Trace;

/**
 * Created by hkq325800 on 2017/4/19.
 */

public class VaryTestActivity extends MyBaseFragmentActivityVP {
    private static final int pageSize = 5;//每一组数据的数量
    private static final int startLoadBefore = 0;//提前这个数值进行下一组的数据获取 最小为0 需小于pageSize
    private static final int postDelay = 300;//防止卡顿
    private int maxPage = 50;//最大页数 按实际赋值获取
    private int initPos = 9;//初始化时的页数
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
    String url1 = "https://camo.githubusercontent.com/05fc1003f657c14d14a4d62568a296880f011fa6/687474703a2f2f7777332e73696e61696d672e636e2f6d773639302f37656630316663616777316632677a7a3035373062673230616e3035686d78762e676966";
    String url2 = "https://camo.githubusercontent.com/ba7de79e728e8e138438db219c544001e97b9e2f/687474703a2f2f7777312e73696e61696d672e636e2f6d773639302f37656630316663616777316632677a797838656762673230616e303568337a6c2e676966";

    public static final String EXTRA_MODEL = "model";
//    public static final String EXTRA_AMOUNT_ZAN = "amount_zan";
//    public static final String EXTRA_AMOUNT_COMMENT = "amount_comment";
//    public static final String EXTRA_TOPIC = "topic";

    @Override
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

    @Override
    protected void doSthBeforeSetView(Bundle savedInstanceState) {
        super.doSthBeforeSetView(savedInstanceState);
        if (initPos < pageSize) {
            nowMaxPos = maxPage < pageSize ? maxPage - 1 : pageSize - 1;
        } else if (initPos >= pageSize) {//如果初始化位置比规定的每页页数还要大 则初始化的数据包含之前的所有数据
            nowMaxPos = initPos + pageSize - initPos % pageSize - 1;//最大页数
            if (initPos == nowMaxPos && initPos + pageSize < maxPage) nowMaxPos += pageSize;
            else if (initPos == nowMaxPos) nowMaxPos = maxPage;
//            nowMinPos = nowMaxPos - pageSize + 1;
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_vary;
    }

    @Override
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

    @Override
    protected void initData(Bundle savedInstanceState) {
        mVaryZanTxt.setText(list.get(initPos).getZan() + "");
        mVaryCommentTxt.setText(list.get(initPos).getComment() + "");
        mViewPager.setCurrentItem(initPos);//模拟初次点击不是0
        nextTabIndex = initPos;
    }

    //这种方法在initPos%pageSize == 0时就失效了
    @Override
    protected void onViewPagerSelected(int position) {
        mVaryZanTxt.setText(list.get(position).getZan() + "");
        mVaryCommentTxt.setText(list.get(position).getComment() + "");
        //往下加载更多是这样的
        if (position == nowMaxPos - startLoadBefore && nowMaxPos < maxPage - 1) {
            getNextData();
        }
    }

    @Override
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
                Trace.e("start to add " + nowMaxPos + "-" + (nowMaxPos + pageSize));
                if (nowMaxPos + pageSize <= maxPage - 1)
                    nowMaxPos += pageSize;
                else
                    nowMaxPos = maxPage - 1;
                Trace.e("nowMaxPage" + nowMaxPos);
                for (int i = nowMaxPos - pageSize + 1; i < nowMaxPos + 1; i++) {
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
