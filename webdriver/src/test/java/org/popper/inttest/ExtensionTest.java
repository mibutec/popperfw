package org.popper.inttest;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import junit.framework.Assert;

import org.junit.Test;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.annotations.RunAfter;
import org.popper.fw.annotations.RunBefore;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.interfaces.ReEvalutateException;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.inttest.ExtensionTest.Counting.CountingImpl;
import org.popper.inttest.ExtensionTest.Revaluating.RevaluatingImpl;
import org.popper.testpos.SelfImplementedLocator;

public class ExtensionTest extends AbstractIntTest {
	@Test
	public void testOwnLocator() {
		SelfImplementedLocator selfImplementedLocator = factory.createPage(SelfImplementedLocator.class);
		selfImplementedLocator.open();
		
		Assert.assertEquals("first idLocator", selfImplementedLocator.xxx().text());
		Assert.assertEquals("second idLocator", selfImplementedLocator.xxxWithParam("1").text());
	}
	
	@Test
	public void testReevalutate() {
		RevaluatingPO po = factory.createPage(RevaluatingPO.class);
		Assert.assertEquals(Integer.valueOf(3), po.value());
		Assert.assertEquals(Integer.valueOf(4), po.value());
	}
	
	@Test
	public void testOrder() {
		OrderPO po = factory.createPage(OrderPO.class);
		Assert.assertEquals(Integer.valueOf(3), po.order1());
		Assert.assertEquals(Integer.valueOf(3), po.order2());
		Assert.assertEquals(Integer.valueOf(3), po.order3());
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(CountingImpl.class)
	public static @interface Counting {
		public static class CountingImpl implements IAnnotationProcessor<Counting, Integer> {
			private static int counter = 0;
			
			@Override
			public Integer processAnnotation(Counting locatorAnnotation, LocatorContextInformation info,
					Integer lastResult) throws ReEvalutateException {
				return counter++;
			}
		}
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(RevaluatingImpl.class)
	public static @interface Revaluating {
		public static class RevaluatingImpl implements IAnnotationProcessor<Revaluating, Integer> {
			@Override
			public Integer processAnnotation(Revaluating annotation, LocatorContextInformation info,
					Integer lastResult) throws ReEvalutateException {
				if (lastResult < 3) {
					throw new ReEvalutateException();
				}
				
				return lastResult;
			}
		}
	}
	
	@Page
	public interface RevaluatingPO {
		@Counting
		@Revaluating
		public Integer value();
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	public static @interface Order1 {
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	@RunAfter(Order1.class)
	@RunBefore(Order3.class)
	public static @interface Order2 {
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	public static @interface Order3 {
	}
	
	public static class OrderedImpl implements IAnnotationProcessor<Annotation, Integer> {
		@Override
		public Integer processAnnotation(Annotation annotation, LocatorContextInformation info,
				Integer lastResult) throws ReEvalutateException {
			Integer value;
			if (annotation instanceof Order1) {
				value = -1;
			} else if (annotation instanceof Order2) {
				value = 1;
			} else {
				value = 2;
			}
			
			if (lastResult == null && value == -1) {
				return 1;
			}
			
			Assert.assertEquals(Integer.valueOf(value), lastResult);				
			return ++lastResult;
		}
	}

	@Page
	public interface OrderPO {
		@Order1
		@Order2
		@Order3
		public Integer order1();
		
		@Order3
		@Order2
		@Order1
		public Integer order2();
		
		@Order2
		@Order3
		@Order1
		public Integer order3();
	}

}
