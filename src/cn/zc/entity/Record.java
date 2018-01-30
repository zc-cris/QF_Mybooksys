package cn.zc.entity;
import java.util.Date;

/**
 * 记录表对应实例
 * @author Administrator
 *
 */
public class Record {
	private Integer id;		
	private Integer bid;
	private Integer rid;
	private Date lendDate;
	private Date backDate;
	private Double publishment;
	public Record(Integer id, Integer bid, Integer rid, Date lendDate, Date backDate, Double publishment) {
		this.id = id;
		this.bid = bid;
		this.rid = rid;
		this.lendDate = lendDate;
		this.backDate = backDate;
		this.publishment = publishment;
	}
	public Record() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBid() {
		return bid;
	}
	public void setBid(Integer bid) {
		this.bid = bid;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public Date getLendDate() {
		return lendDate;
	}
	public void setLendDate(Date lendDate) {
		this.lendDate = lendDate;
	}
	public Date getBackDate() {
		return backDate;
	}
	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}
	public Double getPublishment() {
		return publishment;
	}
	public void setPublishment(Double publishment) {
		this.publishment = publishment;
	}
}
