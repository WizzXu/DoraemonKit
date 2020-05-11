package com.didichuxing.doraemonkit.kit.network;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.DokitConstant;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.network.mock.view.MockConfigActivity;


/**
 * @author jintai
 * @desc: 网络监测kit
 */
public class MyMockKit extends AbstractKit {
    private String mockUrlStr = "";
    private String tokenStr = "";
    private String projectId = "";


    @Override
    public int getCategory() {
        return Category.PLATFORM;
    }

    @Override
    public int getName() {
        return R.string.dk_kit_network_mock;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_net_mock;
    }


    @Override
    public void onClick(Context context) {
        Intent intent = new Intent(context, MockConfigActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mockUrlStr", mockUrlStr);
        intent.putExtra("tokenStr", tokenStr);
        intent.putExtra("projectId", projectId);
        context.startActivity(intent);
    }

    public MyMockKit() {
    }

    public MyMockKit(String mockUrlStr, String tokenStr, String projectId) {
        this.mockUrlStr = mockUrlStr;
        this.tokenStr = tokenStr;
        this.projectId = projectId;
    }

    @Override
    public void onAppInit(Context context) {

    }

    @Override
    public boolean isInnerKit() {
        return true;
    }

    @Override
    public String innerKitId() {
        return "dokit_sdk_platform_ck_mock";
    }
}
