-- BetterChoice AI — Sample product catalog for Phase 1 development

-- Laptops
INSERT INTO products (category_id, name, slug, description, base_price, currency, image_url, brand, rating_avg, review_count, metadata)
SELECT c.id,
       'HP Victus Gaming Laptop',
       'hp-victus-gaming-laptop',
       'AMD Ryzen 5 5600H, 8GB RAM, 512GB SSD, NVIDIA RTX 3050 — ideal for gaming and coding on a budget.',
       57990.00, 'INR',
       'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400',
       'HP', 4.40, 5500,
       '{"processor":"Ryzen 5 5600H","ram":"8GB","storage":"512GB SSD","gpu":"RTX 3050","screen":"15.6 inch FHD"}'::jsonb
FROM categories c WHERE c.slug = 'laptops';

INSERT INTO products (category_id, name, slug, description, base_price, currency, image_url, brand, rating_avg, review_count, metadata)
SELECT c.id,
       'Dell G15 Gaming Laptop',
       'dell-g15-gaming-laptop',
       'Intel Core i5 12th Gen, 8GB RAM, 512GB SSD, RTX 3050 — reliable performance for everyday gaming.',
       62499.00, 'INR',
       'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=400',
       'Dell', 4.20, 3200,
       '{"processor":"Intel Core i5 12th Gen","ram":"8GB","storage":"512GB SSD","gpu":"RTX 3050","screen":"15.6 inch FHD"}'::jsonb
FROM categories c WHERE c.slug = 'laptops';

INSERT INTO products (category_id, name, slug, description, base_price, currency, image_url, brand, rating_avg, review_count, metadata)
SELECT c.id,
       'Lenovo LOQ Gaming Laptop',
       'lenovo-loq-gaming-laptop',
       'AMD Ryzen 7, 16GB RAM, 512GB SSD, RTX 4050 — strong performance per rupee.',
       72990.00, 'INR',
       'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=400',
       'Lenovo', 4.50, 4100,
       '{"processor":"Ryzen 7","ram":"16GB","storage":"512GB SSD","gpu":"RTX 4050","screen":"15.6 inch FHD"}'::jsonb
FROM categories c WHERE c.slug = 'laptops';

INSERT INTO products (category_id, name, slug, description, base_price, currency, image_url, brand, rating_avg, review_count, metadata)
SELECT c.id,
       'ASUS TUF Gaming A15',
       'asus-tuf-gaming-a15',
       'Ryzen 5, 16GB RAM, 512GB SSD, RTX 3050 — military-grade durability with solid thermals.',
       64990.00, 'INR',
       'https://images.unsplash.com/photo-1525547719575-a1d4b8549b6b?w=400',
       'ASUS', 4.30, 2800,
       '{"processor":"Ryzen 5","ram":"16GB","storage":"512GB SSD","gpu":"RTX 3050","screen":"15.6 inch FHD"}'::jsonb
FROM categories c WHERE c.slug = 'laptops';

INSERT INTO products (category_id, name, slug, description, base_price, currency, image_url, brand, rating_avg, review_count, metadata)
SELECT c.id,
       'Apple MacBook Air M2',
       'apple-macbook-air-m2',
       'Apple M2 chip, 8GB RAM, 256GB SSD — ultra-portable premium laptop for creators.',
       99900.00, 'INR',
       'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400',
       'Apple', 4.70, 8900,
       '{"processor":"Apple M2","ram":"8GB","storage":"256GB SSD","gpu":"Integrated","screen":"13.6 inch Retina"}'::jsonb
FROM categories c WHERE c.slug = 'laptops';

INSERT INTO products (category_id, name, slug, description, base_price, currency, image_url, brand, rating_avg, review_count, metadata)
SELECT c.id,
       'Acer Aspire 5',
       'acer-aspire-5',
       'Intel Core i3, 8GB RAM, 512GB SSD — affordable everyday laptop for students.',
       38990.00, 'INR',
       'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400',
       'Acer', 4.10, 1900,
       '{"processor":"Intel Core i3","ram":"8GB","storage":"512GB SSD","gpu":"Integrated","screen":"15.6 inch FHD"}'::jsonb
FROM categories c WHERE c.slug = 'laptops';

-- Product attributes for HP Victus
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, display_order)
SELECT p.id, 'Processor', 'AMD Ryzen 5 5600H', 0 FROM products p WHERE p.slug = 'hp-victus-gaming-laptop';
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, display_order)
SELECT p.id, 'RAM', '8 GB', 1 FROM products p WHERE p.slug = 'hp-victus-gaming-laptop';
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, display_order)
SELECT p.id, 'Storage', '512 GB SSD', 2 FROM products p WHERE p.slug = 'hp-victus-gaming-laptop';
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, display_order)
SELECT p.id, 'GPU', 'NVIDIA RTX 3050', 3 FROM products p WHERE p.slug = 'hp-victus-gaming-laptop';

-- Multi-platform sources for HP Victus
INSERT INTO product_sources (product_id, platform, external_id, url, price, last_synced_at)
SELECT p.id, 'AMAZON', 'amz-hp-victus-001', 'https://amazon.in/dp/B0EXAMPLE1', 57990.00, NOW()
FROM products p WHERE p.slug = 'hp-victus-gaming-laptop';

INSERT INTO product_sources (product_id, platform, external_id, url, price, last_synced_at)
SELECT p.id, 'FLIPKART', 'fk-hp-victus-001', 'https://flipkart.com/hp-victus', 58499.00, NOW()
FROM products p WHERE p.slug = 'hp-victus-gaming-laptop';

INSERT INTO product_sources (product_id, platform, external_id, url, price, last_synced_at)
SELECT p.id, 'CROMA', 'croma-hp-victus-001', 'https://croma.com/hp-victus', 58990.00, NOW()
FROM products p WHERE p.slug = 'hp-victus-gaming-laptop';

-- Sources for Dell G15
INSERT INTO product_sources (product_id, platform, external_id, url, price, last_synced_at)
SELECT p.id, 'AMAZON', 'amz-dell-g15-001', 'https://amazon.in/dp/B0EXAMPLE2', 62499.00, NOW()
FROM products p WHERE p.slug = 'dell-g15-gaming-laptop';

INSERT INTO product_sources (product_id, platform, external_id, url, price, last_synced_at)
SELECT p.id, 'FLIPKART', 'fk-dell-g15-001', 'https://flipkart.com/dell-g15', 61999.00, NOW()
FROM products p WHERE p.slug = 'dell-g15-gaming-laptop';

-- Sources for Lenovo LOQ
INSERT INTO product_sources (product_id, platform, external_id, url, price, last_synced_at)
SELECT p.id, 'AMAZON', 'amz-lenovo-loq-001', 'https://amazon.in/dp/B0EXAMPLE3', 72990.00, NOW()
FROM products p WHERE p.slug = 'lenovo-loq-gaming-laptop';

INSERT INTO product_sources (product_id, platform, external_id, url, price, last_synced_at)
SELECT p.id, 'RELIANCE_DIGITAL', 'rd-lenovo-loq-001', 'https://reliancedigital.in/lenovo-loq', 72499.00, NOW()
FROM products p WHERE p.slug = 'lenovo-loq-gaming-laptop';
