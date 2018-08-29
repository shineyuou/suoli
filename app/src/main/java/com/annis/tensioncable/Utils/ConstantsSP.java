package com.annis.tensioncable.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class ConstantsSP {
    private static ConstantsSP instance;

    private static final String DEFAULT_NAME = "SP_FILE_NAME";

    private String name = DEFAULT_NAME;

    private static Context mContext;

    public static ConstantsSP getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (instance == null) {
            synchronized (ConstantsSP.class) {
                if (instance == null) {
                    instance = new ConstantsSP();
                }
            }
        }
        return instance;
    }

    /**
     * 保存数据,泛型方法
     *
     * @param key，键值
     * @param value，数据
     * @param <V>
     */
    public <V> void setValue(@NonNull String key, V value) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.commit();
    }

    /**
     * 读取数据,泛型方法
     *
     * @param key，键值
     * @param defaultValue，默认值
     * @param <V>
     * @return
     */

    public <V> V getValue(@NonNull String key, V defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        Object value = defaultValue;
        if (defaultValue instanceof String) {
            value = sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            value = sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            value = sp.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            value = sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            value = sp.getFloat(key, (Float) defaultValue);
        }
        return (V) value;
    }

    /**
     * 清除数据
     */
    public void clearData() {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

}
