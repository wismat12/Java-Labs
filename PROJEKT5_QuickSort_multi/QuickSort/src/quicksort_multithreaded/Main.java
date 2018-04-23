package quicksort_multithreaded;

import java.util.Arrays;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		int size = (int) Math.pow(10, 6);
		int []tab_multithreaded;
		int []tab_parallel_sort;
		int number;
		Random generator = new Random();
		
		tab_multithreaded = new int[size];
		tab_parallel_sort = new int[size];
		
		for(int i = 0;i < size; i++){
			number = generator.nextInt(10000);
			tab_multithreaded[i] = number;
			tab_parallel_sort[i] = number;
		}
			
		long startTime = System.nanoTime();
		
		Arrays.parallelSort(tab_parallel_sort);
		
		double estimatedTime = System.nanoTime() - startTime;
		
		estimatedTime = estimatedTime/1000000;
		
		System.out.println("Czas Parallel Sort w milisekundach: " + estimatedTime);   
		System.out.println("Teraz mój QuickSort");  
		
		startTime = System.nanoTime();
		
		QuickSort Q = new QuickSort(size, tab_multithreaded, false);
		
		estimatedTime = System.nanoTime() - startTime;
		
		estimatedTime = estimatedTime/1000000;
		
		System.out.println("Czas mojego QuickSort w milisekundach: " + estimatedTime);   
		
		System.out.println(">>>Koniec g³ównego w¹tku!");

	}
}
