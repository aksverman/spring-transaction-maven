package com.zetainteractive.security.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class LambdaExpression {
	public static void main(String []args) {
		
		LambdaExpression lambdaExpression = new LambdaExpression();
		
		//Runnable implementation
		lambdaExpression.runnableUsingLambda();
		
		//Sorting List 
		lambdaExpression.listSortByUsingComparatorWithLambda();
		
		//Predicate Example		
		lambdaExpression.predicateUsingLambda();
	}
	
	
	/*
	 * Runnable interface run() implementation
	 * using old method and 
	 * Lambda Expression to define run()
	 */
	public void runnableUsingLambda() {
		System.out.println("=== RunnableTest ===");
		// Runnable Test
		Runnable run1 = new Runnable() {
			@Override
			public void run() {
				System.out.println("old runnbale method");
			}
		};		
		// Runnable Test using Lambda Expressions
		Runnable run2 = () -> System.out.println("Lambda Exp Runnable run() impl");
		
		run1.run();
		run2.run();
	}
	
	/*
	 * Sorting List of names by using Comparator
	 * using old method and 
	 * Lambda Expression to implement compare()
	 */
	public void listSortByUsingComparatorWithLambda() {
		
		System.out.println("=== List Collection Sort ===");
		List<String> names = Arrays.asList("rohit", "anna", "harish", "krishna");
		List<String> mncs = Arrays.asList("tcs", "hcl", "zeta", "infosys");
		
		//previous release version implementation to sort
		Collections.sort(names, new Comparator<String>() {
		    @Override
		    public int compare(String a, String b) {
		        return a.compareTo(b);
		    }
		});
		System.out.println(names);
		//sorting using lambda expressions
		Collections.sort(mncs, (a, b) -> a.compareTo(b));
		mncs.forEach(mncsvar -> System.out.println(mncsvar));
	}
	
	/*
	 * Predicate to filter odd numbers present in Collection
	 * predicate accepts Lambda Expression to 
	 * call test() on passed parameter
	 * returns boolean value as test()
	 */
	public void predicateUsingLambda() {

		System.out.println("=== Predicate Functional Interface ===");
		Collection<Integer> myInts = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
		Collection<Integer> onlyOdds = filterOddNumbers(n -> n % 2 != 0, myInts);
		System.out.println(onlyOdds);
	}
	
	/*
	 * @param predicate
	 * @param items
	 * @return collection of items be filtered by passed predicate
	 */
	private <T> Collection<T> filterOddNumbers(Predicate<T> predicate, Collection<T> items) {
		Collection<T> result = new ArrayList<T>();
		for(T item: items) {
			if(predicate.test(item)) 
				result.add(item);
		}
		return result;
	}
}
