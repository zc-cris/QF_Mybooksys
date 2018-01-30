package cn.zc.entity;

/**
 * 为读者设定类型 每种类型的读者借书的总数量和最大天数都不相同
 * @author Administrator
 *
 */
public class Teacher extends Reader {

	public Teacher() {
		setCanLendCount(15);		//老师最多借15本
		setLendDays(60);
	}

	@Override
	public String toString() {
		return "Teacher [getReaderId()=" + getReaderId() + ", getrName()=" + getrName() + ", isGender()=" + (isGender()==true?"男":"女")
				+ ", getTel()=" + getTel() + ", getRegDate()=" + getRegDate() + ", isAvailable()=" + (isAvailable()==true?"有效用户":"无效用户")
				+ ", getLendBookCount()=" + getLendBookCount() + ", getLendDays()=" + getLendDays()
				+ ", getCanLendCount()=" + getCanLendCount() + "]";
	}
}
