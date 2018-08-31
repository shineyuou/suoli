package com.annis.tensioncable.FFT;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class ReadFiledata {
    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();

    }

    public static double[] txt2Doulbe(File file) {
        List list = new ArrayList();
        double[] data = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                list.add(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        data = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String s = (String) list.get(i);
            data[i] = Double.parseDouble(s);
        }
        return data;
    }
}
