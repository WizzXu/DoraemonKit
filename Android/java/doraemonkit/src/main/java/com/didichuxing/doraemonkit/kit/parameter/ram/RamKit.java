package com.didichuxing.doraemonkit.kit.parameter.ram;

import android.content.Context;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;
import com.google.auto.service.AutoService;

@AutoService(AbstractKit.class)
public class RamKit extends AbstractKit {


    @Override
    public int getName() {
        return R.string.dk_frameinfo_ram;
    }

    @Override
    public int getIcon() {
        return R.mipmap.dk_ram;
    }

    @Override
    public void onClick(Context context) {
        startUniversalActivity(RamMainPageFragment.class, context, null,true);
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
        return "dokit_sdk_performance_ck_arm";
    }
}
