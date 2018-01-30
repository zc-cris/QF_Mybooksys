package cn.zc.entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 书籍表对应的实体
 * @author Administrator
 *
 */
public class Book {
	private Integer bookid;		
	private String isbn;
	private String bname;
	private BigDecimal price;
	private String author;
	private String publisher;
	private Date pubdate;
	private boolean lended;
	private Integer counter;
	private boolean available;
	
	public Book() {
	}
	public Book(Integer id, String isbn, String name, BigDecimal price, String author, String publisher, Date pubDate,
			boolean lended, Integer counter) {
		this.bookid = id;
		this.isbn = isbn;
		this.bname = name;
		this.price = price;
		this.author = author;
		this.publisher = publisher;
		this.pubdate = pubDate;
		this.lended = lended;
		this.counter = counter;
	}
	public Integer getId() {
		return bookid;
	}
	public void setId(Integer id) {
		this.bookid = id;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getName() {
		return bname;
	}
	public void setName(String name) {
		this.bname = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Date getPubDate() {
		return pubdate;
	}
	public void setPubDate(Date pubDate) {
		this.pubdate = pubDate;
	}
	public boolean isLended() {
		return lended;
	}
	public void setLended(boolean lended) {
		this.lended = lended;
	}
	public Integer getCounter() {
		return counter;
	}
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	@Override
	public String toString() {
		return "Book [id=" + bookid + ", isbn=" + isbn + ", name=" + bname + ", price=" + price + ", author=" + author
				+ ", publisher=" + publisher + ", pubDate=" + pubdate + ", lended=" + lended + ", counter=" + counter
				+ "]";
	}
}
