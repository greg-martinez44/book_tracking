create or replace view v_graphic_novel_stats as
select
	b.title,
    b.pages,
    b.year,
    b.genre,
    a.author,
    pub.imprint,
    i.illustrator,
    t.translator
from books as b
inner join authors as a
	on b.author_id = a.author_id
inner join publishers pub
	on b.imprint_id = pub.imprint_id
inner join illustrators as i
	on b.illustrator_id = i.illustrator_id
inner join translators as t
	on b.translator_id = t.translator_id
where
	b.format = 'gn';