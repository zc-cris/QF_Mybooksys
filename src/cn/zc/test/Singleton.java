package cn.zc.test;

//延迟加载模式的单例
public class Singleton {
	
	private static class inner{
		private Singleton singleton = new Singleton();
	}

	private Singleton() {			//唯一的不好：可以通过暴力反射破解创建
	}

	public static Singleton getInstance() {
		return new inner().singleton;
	}
}
