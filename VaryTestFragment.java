package zj.health.fjzl.hnrm.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import zj.health.fjzl.hnrm.R;
import zj.health.fjzl.hnrm.ui.VaryModel;
import zj.health.fjzl.hnrm.ui.activity.VaryTestActivity;
import zj.health.fjzl.pt.global.base.MyBaseFragment;

/**
 * Created by hkq325800 on 2017/4/19.
 */

public class VaryTestFragment extends MyBaseFragment {
    @BindView(R.id.mVaryIdTxt)
    TextView mVaryIdTxt;
    @BindView(R.id.mVaryPageTxt)
    TextView mVaryPageTxt;
    @BindView(R.id.mVaryDetailTxt)
    TextView mVaryDetailTxt;
    @BindView(R.id.mVaryUserImg)
    ImageView mVaryUserImg;

    VaryModel model;

    public static VaryTestFragment getInstance(Bundle bundle) {
        VaryTestFragment fragment = new VaryTestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_vary;
    }

    @Override
    protected void initView(View rootView, ViewGroup container) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        model = (VaryModel) getArguments().getSerializable(VaryTestActivity.EXTRA_MODEL);
        if (model != null) {
            mVaryIdTxt.setText(model.getId() + "");//需要通过id获取对应的评论列表
            mVaryPageTxt.setText(model.getPage() + "");
            mVaryDetailTxt.setText(model.getDetail());
            Glide.with(this).load(model.getUser_icon()).centerCrop().into(mVaryUserImg);
        }
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }
}
