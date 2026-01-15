-- Seed initial data mapped to taxonomy
-- Clean existing seed data (optional, but good for idempotency if needed, 
-- though Flyway runs each migration only once)
DELETE FROM shop;
DELETE FROM merchant;

-- 2. Seed Merchant data (mapped to new taxonomy)
INSERT INTO merchant (category_id, name, description_eng) VALUES
    ((SELECT id FROM merchant_category WHERE name_eng = 'Smartphones & Tablets'), 'TechWorld', 'Premium gadgets and computers'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Computer Components'), 'GigaByte Store', 'Components and gaming gear'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Men''s Clothing'), 'Urban Style', 'Trendy clothes for young people'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Furniture'), 'HomeComfort', 'Modern furniture and decor'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Skincare'), 'Glow Up', 'Professional skincare and makeup');

-- 3. Shop data
-- Sample coordinates (London area)
INSERT INTO shop (merchant_id, name, city, street, latitude, longitude) VALUES
    ((SELECT id FROM merchant WHERE name = 'TechWorld'), 'TechWorld Flagship', 'London', 'Oxford Street, 100', 51.5145, -0.1506),
    ((SELECT id FROM merchant WHERE name = 'TechWorld'), 'TechWorld Express', 'London', 'Piccadilly, 50', 51.5097, -0.1345),
    ((SELECT id FROM merchant WHERE name = 'Urban Style'), 'Urban Style Sol', 'London', 'Regent Street, 20', 51.5117, -0.1417),
    ((SELECT id FROM merchant WHERE name = 'HomeComfort'), 'HomeComfort IKEA Area', 'London', 'Tottenham Court Road, 30', 51.5204, -0.1342),
    ((SELECT id FROM merchant WHERE name = 'Glow Up'), 'Glow Up Boutique', 'London', 'Bond Street, 15', 51.5122, -0.1444);
