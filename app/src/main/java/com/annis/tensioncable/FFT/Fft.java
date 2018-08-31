package com.annis.tensioncable.FFT;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/********************************************
 *
 * @author Silvia
 *
 *
 * Compilation: javac FFT.java
 * Execution:   java FFT N
 * Dependencies: Complex.java
 *
 *
 * Compute the FFT of a lenth N complex sequence. 
 * Bare bones implementation that turns in O(N log N) time. 
 *

 ************************/
public class Fft {
    // compute the FFT of x[]
    public static Complex[] fft(Complex[] x) {
        int N = x.length;

        //base case
        if (N == 1)
            return new Complex[]{x[0]};

        // radix 2 Cooley-Tukey FFT
        //		if (N%2 !=0) { throw new RuntimeException("N is not a power of 2");}

        if (N % 2 != 0) {
            return dft(x);

        }


        // fft of even terms
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd = even; //reuse the arrya
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    public static Complex[] dft(Complex[] x) {
        int n = x.length;

        // exp(-2j*n*PI) = cos(-2j*n*PI) + i*sin(-2j*n*PI);
        if (n == 1) {
            return x;
        }

        Complex[] result = new Complex[n];
        for (int j = 0; j < n; j++) {
            result[j] = new Complex(0, 0);
            for (int k = 0; k < n; k++) {
                double p = -2 * j * k * Math.PI / n;
                Complex m = new Complex(Math.cos(p), Math.sin(p));
                result[j] = result[j].plus(x[k].times(m));
            }
        }
        return result;
    }


    // compute the circular convolution of x and y
/*	public static Complex[] cconvolve(Complex[] x, Complex[] y) {
        // should probably pad x and y with 0s so that they have same length and are powers of 2
		if (x.length != y.length) { throw new RuntimeException("Dimensions don't agree");}
		
		int N = x.length;
		
		// compute FFT of each sequence
		Complex[] a = fft(x);
		Complex[] b = fft(y);
		
		// point-wise multiply
		Complex[] c = new Complex[N];
		for (int i =0; i < N; i++) {
			c[i] = a[i].times(b[i]);		
		}
		
		// compute inverse FFT
		return ifft(c);
		
	}
	*/

    //compute the linear convolution of x and y
/*	public static Complex[] convolve(Complex[] x, Complex[] y) {
		Complex ZERO = new Complex(0, 0);
		
		Complex[] a = new Complex[2*x.length];
		for (int i = 0; i< x.length; i++) a[i] = x[i];
		for (int i = x.length; i< 2*x.length; i++) a[i] = ZERO;
		
		Complex[] b=new Complex[2*y.length];
		for  (int i = 0; i < y.length; i++) b[i] = y[i];
		for  (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;
		
		return cconvolve(a,b);
	}*/

    //display an array of complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("............");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }

    public static ArrayList<Float> obtainFreSpectrum(Complex[] frequency, int n) {
        int single = (int) (n - 1) / 2 + 1;
        ArrayList<Float> fre = new ArrayList<>();
        //double[] fre = new double[single];
        for (int i = 0; i < single; i++) {
            fre.add((float) frequency[i].abs());
            //fre[i] = frequency[i].abs();
        }
        return fre;
    }


    public static double findFre(Complex[] frequency, double Fs, int n, double preFre) {


        int single = (int) (n - 1) / 2 + 1;
        double[] fre = new double[single];
        Double[] freDouble = new Double[single];
        //		show(y,"y = fft(x)");
        for (int i = 0; i < single; i++) {
            fre[i] = frequency[i].abs();
            freDouble[i] = fre[i];   // the amplitude of the frequency
            //			System.out.println(fre[i]);
        }


        double fs = Fs / n; // frequency slot
        int slot = (int) (preFre / fs);

        int[] maxIndex = new int[2];
        double[] maxValue = new double[2];
        double[] maxFre = new double[2];
        for (int i = 0; i < 2; i++) {
            List<Double> freList = new ArrayList<>();

            Collections.addAll(freList, freDouble);


            maxValue[i] = Collections.max(freList);
            //			System.out.println("max Fre" + maxValue[i]);


            maxIndex[i] = freList.indexOf(maxValue[i]);

            maxFre[i] = maxIndex[i] * fs;
            //			System.out.println("max Fre" + maxFre[i]);


            for (int j = maxIndex[i] - slot / 2; j < maxIndex[i] + slot / 2; j++) {
                Log.e("j的初值", "" + j);
                freDouble[j] = (double) 0;
            }
        }

        double diffFre = maxFre[1] - maxFre[0];
        int order = (int) (maxFre[0] / diffFre);
        return maxFre[0] / order;

    }
	


/*	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
	
		

	
		
		File file = new File("src/env.txt");
		//System.out.println(txt2String(file));
		
		double[] data = ReadFiledata.txt2Doulbe(file);
		int num = (int) Math.floor(Math.log(data.length) / Math.log(2));
		int n = (int) Math.pow(2, num);
		Complex[] x = new Complex[n];
		//origin data
		for (int i = 0; i < n ; i++) {
//			x0[i] = -2*Math.random();
			x[i] = new Complex(data[i]);
//			x[i] = new Complex(2*i + 1,0);
//			x[i] = new Complex(-2*Math.random() + 1, 0);
		
		}
//		show(x,"x");
		
		
		
		//FFT of original data
		Complex[] y = fft(x);
		
		double Fs = 128; //HZ sample frequency
		double frequency = findFre(y, Fs, n);
		
		System.out.println("Frequency: " + frequency + "Hz");
		
		
		
		
			System.out.println("max Fre" + maxFre[i]);
		int single = (int)(n-1)/2+1;
		double [] fre = new double[single];
		Double[] freDouble = new Double[single];
//		show(y,"y = fft(x)");
		for (int i=0; i < single; i++) {
			fre[i] = y[i].abs();
			freDouble[i] = new Double(fre[i]);
			System.out.println(fre[i]);
		}
		
		
	
		double fs = Fs/n; // frequency slot
		int slot = (int) (0.3/fs);
		
		List<Double> freList = 	new ArrayList<>();
		
	
		
		Collections.addAll(freList, freDouble);
		
	
		double maxValue = Collections.max(freList);
		System.out.println("max Fre" + maxValue);
		
		
		int maxIndex = freList.indexOf(maxValue);
		
		double maxFre = maxIndex*fs;
		System.out.println("max Fre" + maxFre);
		
	

		Calculate cal = new Calculate(frequency);
		double force = cal.cableForce();
		System.out.println("cable force : " + force);

		
		long end = System.currentTimeMillis();
		
		System.out.println("Duration: " + (end - start) + "ms");
	}
	*/


}
