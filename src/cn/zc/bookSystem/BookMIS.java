package cn.zc.bookSystem;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import cn.zc.entity.Book;
import cn.zc.entity.Reader;
import cn.zc.manager.BookManager;
import cn.zc.manager.ReaderManager;

/**
 * 图书管理系统
 * @author 张城
 *
 */

public class BookMIS {

	private static BookManager bookManager = new BookManager();
	private static ReaderManager readerManager = new ReaderManager();
	private static Scanner scanner = new Scanner(System.in);
	private static boolean isRunning = true;
	private static int i = 0;
	private static Reader reader = new Reader();
	private static Book book = new Book();
	private static List<Book> books;

	public static void main(String[] args) throws Exception {
		System.out.println("===欢迎使用ABC图书管理系统===");
		while (isRunning) {
			welcome();
			i = scanner.nextInt();
			switch (i) {
			case 1:
				readerManage(readerManager, isRunning, scanner, i);
				break;

			case 2:
				bookManager(bookManager, isRunning, scanner, i);
				break;

			case 3:
				isRunning = false;
				System.out.println("欢迎使用本系统！下次再会");
				break;

			default:
				System.out.println("非法输入，请重新输入！");
				break;
			}
		}
	}

	/**
	 * 系统首页欢迎界面
	 */
	public static void welcome() {
		System.out.println("1.管理读者");
		System.out.println("2.管理图书");
		System.out.println("3.退出系统");
		System.out.println("请选择：");
	}

	/**
	 * 读者管理子系统
	 * @param readerManager	读者管理类
	 * @param flag	循环标识
	 * @param scanner	输入流对象
	 * @param i	用户输入数字
	 */
	
	public static void readerManage(ReaderManager readerManager, boolean flag, Scanner scanner, int i) {
		while (flag) {
			readerWelcome();
			i = scanner.nextInt();
			switch (i) {
			case 1:
				addReader(readerManager, scanner, reader);
				break;

			case 2:
				cancelReader(readerManager, scanner);
				break;

			case 3:
				updateReader(readerManager, scanner, reader);
				break;

			case 4:
				findReader(readerManager, scanner, reader);
				break;

			case 5:
				flag = false;
				break;

			default:
				System.out.println("非法输入，请重新输入");
				break;
			}
		}
	}

	/**
	 * 读者管理系统欢迎界面
	 */
	public static void readerWelcome() {
		System.out.println("欢迎来到读者管理页面，请输入对应的数字进行以下功能的选择");
		System.out.println("1.新增读者");
		System.out.println("2.注销读者");
		System.out.println("3.更新读者信息");
		System.out.println("4.查询读者信息");
		System.out.println("5.退出读者管理系统");
	}

	/**
	 * 查询读者信息
	 * @param readerManager	读者管理系统
	 * @param scanner	输入流对象
	 */
	
	public static void findReader(ReaderManager readerManager, Scanner scanner, Reader reader) {
		System.out.println("请输入你要查询的读者id");
		int id = scanner.nextInt();
		System.out.println("请输入你要查询的读者类型：1为老师；2为学生");
		boolean isTeacher = scanner.nextInt() == 1;
		reader = readerManager.findReaderById(id,isTeacher);
		System.out.println(reader == null ? "该用户不存在" : reader);
	}

	/**
	 * 更新读者数据
	 * @param readerManager	读者管理系统
	 * @param scanner	输入流对象
	 */
	public static void updateReader(ReaderManager readerManager, Scanner scanner, Reader reader) {
		System.out.println("请输入要更新的读者id");
		reader.setReaderId(scanner.nextInt());

		System.out.println("请输入新的名字");
		reader.setrName(scanner.next());

		System.out.println("请输入新的电话");
		reader.setTel(scanner.next());

		System.out.println(readerManager.updateReaderInfo(reader) == true ? "更新成功" : "更新失败");
	}

	/**
	 * 注销读者
	 * @param readerManager	读者管理系统
	 * @param scanner	输入流对象
	 */
	public static void cancelReader(ReaderManager readerManager, Scanner scanner) {
		System.out.println("请输入要注销的读者id");
		System.out.println(readerManager.removeReaderById(scanner.nextInt()) == true ? "注销成功" : "注销失败");
	}

