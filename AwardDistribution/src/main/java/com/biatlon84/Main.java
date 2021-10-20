package com.biatlon84;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println(award(in));
	}

	public static int award(Scanner in) {
		String[] s = in.nextLine().split(" ", 0);
		int number_acc = Integer.parseInt(s[0]);
		int number_emp = Integer.parseInt(s[1]);
		int[] account = new int[number_acc];
		long sum = 0;
		int first = number_acc - Math.min(number_emp, number_acc);
		// Input data of accounts
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < number_acc; i++) {
			account[i] = Integer.parseInt(in.nextLine());
			list.add(account[i]);
		}
		Arrays.sort(account);
		for (int i = first; i < number_acc; i++) {
			sum += account[i];
		}
		int avrg = (int) (sum / number_emp);
		if (avrg <= 0) {
			return 0;
		}
		int binary = avrg - account[number_acc - 1] / number_emp;
		if (binary <= 0) {
			return avrg;
		}
		if (binary >= 2) {
			binary /= 2;
		}
		boolean forward = false;
		while (true) {
			int counter = 0;
			if (avrg > 0) {
				for (int j = first; (j < number_acc) && (counter < number_emp); j++) {
					counter += account[j] / avrg;
				}
			} else {
				avrg = 2;
				if (forward) {
					avrg = 1;
				}
			}
			if (binary >= 2) {
				binary /= 2;
			}
			if (counter >= number_emp) {
				if (forward) {
					return avrg;
				}
				avrg += binary;
			} else {
				if (binary <= 1) {
					forward = true;
				}
				avrg -= binary;
			}
		}
	}
}
