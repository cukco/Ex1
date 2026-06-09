create or replace function update_last_modified()
returns trigger as $$
    begin
        new.last_modified=current_timestamp;
        return new;
    end;
$$ language plpgsql;

create or replace trigger trg_update_last_modified
    before update on products
    for each row
    execute function update_last_modified();

update products
set id=6 where id=5;