	/**
	 * 新增读者
	 * @param readerManager
	 *            读者管理系统
	 * @param scanner
	 *            输入流对象
	 */
	public static void addReader(ReaderManager readerManager, Scanner scanner, Reader reader) {
		System.out.println("请输入读者的id");
		reader.setReaderId(scanner.nextInt());
		System.out.println("请输入读者的姓名");
		reader.setrName(scanner.next());

		// System.out.println("请输入读者的性别：true为男性；false为女性");
		// boolean gender = scanner.nextBoolean();

		System.out.println("请输入读者的性别：1为男性；2为女性");
		reader.setGender(scanner.nextInt() == 1);
		System.out.println("请输入读者的电话号码，共11位数字");
		reader.setTel(scanner.next());
		System.out.println("请输入读者的类别，如果是老师，请输入1；如果是学生，请输入2");
		reader.setType(scanner.nextInt() == 1);
		System.out.println(readerManager.createNewReader(reader) == true ? "添加成功" : "添加失败");
	}

	/**
	 * 图书管理子系统
	 * @param bookManager
	 *            图书管理系统
	 * @param flag
	 *            循环标志
	 * @param scanner
	 *            输入流对象
	 * @param i
	 *            用户输入选项
	 * @throws Exception
	 */
	public static void bookManager(BookManager bookManager, boolean flag, Scanner scanner, int i) throws Exception {
		int id;
		String isbn;
		String name;
		String author;
		
		while (flag) {
			bookWelcome();
			i = scanner.nextInt();
			switch (i) {
			case 1:
				addBook(bookManager, scanner);
				break;

			case 2:
				removeBook(bookManager, scanner);
				break;

			case 3:
				lendBook(bookManager, scanner);

				break;

			case 4:
				returnBook(bookManager, scanner);
				break;

			case 5:
				findBook(bookManager, scanner);
				break;

			case 6:
				findBooksByIsbn(bookManager, scanner);
				break;

			case 7:
				findBooksByAuthor(bookManager, scanner);
				break;

			case 8:
				findBooksByName(bookManager, scanner);
				break;

			case 9:
				findBooksTop10();
				break;
			case 10:
				flag = false;
				break;

			default:
				System.err.println("非法输入，请重新输入");
				break;
			}
		}
	}
	
	/**
	 * 图书借阅量前10
	 */
	private static void findBooksTop10() {
		System.out.println(bookManager.searchTop10Books());
	}

	/**
	 * 根据编号和读者借出一本书
	 * @param bookManager	图书关系子系统
	 * @param scanner	输入流对象
	 */
	public static void lendBook(BookManager bookManager, Scanner scanner) {
		System.out.println("请输入要借出的图书编号");
		int bookId = scanner.nextInt();
		System.out.println("请输入借书人的编号");
		int readerId = scanner.nextInt();
		System.out.println("请输入借书人的类型：老师就输入1；学生就输入2");
		boolean isTeacher = scanner.nextInt() == 1;
		System.out.println(bookManager.lendOut(bookId, readerId, isTeacher) == true ? "成功借出" : "未能借出");
		}

	/**
	 * 根据图书编号归还图书
	 * @param bookManager	图书管理子系统
	 * @param scanner		输入流对象
	 */
	public static void returnBook(BookManager bookManager, Scanner scanner) {
		System.out.println("请输入要归还的图书编号");
		int bookId = scanner.nextInt();
		System.out.println("请输入还书的读者编号");
		int readerId = scanner.nextInt();
		System.out.println("请输入还书的读者类型：老师请输入1；学生请输入2");
		boolean isTeacher = scanner.nextInt() == 1;
		double returnBack = bookManager.returnBack(bookId, readerId, isTeacher);
		System.out.println(returnBack>=0 ? "书本成功归还,缴纳罚金"
				+  returnBack+"元": "未能成功归还");
	}

