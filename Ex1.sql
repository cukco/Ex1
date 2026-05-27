insert into students (id,name, age, major, gpa) values
    (6,'Hùng',23,'Hoá học',3.4);

update students
set gpa=3.6 where name='Bình';

delete from students
where gpa<3.0;

select name from students
where major='CNTT'
    limit 1;

select *from students
where gpa between 3.0 and 3.6;

select *from students
where name ilike 'c%';

select *from students
order by name asc
    limit 3 offset 1