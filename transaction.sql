-- 事务（transaction），不可分割的一组原子性操作，要么全做，要么全失败
-- 通常用于对数据库进行更改的时候使用
-- 事务的ACID特性：
-- A ： Atomicity（原子性）：不可分割，要么全做，要么全不做
-- C : 	Consistency(一致性)：事务前后数据状态保持一致性
-- I ： Isolation（隔离性）：事务不能看到彼此的中间状态
-- D :  Duration（持久性） ：事务完成后数据要持久化


-- 触发器：触发器是一种与表操作有关的数据库对象，当触发器所在表上出现指定事件时，将调用该对象，即表的操作事件触发表上的触发器的执行

-- 读者借书的时候向tb_record表中插入一条数据，同时对应的读者的lendBookCount+1；
drop trigger if exists lend_update_tb_reader_lendBookCount;
delimiter $
CREATE TRIGGER lend_update_tb_reader_lendBookCount AFTER INSERT ON tb_record for EACH ROW 
BEGIN 
DECLARE c int ;
set c = (SELECT lendBookCount from tb_reader where readerId = new.rid);
UPDATE tb_reader set lendBookCount = c+1 where readerId = new.rid;
END$
delimiter ;

-- 读者还书更新tb_record表的还书日期的时候，对应的读者的lendBookCount-1
drop TRIGGER if EXISTS return_update_tb_reader_lendBookCount;
delimiter $
create TRIGGER return_update_tb_reader_lendBookCount AFTER UPDATE on tb_record for EACH ROW
BEGIN
DECLARE c int ;
set c = (SELECT lendBookCount from tb_reader WHERE readerId = new.rid);
UPDATE tb_reader set lendBookCount = c-1 where readerId = new.rid;
END$
delimiter ;




