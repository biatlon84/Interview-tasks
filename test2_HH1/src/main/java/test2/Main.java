package test2;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {

		int[] ar = new int[] { 1, 2, 8, 3, 7, -1, -1, -9, -7, 3, -6 };
		int x = -10;
		System.out.println(Arrays.toString(ar));
		find(ar, x);
		System.out.println("");
		for (int ii = 0; ii < ar.length; ii++) {
			int a1 = ar[ii];
			for (int i = 0; i < ar.length; i++) {
				if (x - ar[i] == a1) {
					System.out.println(ar[i]);
					System.out.println(a1);
					return;
				}
			}
		}
		System.out.println("No numbers were found");
	}

	public static void find(int[] ar, int x) {
		int f = 0;
		int n = ar.length;
		int end = n - 1;
		int start = 0;
		Arrays.sort(ar);
		while (true) {
			f = ar[start] + ar[end];
			if (x == f) {
				System.out.println(ar[end]);
				System.out.println(ar[start]);
				return;
			}
			if (f > x) {
				end--;
			}
			if (f < x) {
				start++;
			}
			if (start >= end) {
				System.out.println("No numbers were found");
				break;
			}
		}

	}

}
