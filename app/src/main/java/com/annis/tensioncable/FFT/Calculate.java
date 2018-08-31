package com.annis.tensioncable.FFT;


public class Calculate {
    private static double density;
    private static double length;
    private static double frequency;

    public Calculate(double fre, double den, double leng) {
        frequency = fre;
        density = den;
        length = leng;


        //		 try {
        //		 System.out.println("Please input the density of the cable (density*area): ");
        //		 density = (double) System.in.read();
        //		 System.out.println("Your input is:" + density);
        //		 System.out.println("Please input the length of the cable: ");
        //		 length = (double) System.in.read();
        //		 System.out.println("Your input is:" + length);
        //		 }
        //		 catch(IOException e) {
        //			 e.printStackTrace();
        //		 }
    }


    public double cableForce() {
        return 4 * density * (Math.pow(length, 2)) * (Math.pow(frequency, 2));
    }


}
