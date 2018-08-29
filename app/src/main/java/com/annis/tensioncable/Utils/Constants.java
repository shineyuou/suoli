package com.annis.tensioncable.Utils;

import android.content.Context;
import android.text.TextUtils;

import com.annis.tensioncable.model.Equipment;
import com.annis.tensioncable.model.TensionCable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    /**
     * 全局对象
     */
    public static class Object {
        //TODO 去掉 class Object, 替换动态数据
        static List<Equipment> equipments;

        public static List<Equipment> getEquipments() {
            if (equipments == null) {
                equipments = new ArrayList<>();
                equipments.add(new Equipment("设备1", 1));
                equipments.add(new Equipment("设备2", 2));
                equipments.add(new Equipment("设备3", 0));
            }
            return equipments;
        }

        static List<TensionCable> tensionCables;

        public static List<TensionCable> getTensionCables(Context context) {
            String json = ConstantsSP.getInstance(context).getValue(TensionCables, "");
            if (!TextUtils.isEmpty(json)) {
                tensionCables = new Gson().fromJson(json, new TypeToken<List<TensionCable>>() {
                }.getType());
            }

            if (tensionCables == null) {
                tensionCables = new ArrayList<>();
                tensionCables.add(new TensionCable("拉索1", 1000.5, 50.51, 11.1));
                tensionCables.add(new TensionCable("拉索2", 1000.5, 50.51, 11.1));
                tensionCables.add(new TensionCable("拉索3", 1000.5, 50.51, 11.1));
                tensionCables.add(new TensionCable("拉索4", 1000.5, 50.51, 11.1));
                saveTensionCables(context, tensionCables);
            }
            return tensionCables;
        }

        public static void saveTensionCables(Context context, List<TensionCable> list) {
            if (list != null && list.size() > 0) {
                ConstantsSP.getInstance(context).setValue(TensionCables, new Gson().toJson(list));
            } else {
                ConstantsSP.getInstance(context).setValue(TensionCables, "");
            }
        }

        public static void addTensionCables(Context context, TensionCable item) {
            if (tensionCables == null) {
                tensionCables = new ArrayList<>();
            }
            tensionCables.add(item);
            saveTensionCables(context, tensionCables);
        }

        public static final String TensionCables = "TensionCables";
    }

    public static final class SP {
        /***  测量时间  ***/
        public static final String MeasureTime = "MeasureTime";
        /***  测量频率  ***/
        public static final String MeasureFrequency = "MeasureFrequency";
    }
}
