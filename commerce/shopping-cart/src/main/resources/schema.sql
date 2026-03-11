CREATE TABLE IF NOT EXISTS shopping_cart (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	username VARCHAR(512) NOT NULL,
	is_active BOOLEAN,
	CONSTRAINT uq_username UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS shopping_cart_items (
    shopping_cart_id UUID NOT NULL REFERENCES shopping_cart(uuid) ON DELETE CASCADE,
    product_id UUID NOT NULL,
	quantity INTEGER DEFAULT 0,
	PRIMARY KEY (shopping_cart_id, product_id)
);