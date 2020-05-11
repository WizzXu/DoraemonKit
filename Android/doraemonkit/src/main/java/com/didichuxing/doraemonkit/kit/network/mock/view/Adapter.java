package com.didichuxing.doraemonkit.kit.network.mock.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.network.mock.ApiInterfaceListResponse;
import com.didichuxing.doraemonkit.kit.network.mock.MockDataCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: xuweiyu
 * Date: 2020/3/23
 * Description:
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<ApiInterfaceListResponse.DataBean.ListBean> apiList = new ArrayList<>();
    private List<Switch> list_switch;

    private Context mContext;

    private TextView tv;
    private Switch aSwitch;

    public void setCallback(MockDataCallback callback) {
        this.callback = callback;
    }

    private MockDataCallback callback;

    public Adapter(Context context) {
        mContext = context;
        apiList = new ArrayList<>();
        list_switch = new ArrayList<>();
    }

    public void addItems(ArrayList<ApiInterfaceListResponse.DataBean.ListBean> list) {
        apiList.clear();
        apiList.addAll(list);
    }

    public Switch getaSwitch(int position) {
        return list_switch.get(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dk_item_mock_config, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv.setText(apiList.get(position).getPath());
        holder.aSwitch.setTag(position);
        holder.aSwitch.setChecked("undone".equals(apiList.get(position).getStatus()));
        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterfaceListResponse.DataBean.ListBean bean = apiList.get((int) holder.aSwitch.getTag());
                if (holder.aSwitch.isChecked()) {
                    bean.setStatus("undone");
                } else {
                    bean.setStatus("done");
                }
                if (callback != null) {
                    callback.call(null);
                }
                //MockUtil.getInstance().interfaceInfoConcurrentHashMap.put(bean.getPath(), bean);
            }
        });
        holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final EditText et = new EditText(mContext);
                et.setHint("请输入参数如：key1=1&key2=2");
                et.setTextColor(Color.BLACK);
                et.setText(apiList.get(position).getParams());
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                apiList.get(position).setParams(input);
                                if (callback != null) {
                                    callback.call(null);
                                }
                            }
                        })
                        .setNegativeButton("取消", null).show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return apiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private Switch aSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            aSwitch = (Switch) itemView.findViewById(R.id.aSwitch);
        }
    }

}


