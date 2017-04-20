package cn.kerhcin.zhihuviewpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hkq325800 on 2017/4/19.
 */

public class VaryTestFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(provideContentViewId(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(savedInstanceState);
    }

    protected int provideContentViewId() {
        return R.layout.fragment_vary;
    }

    protected void initData(Bundle savedInstanceState) {
        model = (VaryModel) getArguments().getSerializable(VaryTestActivity.EXTRA_MODEL);
        String detail = "页面详情";
        if (model != null) {
            for (int i = 0; i < 40; i++) {
                detail += model.getDetail() + "\n";
            }
            mVaryIdTxt.setText("页面detailId" + model.getId());//需要通过id获取对应的评论列表
            mVaryPageTxt.setText("页数：" + model.getPage());
            mVaryDetailTxt.setText(detail);
            Glide.with(this).load(model.getUser_icon()).centerCrop().into(mVaryUserImg);
        }
    }
}