	/**
	 * 根据图书名查询到所有的图书
	 * @param bookManager	图书管理子系统
	 * @param scanner		输入流对象
	 */
	public static void findBooksByName(BookManager bookManager, Scanner scanner) {
		String name;
		int page;
		int size;
		System.out.println("请输入图书名");
		name = scanner.next();
		System.out.println("请输入查询的页码");
		page = scanner.nextInt();
		System.out.println("请输入每页显示记录数");
		size = scanner.nextInt();
		books = bookManager.findBooksByName(name,page,size);
		System.out.println(books == null ? "没有该书名的图书:" : "有该书名的图书" + books);
	}
	
	/**
	 * 根据作者名字查询到所有的图书
	 * @param bookManager	图书管理子系统
	 * @param scanner		输入流对象
	 */
	public static void findBooksByAuthor(BookManager bookManager, Scanner scanner) {
		String name;
		int page;
		int size;
		System.out.println("请输入作者的名字");
		name = scanner.next();
		System.out.println("请输入查询的页码");
		page = scanner.nextInt();
		System.out.println("请输入每页显示记录数");
		size = scanner.nextInt();
		books = bookManager.findBooksByAuthor(name,page,size);
		System.out.println(books != null ? "有该作者的图书:" + books : "没有该作者的图书");
	}
	
	/**
	 * 根据isbn号查询到所有的图书
	 * @param bookManager	图书管理子系统
	 * @param scanner	输入流对象
	 */
	public static void findBooksByIsbn(BookManager bookManager, Scanner scanner) {
		String isbn;
		int page;
		int size;
		System.out.println("请输入要查找的图书isbn号");
		isbn = scanner.next();
		System.out.println("请输入查询的页码");
		page = scanner.nextInt();
		System.out.println("请输入每页显示记录数");
		size = scanner.nextInt();
		books = bookManager.findBooksByIsbn(isbn, page, size);
		System.out.println(books != null ? "有该isbn编号的图书:" + books : "没有该isbn编号的图书");
	}

	/**
	 * 下架一本图书
	 * @param bookManager 图书管理子系统
	 * @param scanner	输入流对象
	 */
	public static void removeBook(BookManager bookManager, Scanner scanner) {
		System.out.println("请输入要下架的图书编号");
		System.out.println(bookManager.removeBookById(scanner.nextInt()) == true ? "下架书籍成功" : "下架书籍失败");
	}

	/**
	 * 查找一本图书
	 * @param bookManager	图书管理子系统
	 * @param scanner		输入流对象
	 */
	public static void findBook(BookManager bookManager, Scanner scanner) {
		System.out.println("请输入要查找的图书编号");
		book = bookManager.findBookById(scanner.nextInt());
		System.out.println(book == null ? "没有该编号的图书" : "有该编号的图书:" + book);
	}

	/**
	 * 添加一本图书
	 * @param bookManager	图书管理子系统
	 * @param scanner	输入流对象
	 * @throws ParseException  类型转换异常
	 */
	public static void addBook(BookManager bookManager, Scanner scanner) throws ParseException {
		System.out.println("请输入图书编号");
		book.setId(scanner.nextInt());
		System.out.println("请输入图书isbn号");
		book.setIsbn(scanner.next());
		System.out.println("请输入图书名字");
		book.setName(scanner.next());
		System.out.println("请输入作者名字");
		book.setAuthor(scanner.next());
		System.out.println("请输入图书价格");
		book.setPrice(scanner.nextBigDecimal());

		System.out.println("请输入图书出版社");
		book.setPublisher(scanner.next());
		System.out.println("请输入图书出版日期");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		book.setPubDate(sdf.parse(scanner.next()));
		System.out.println(bookManager.addNewBook(book) == true ? "添加成功" : "添加失败");
	}

	/**
	 * 图书子系统欢迎界面
	 */
	public static void bookWelcome() {
		System.out.println("欢迎来到书籍管理子系统，请输入对应的数字进行以下功能的选择");
		System.out.println("1.新增图书"); // ok
		System.out.println("2.下架图书");	//ok
		System.out.println("3.借出图书");
		System.out.println("4.归还图书");
		System.out.println("5.根据id查找图书"); // ok
		System.out.println("6.根据isbn号查找所有图书");
		System.out.println("7.根据作者查找所有图书");
		System.out.println("8.根据书名查找所有图书");
		System.out.println("9.图书借阅量TOP10");
		System.out.println("10.退出图书管理系统");
	}
}
