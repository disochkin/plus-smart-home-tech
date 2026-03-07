CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	product_name VARCHAR(512) NOT NULL,
	description VARCHAR(7000),
	image_src VARCHAR(512),
	quantity_state VARCHAR(64) NOT NULL,
	product_state VARCHAR(64) NOT NULL,
	product_category VARCHAR(64) NOT NULL,
	price DECIMAL(19, 2)
);