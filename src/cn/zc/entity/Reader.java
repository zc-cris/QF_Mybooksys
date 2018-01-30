package cn.zc.entity;

import java.util.Date;

/**
 * 读者表对应实例
 * @author Administrator
 *
 */

public class Reader {
	private Integer readerId;			
	private String rName;
	private boolean gender;
	private String tel;
	private Date regDate;
	private boolean available;
	
	private Integer lendBookCount;
	private Integer lendDays;
	private boolean type;
	private Integer canLendCount;
	
	public Reader() {
	}
	
	
	public Reader(Integer readerId, String rName, boolean gender, String tel, Date regDate, boolean available,
			Integer lendBookCount, Integer lendDays, boolean type, Integer canLendCount) {
		this.readerId = readerId;
		this.rName = rName;
		this.gender = gender;
		this.tel = tel;
		this.regDate = regDate;
		this.available = available;
		this.lendBookCount = lendBookCount;
		this.lendDays = lendDays;
		this.type = type;
		this.canLendCount = canLendCount;
	}
	
	public Integer getReaderId() {
		return readerId;
	}
	public void setReaderId(Integer readerId) {
		this.readerId = readerId;
	}
	public String getrName() {
		return rName;
	}
	
	public void setrName(String rName) {
		this.rName = rName;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public Integer getLendBookCount() {
		return lendBookCount;
	}
	public void setLendBookCount(Integer lendBookCount) {
		this.lendBookCount = lendBookCount;
	}
	public Integer getLendDays() {
		return lendDays;
	}
	public void setLendDays(Integer lendDays) {
		this.lendDays = lendDays;
	}
	public boolean isType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	public Integer getCanLendCount() {
		return canLendCount;
	}
	public void setCanLendCount(Integer canLendCount) {
		this.canLendCount = canLendCount;
	}
	
	@Override
	public String toString() {
		return "Reader [readerId=" + readerId + ", rName=" + rName + ", gender=" + (gender==true?"男":"女") + ", tel=" + tel
				+ ", regDate=" + regDate + ", available=" + (available==true?"有效用户":"无效用户") + ", lendBookCount=" + lendBookCount
				+ ", lendDays=" + lendDays + ", type=" + (type==true?"老师":"学生") + ", canLendCount=" + canLendCount + "]";
	}
	
	
	
}
