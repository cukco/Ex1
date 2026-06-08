CREATE OR REPLACE PROCEDURE calculate_order_total (
    order_id_input INT,
    OUT total NUMERIC
)
    LANGUAGE plpgsql
AS $$
BEGIN
    SELECT quantity * unit_price INTO total
    FROM order_detail
    WHERE order_id = order_id_input;
END;
$$;

call calculate_order_total(102, NULL);
