package com.saien.cglib;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.Mixin;

/**
 * @author Paiker Abbas
 * 
 *         This example is for the understanding/study purpose of CgLib
 * 
 */
public class PersonServiceTest {

	@Test
	public void testCglib() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(PersonService.class);
		enhancer.setCallback((FixedValue) () -> "Hello Paiker");
		PersonService proxy = (PersonService) enhancer.create();

		String result = proxy.sayHello(null);

		assertEquals(result, "Hello Paiker");

	}

	@Test
	public void returnValueBasedOnMethodSignature() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(PersonService.class);

		enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
			if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
				return "Hello Paiker";
			} else {
				return proxy.invokeSuper(obj, args);
			}
		});

		PersonService proxy = (PersonService) enhancer.create();
		assertEquals("Hello Paiker", proxy.sayHello(null));

		int nameLength = proxy.nameLength("John");
		assertEquals(4, nameLength);
	}

	@Test
	public void beanCreator() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		BeanGenerator beanGenerator = new BeanGenerator();
		beanGenerator.addProperty("name", String.class);
		Object myBean = beanGenerator.create();
		Method setter = myBean.getClass().getMethod("setName", String.class);
		setter.invoke(myBean, "Some string value set by Cglib");
		Method getter = myBean.getClass().getMethod("getName");
		assertEquals("Some string value set by Cglib", getter.invoke(myBean));
	}

	@Test
	public void createMixin() {
		Mixin mixin = Mixin.create(new Class[] { InterfaceA.class, InterfaceB.class, MixinInterface.class },
				new Object[] { new ClassA(), new ClassB() });
		MixinInterface mixinDelegate = (MixinInterface) mixin;
		assertEquals("first behaviour", mixinDelegate.firstMethod());
		assertEquals("second behaviour", mixinDelegate.secondMethod());
	}
}
