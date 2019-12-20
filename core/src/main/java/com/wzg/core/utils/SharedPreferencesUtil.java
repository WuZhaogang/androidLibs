package com.wzg.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import com.wzg.core.Constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import io.reactivex.annotations.Nullable;

/**
 * SharedPrefereces保存数据，工具类
 *
 * @author kTian
 */
public class SharedPreferencesUtil {
    private static Context mContext;
    private final static String sharedName = Constant.SP_NAME;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 得到SharedPreferences对象
     *
     * @return 返回一个SharedPreferences对象
     */
    private static SharedPreferences newSharedPreferences() {
        return mContext.getSharedPreferences(sharedName, mContext.MODE_PRIVATE);
    }

    /**
     * 移除SharedPrefereces中的缓存数据
     *
     * @param key
     */
    public static void removeDataSharedPreferences(String key) {
        newSharedPreferences().edit().remove(key);
    }

    /**
     * 保存String类型的数据
     *
     * @param key
     * @param value 键对应的值
     */
    public static void putString(String key, String value) {
        Editor mEditor = newSharedPreferences().edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * 获取保存的String类型的数据
     *
     * @param key
     * @param defaultString 找不到值的时候的默认值
     * @return String
     */
    public static String getStringWithDefault(String key, String defaultString) {
        return newSharedPreferences().getString(key, defaultString);
    }

    /**
     * 获取保存的String类型的数据
     *
     * @param key
     * @return String
     */
    public static String getString(String key) {
        return newSharedPreferences().getString(key, "");
    }

    /**
     * 保存integer类型的数据
     *
     * @param key
     * @param value 键对应的值
     */
    public static void putInt(String key, int value) {
        Editor mEditor = newSharedPreferences().edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * 获取保存的integer类型的数据
     *
     * @param key
     * @return integer
     */

    public static int getInt(String key, int defaultValue) {
        return newSharedPreferences().getInt(key, defaultValue);
    }

    /**
     * 保存boolean类型的数据
     *
     * @param sharedName 保存SharedPreferences的名字，不可以为null
     * @param key
     * @param value      键对应的值
     */
    public static void putBoolean(String sharedName, String key, boolean value) {
        Editor mEditor = newSharedPreferences().edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * 获取保存的boolean类型的数据
     *
     * @param key
     * @return boolean
     */

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultVal) {
        return newSharedPreferences().getBoolean(key, defaultVal);
    }

    /**
     * 保存float类型的数据
     *
     * @param key
     * @param value      键对应的值
     */
    public static void putFloat(String key, float value) {
        Editor mEditor = newSharedPreferences().edit();
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    /**
     * 获取保存的float类型的数据
     *
     * @param key
     * @return float
     */

    public static float getFloat(String key) {
        return newSharedPreferences().getFloat(key, 0f);
    }

    /**
     * 保存long类型的数据
     *
     * @param key
     * @param value      键对应的值
     */
    public static void putLong(String key, long value) {
        Editor mEditor = newSharedPreferences().edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * 获取保存的long类型的数据
     *
     * @param sharedName 保存ShaedPreferences的名字，不可以为null
     * @param key
     * @return long
     */

    public static long getLong(String key) {
        return newSharedPreferences().getLong(key, 0);
    }

    /**
     * 将Object信息Base64后存入Preferences
     *
     * @param name
     * @param obj
     */

    public static <T> void putPreferences(String name, T obj) {
        SharedPreferences sp = mContext.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (obj == null) {
            editor.putString(name, null);
            editor.commit();
            return;
        }

        try {
            ByteArrayOutputStream toByte = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(toByte);
            oos.writeObject(obj);

            // 对byte[]进行Base64编码
            String obj64 = new String(Base64.encode(toByte.toByteArray(), Base64.DEFAULT));

            editor.putString(name, obj64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SharePreference中值，Base64解码之后传出
     *
     * @param name
     */
    @Nullable
    public static <T> T getPreferences(String name) {
        SharedPreferences sp = mContext.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        try {
            String obj64 = sp.getString(name, "");
            if (TextUtils.isEmpty(obj64)) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(obj64, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
