create or replace view v_audiobook_stats as
select
	b.title,
    b.duration,
    b.year,
    b.genre,
    a.author,
    pub.imprint,
    n.narrator
from books as b
inner join authors as a
	on b.author_id = a.author_id
inner join publishers pub
	on b.imprint_id = pub.imprint_id
left join narrators as n
	on b.narrator_id = n.narrator_id
where
	b.format = 'audio';