package cn.zc.test;

import java.util.Comparator;

public class BinarySearch {
	public static void main(String[] args) {

	}

	public static <T extends Comparable<T>> int search(T[] arr, T key) {
		int start = 0;
		int end = arr.length - 1;
		while (start <= end) {
			int mid = (end - start) / 2 + start; // 防止int值溢出
			// int mid = (end+start)>>>1;
			if (key.compareTo(arr[mid]) == 0) {
				return mid;
			} else if (key.compareTo(arr[mid]) < 0) {
				end = mid - 1;

			} else {
				start = mid + 1;
			}
		}
		return -1;
	}

	public static <T> int search(T[] arr, T key, Comparator<T> comp) {
		int start = 0;
		int end = arr.length - 1;
		while (start <= end) {
			int mid = (end - start) / 2 + start; // 防止int值溢出
			// int mid = (end+start)>>>1;
			if (comp.compare(key, arr[mid]) == 0) {
				return mid;
			} else if (comp.compare(key, arr[mid]) < 0) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		return -1;
	}
}
