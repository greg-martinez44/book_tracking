start transaction;
set @title = 'Rent-A-Girlfriend%(Volume 7)%';
set @started = '2021-12-30';
select b_ordered.book_id into @book_id
from (
select b.book_id, b.title, row_number() over (partition by b.title order by pur.purchase_date desc) row_num
from books b
left join purchases pur
on b.book_id = pur.book_id
where
	title like @title
) b_ordered
where b_ordered.row_num = 1;
insert into starts (book_id, started)
values (@book_id, @started);
select
	*
from starts
where book_id = @book_id;
commit;
