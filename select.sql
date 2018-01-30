SELECT * from tb_reader;
-- 投影
SELECT readerid,rname,regdate from tb_reader;
-- alias 别名
SELECT readerid as 编号,rname as 名字,regdate as 注册日期 FROM tb_reader;

-- 筛选(条件组合 ： and or)
SELECT * from tb_reader WHERE gender = 1;
SELECT * from tb_reader WHERE gender = 1 and regdate = '2017-12-28';
SELECT * from tb_reader WHERE gender = 1 or regdate = '2017-12-28';
SELECT * from tb_reader WHERE gender = 1 or regdate <> '2017-12-28';
update tb_reader set tel = null where readerid in (123,10001);
SELECT * from tb_reader where tel is null;

-- 模糊查询
-- 查询姓王的读者
select * from tb_reader WHERE rname like '王%';
-- 查询姓王的单名的读者
select * from tb_reader WHERE rname like '王_';
-- 查询王姓双名的读者
select * from tb_reader WHERE rname like '王__';
-- 查询名字含有日的读者（不建议使用,效率较低）
select * from tb_reader WHERE rname like '%花%';

-- 查询时排序(默认升序 ASC)
SELECT * from tb_reader ORDER BY readerid;
-- 降序
SELECT * from tb_reader ORDER BY readerid DESC;
-- 分页查询（ LIMIT,mysql专用分页关键字 (page-1)*size,size ）
SELECT * from tb_reader ORDER BY readerid asc LIMIT 2;   -- 查询前两条数据
SELECT * from tb_book  LIMIT 2,2;	-- 每页两条数据，查询第二页的数据
-- 分组查询：(MAX/MIN/AVG/SUM/COUNT)
SELECT gender,COUNT(gender) AS tatal from tb_reader GROUP BY gender;
-- 先where 限定，然后group by 分组，再order by 排序，最后limit分页
select bookid,isbn,bname,price,author,publisher,pubdate,lended,
sum(counter) as counter,available from tb_book group by isbn order by counter desc
limit 10;
select available from tb_reader where readerid =123;

select bid,lenddate from tb_record where lenddate = 
(select max(lenddate) from tb_record where bid = 222 and rid = 123);

update tb_record set backdate =NOW(), publishment =11.23 where recordid =10;
