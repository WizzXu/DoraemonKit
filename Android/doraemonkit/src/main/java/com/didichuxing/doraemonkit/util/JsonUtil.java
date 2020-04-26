package com.didichuxing.doraemonkit.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtil {

    private static final String TAG = "JsonUtil";
    private static final Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        //Add strategy if needed.
        gson = builder.create();
    }

    public static String jsonFromObject(Object object) {
        if (object == null) {
            return "{}";
        }
        try {
            return gson.toJson(object);
        } catch (Throwable e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static <T> T objectFromJson(String json, Class<T> klass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (klass == null) {
            return null;
        }
        try {
            return gson.fromJson(json, klass);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> ArrayList<T> jsonToList(String json, Class<T> classOfT) {
        ArrayList<T> listOfT = new ArrayList<>();

        Type type;
        if (classOfT.isPrimitive()) {
            type = new TypeToken<ArrayList<JsonPrimitive>>() {
            }.getType();

            ArrayList<JsonPrimitive> jsonObjs = gson.fromJson(json, type);

            for (JsonPrimitive jsonObj : jsonObjs) {
                listOfT.add(gson.fromJson(jsonObj, classOfT));
            }
        } else {
            type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjs = gson.fromJson(json, type);

            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(gson.fromJson(jsonObj, classOfT));
            }
        }

        return listOfT;
    }

    /**
     * 判断一个json字符串是不是为空，可能是空字符串，或者空的json串
     *
     * @param json
     * @return
     */
    public static boolean isEmpty(String json) {
        if (TextUtils.isEmpty(json)) {
            return true;
        }
        if (TextUtils.equals(json, "{}")) {
            return true;
        }
        return false;
    }

    public static String jsonFormat(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();

        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == ':') {
                jsonFormat.append(c + " ");
            } else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                }
            } else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c);
                }
            } else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();

    }
}
