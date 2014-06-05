package org.shun.game.springcontextholder;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ShunContext implements ApplicationContextAware , DisposableBean {
	private static ApplicationContext applicationContext = null;
	
	public ShunContext(String[] filenames){
		applicationContext = new ClassPathXmlApplicationContext(filenames);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		ShunContext.applicationContext = applicationContext;
	}

	/**
	 * ȡ�ô洢�ھ�̬�����е�ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * �Ӿ�̬����ApplicationContext��ȡ��Bean, �Զ�ת��Ϊ����ֵ���������.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * �Ӿ�̬����ApplicationContext��ȡ��Bean, �Զ�ת��Ϊ����ֵ���������.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return (T) applicationContext.getBeansOfType(clazz);
	}

	/**
	 * ���applicationContext��̬����.
	 */
	public static void cleanApplicationContext() {
		applicationContext = null;
	}

	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException(
					"applicaitonContextδע��,����applicationContext.xml�ж���SpringContextHolder");
		}
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		applicationContext = null;
	}

}
