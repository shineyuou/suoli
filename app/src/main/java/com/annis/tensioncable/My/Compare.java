package com.annis.tensioncable.My;

public class Compare {
    /**
     * @param list   需要比较的数组
     * @param length 数组长度
     * @return 最大值
     */
    public static double max(double list[], int length) {
        int i;
        double max;
        max = list[0];
        for (i = 1; i < length; i++) {
            if (list[i] > max) {
                max = list[i];
            }

        }
        return max;
    }

    /**
     * @param list   需要比较的数组
     * @param length 数组长度
     * @return 最大值
     */
    public static int max(int list[], int length) {
        int i;
        int max;
        max = list[0];
        for (i = 1; i < length; i++) {
            if (list[i] > max) {
                max = list[i];
            }

        }
        return max;
    }

    /**
     * @param list  需要比较的数组
     * @param begin 开始位置
     * @param end   结束位置
     * @return 最大值
     */
    public static double max(double list[], int begin, int end) {
        int i;
        double max;
        max = list[begin];
        for (i = begin; i < end; i++) {
            if (list[i] > max) {
                max = list[i];
            }

        }
        return max;
    }

    /**
     * @param list  需要比较的数组
     * @param begin 开始位置
     * @param end   结束位置
     * @return 最大值
     */
    public static int max(int list[], int begin, int end) {
        int i;
        int max;
        max = list[begin];
        for (i = begin + 1; i < end; i++) {
            if (list[i] > max) {
                max = list[i];
            }

        }
        return max;
    }

    /**
     * @param list   需要比较的数组
     * @param length 数组长度
     * @return 最小值
     */
    public static double min(double list[], int length) {
        int i;
        double min;
        min = list[0];
        for (i = 1; i < length; i++) {
            if (list[i] < min) {
                min = list[i];
            }

        }
        return min;
    }

    /**
     * @param list   需要比较的数组
     * @param length 数组长度
     * @return 最小值
     */
    public static int min(int list[], int length) {
        int i;
        int min;
        min = list[0];
        for (i = 1; i < length; i++) {
            if (list[i] < min) {
                min = list[i];
            }

        }
        return min;
    }

    /**
     * @param list   需要比较的数组
     * @param begin  开始位置
     * @param length 结束位置
     * @return 最小值
     */
    public static double min(double list[], int begin, int length) {
        int i;
        double min;
        min = list[begin];
        for (i = begin + 1; i < length; i++) {
            if (list[i] < min) {
                min = list[i];
            }

        }
        return min;
    }

    /**
     * @param list   需要比较的数组
     * @param begin  开始位置
     * @param length 结束位置
     * @return 最小值
     */
    public static int min(int list[], int begin, int length) {
        int i;
        int min;
        min = list[begin];
        for (i = begin + 1; i < length; i++) {
            if (list[i] < min) {
                min = list[i];
            }

        }
        return min;
    }

    /**
     * 将数组左移一位，并在最后一位添加数据
     *
     * @param demo 原始数组
     * @param ex   数据
     * @return 左移后数组
     */
    public static double[] Zuoyi(double demo[], double ex) {
        System.arraycopy(demo, 1, demo, 0, demo.length - 1);
        demo[demo.length - 1] = ex;
        return demo;
    }

    /**
     * 将数组左移一位，并在最后一位添加数据
     *
     * @param demo 原始数组
     * @param ex   数据
     * @return 左移后数组
     */
    public static Float[] Zuoyi(Float demo[], Float ex) {
        System.arraycopy(demo, 1, demo, 0, demo.length - 1);
        demo[demo.length - 1] = ex;
        return demo;
    }

    /**
     * 将数组左移一位，并在最后一位添加数据
     *
     * @param demo 原始数组
     * @param ex   数据
     * @return 左移后数组
     */
    public static int[] Zuoyi(int demo[], int ex) {
        System.arraycopy(demo, 1, demo, 0, demo.length - 1);
        demo[demo.length - 1] = ex;
        return demo;
    }
}
