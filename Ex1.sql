create or replace function f_check()
returns trigger as $$
    declare
    begin
        insert into customer_log(customer_name, action_time) values
        (new.name,current_timestamp);

        return new;
    end;
$$ language plpgsql;

create trigger t_check
    after insert on customers
    for each row
    execute function f_check();

insert into customers(name, email) VALUES ('Nguyễn Công Hưởng','huongcong@gmail.com');
