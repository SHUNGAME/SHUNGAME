package org.shun.game.test;

import org.shun.game.springcontextholder.ShunContext;

public class SpringTest {
	public static void main(String[] args){
		ShunContext sc = new ShunContext(new String[]{"classpath*:config/spring/**/applicationContext-*.xml"});
		Object test = sc.getBean("test");
		System.out.println(test.getClass().getName());
	}
}
