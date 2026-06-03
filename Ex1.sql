create index idx_author on book(author);
create index on book using hash(genre);

explain analyse select *from book
where author ilike 'Rowling%';

explain analyse select *from book
where genre='Fantasy';

create index idx_genre on book(genre);

create index on book using gin(title,description);

cluster book using idx_genre;

