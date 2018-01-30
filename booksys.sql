-- 删除数据库再创建(注意指定默认字符集为utf8)
drop DATABASE if EXISTS booksys;
create DATABASE booksys DEFAULT charset utf8;
-- 选中数据库
use booksys;

-- 删除表再创建表
drop TABLE if EXISTS tb_reader;
-- 创建读者表
create TABLE tb_reader 
(
readerid INTEGER not null,
rname VARCHAR(20) not null,
gender bit DEFAULT 1,
tel char(11),
regdate date not null,
available bit DEFAULT 1,
lendbookcount INTEGER DEFAULT 0,		-- 当前借了多少本图书
type bit not null,						-- 读者类型，1为老师，0为学生
PRIMARY key (readerid)
);

DROP TABLE IF EXISTS tb_book;
-- 创建图书表
CREATE TABLE tb_book (
  bookid INTEGER NOT NULL,
	isbn VARCHAR(13) not null,
  bname varchar(100) NOT NULL,
	price decimal(6,2) NOT NULL,
  author varchar(100) NOT NULL,
  publisher varchar(100) NOT NULL,
	pubdate date,
  lended bit DEFAULT 0,
	counter int DEFAULT 0,
	available bit DEFAULT 1,
  PRIMARY KEY (bookid)
);

DROP TABLE if EXISTS tb_record;
-- 创建记录表
create table tb_record
(
-- 自动增长
	recordid BIGINT not null auto_increment,
	bid INTEGER not null,
	rid INTEGER not null,
	lenddate datetime not null,
	backdate datetime,
	publishment DECIMAL(5,2),
	PRIMARY key (recordid)
);

-- 外键约束(级联更新)
ALTER TABLE tb_record add CONSTRAINT fk_record_bid
FOREIGN key(bid) REFERENCES tb_book(bookid)
on update CASCADE;

alter TABLE tb_record add CONSTRAINT fk_record_rid
FOREIGN key(rid) REFERENCES tb_reader(readerid)
on UPDATE CASCADE;

-- SQL:结构化查询语言
-- DDL(数据定于语言)：create/DROP/ALTER
-- DML(数据操纵语言)：insert/delete/UPDATE
-- DQL（数据查询语言）：select
-- DCL(数据控制语言)：grant/REVOKE...

-- 插入数据(注意now（）函数和default关键字)
INSERT into tb_reader VALUES(10001,'王大锤',1,'12345678911',NOW(),DEFAULT);
INSERT into tb_reader (readerid,rname,regdate) VALUES (10002,'李大嘴','2017-12-12');

-- 插入指定内容的数据
INSERT INTO tb_book(bookid,bookname,author,publisher,price) values (1234,'九阴真经','梅超风','神雕侠侣出版社',9999.0);
-- 删除数据
delete from tb_book where bookid = 1234;
