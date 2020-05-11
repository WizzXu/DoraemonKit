package com.didichuxing.doraemonkit.kit.network.mock.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.network.mock.ApiInterfaceListResponse;
import com.didichuxing.doraemonkit.kit.network.mock.MockDataCallback;
import com.didichuxing.doraemonkit.kit.network.mock.MockUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockConfigActivity extends AppCompatActivity {

    private static final String SP_API_LIST = "sp_api_list";
    private static final String SP_TYPE_LIST = "sp_type_list";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private TextView open;
    private Switch openSwitch;
    private TextView refresh;
    private RecyclerView mRecyclerView;

    private Adapter adapter;
    private Spinner typeListView;
    private Spinner typeListOpenView;


    private ArrayAdapter typeListAdapter;
    private ArrayList<ApiInterfaceListResponse.DataBean.ListBean> apiList = new ArrayList<>();
    private String mockUrlStr = "";
    private String tokenStr = "";
    private String projectId = "";

    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dk_activity_mock_config);
        mockUrlStr = getIntent().getStringExtra("mockUrlStr");
        tokenStr = getIntent().getStringExtra("tokenStr");
        projectId = getIntent().getStringExtra("projectId");

        preferences = getSharedPreferences("mock_config", Context.MODE_PRIVATE);
        editor = preferences.edit();
        initView();
        initData(false);
    }

    private void initData(boolean isReFresh) {
        MockUtil.getInstance().init(mockUrlStr,
                tokenStr, "/mock/" + projectId, openSwitch.isChecked());
        if (!isReFresh) {
            //如果不是点击的刷新按钮
            //首先从持久化数据中读取数据并初始化
            getData();
            if (!MockUtil.getInstance().interfaceInfoConcurrentHashMap.isEmpty()) {
                Iterator<Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean>> it = MockUtil.getInstance().interfaceInfoConcurrentHashMap.entrySet().iterator();
                apiList.clear();
                while (it.hasNext()) {
                    Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean> bean = it.next();
                    apiList.add(bean.getValue());
                }
                updateView();
                return;
            }
        }

        MockUtil.getInstance().init(mockUrlStr,
                tokenStr, "/mock/" + projectId, openSwitch.isChecked(),
                new MockDataCallback() {
                    @Override
                    public void call(String data) {
                        Log.e("-->", data);
                        try {
                            new JSONObject(data);
                            setData();
                            showToast("更新数据成功");
                            saveData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("更新数据失败");
                        }
                    }
                }
        );
    }

    private void saveData() {
        editor.putString(SP_API_LIST, gson.toJson(MockUtil.getInstance().interfaceInfoConcurrentHashMap));
        editor.putString(SP_TYPE_LIST, gson.toJson(MockUtil.getInstance().apiTypeList));
        editor.apply();
    }

    private void getData() {
        String apiListSp = preferences.getString(SP_API_LIST, "{}");
        String typeListSp = preferences.getString(SP_TYPE_LIST, "[]");
        Type type1 = new TypeToken<ConcurrentHashMap<String, ApiInterfaceListResponse.DataBean.ListBean>>() {
        }.getType();
        MockUtil.getInstance().interfaceInfoConcurrentHashMap = gson.fromJson(apiListSp, type1);
        Type type2 = new TypeToken<ArrayList<ApiInterfaceListResponse.DataBean>>() {
        }.getType();
        MockUtil.getInstance().apiTypeList = gson.fromJson(typeListSp, type2);
    }


    private void initView() {
        open = (TextView) findViewById(R.id.open);
        openSwitch = (Switch) findViewById(R.id.open_switch);
        refresh = (TextView) findViewById(R.id.refresh);

        openSwitch.setChecked(preferences.getBoolean("isOpen", false));
        MockUtil.getInstance().setOpen(openSwitch.isChecked());

        openSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("isOpen", openSwitch.isChecked());
                editor.apply();
                MockUtil.getInstance().setOpen(openSwitch.isChecked());
                showToast("设置成功");
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(true);
            }
        });

        adapter = new Adapter(this);
        adapter.setCallback(new MockDataCallback() {
            @Override
            public void call(String data) {
                //当有item点击的时候选择全部开关置空
                typeListOpenView.setSelection(0);
                saveData();
            }
        });
        mRecyclerView = findViewById(R.id.api_list_recyclerview);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置adapter
        mRecyclerView.setAdapter(adapter);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));

        typeListView = (Spinner) findViewById(R.id.type_list);
        //适配器
        typeListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        //设置样式
        typeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        typeListView.setAdapter(typeListAdapter);
        typeListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apiList.clear();
                Log.e("-->", String.valueOf(position));
                if (position == 0) {
                    Iterator<Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean>> it = MockUtil.getInstance().interfaceInfoConcurrentHashMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean> bean = it.next();
                        apiList.add(bean.getValue());
                    }
                } else {
                    Iterator<Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean>> it = MockUtil.getInstance().interfaceInfoConcurrentHashMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean> bean = it.next();
                        if (MockUtil.getInstance().apiTypeList.get(position).get_id() == bean.getValue().getCatid()) {
                            apiList.add(bean.getValue());
                        }
                    }
                }
                updateView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        typeListOpenView = (Spinner) findViewById(R.id.type_open);
        //适配器
        ArrayAdapter typeListOpenAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        //设置样式
        typeListOpenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        typeListOpenView.setAdapter(typeListOpenAdapter);
        typeListOpenAdapter.addAll(Arrays.asList("", "全部打开", "全部关闭"));
        typeListOpenView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String state = "undone";

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                } else if (position == 1) {
                    state = "undone";
                } else if (position == 2) {
                    state = "done";
                }
                apiList.clear();
                Iterator<Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean>> it = MockUtil.getInstance().interfaceInfoConcurrentHashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean> bean = it.next();
                    bean.getValue().setStatus(state);
                    apiList.add(bean.getValue());
                }
                typeListView.setSelection(0);
                updateView();
                saveData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void setData() {
        Iterator<Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean>> it = MockUtil.getInstance().interfaceInfoConcurrentHashMap.entrySet().iterator();
        apiList.clear();
        while (it.hasNext()) {
            Map.Entry<String, ApiInterfaceListResponse.DataBean.ListBean> bean = it.next();
            apiList.add(bean.getValue());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                typeListView.setSelection(0);
                typeListOpenView.setSelection(0);
                updateView();
            }
        });
    }

    void updateView() {
        if (MockUtil.getInstance().apiTypeList.isEmpty() || apiList.isEmpty()) {
            typeListView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
        typeListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        ArrayList<String> arrayList = new ArrayList<>();
        for (ApiInterfaceListResponse.DataBean b : MockUtil.getInstance().apiTypeList) {
            arrayList.add(b.getName());
        }
        typeListAdapter.clear();
        typeListAdapter.addAll(arrayList);
        typeListAdapter.notifyDataSetChanged();
        adapter.addItems(apiList);
        adapter.notifyDataSetChanged();
    }

    void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MockConfigActivity.this, s,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
