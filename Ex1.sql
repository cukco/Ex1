select p.category,sum(o.total_price) as total_sales,sum(o.quantity) as total_quantity from products p
inner join orders o on o.product_id=p.product_id
group by p.category
having sum(total_price)>2000
order by total_sales desc;
