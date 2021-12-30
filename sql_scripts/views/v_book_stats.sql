create or replace view v_book_stats as
select
	b.title,
    b.pages,
    b.year,
    b.format,
    b.genre,
    a.author,
    pub.imprint
from books as b
inner join authors as a
	on b.author_id = a.author_id
inner join publishers pub
	on b.imprint_id = pub.imprint_id
where
	b.format not in ('audio', 'gn');