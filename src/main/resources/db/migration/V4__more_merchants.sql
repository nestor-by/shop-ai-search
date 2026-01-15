-- Add 10 more merchants with various categories
INSERT INTO merchant (category_id, name, description_eng) VALUES
    ((SELECT id FROM merchant_category WHERE name_eng = 'Laptops & Computers'), 'Nano Systems', 'High-performance laptops and workstations'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Video Games & Consoles'), 'Pixel Paradise', 'Video games consoles and accessories'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Women''s Clothing'), 'Velvet Vogue', 'Elegant women fashion and accessories'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Shoes & Footwear'), 'Step Ahead', 'Quality footwear for all occasions'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Appliances'), 'Kitchen Pro', 'Modern kitchen appliances and tools'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Fitness Equipment'), 'Iron Pulse', 'Gym equipment and fitness gear'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Cycling'), 'Swift Gears', 'Bicycles, parts and professional gear'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Hotels & Accommodation'), 'Azure Stays', 'Luxury hotel bookings and vacation rent'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Musical Instruments'), 'Harmony Hub', 'Musical instruments for beginners and pros'),
    ((SELECT id FROM merchant_category WHERE name_eng = 'Supermarkets'), 'Fresh Market', 'Organic groceries and daily essentials');

-- Add shops for the new merchants
INSERT INTO shop (merchant_id, name, city, street, latitude, longitude) VALUES
    ((SELECT id FROM merchant WHERE name = 'Nano Systems'), 'Nano Systems Hub', 'London', 'Baker Street, 221B', 51.5237, -0.1585),
    ((SELECT id FROM merchant WHERE name = 'Pixel Paradise'), 'Pixel Paradise Game Store', 'London', 'Carnaby Street, 10', 51.5121, -0.1394),
    ((SELECT id FROM merchant WHERE name = 'Velvet Vogue'), 'Velvet Vogue Boutique', 'London', 'Sloane Street, 50', 51.4938, -0.1601),
    ((SELECT id FROM merchant WHERE name = 'Step Ahead'), 'Step Ahead Shoes', 'London', 'King''s Road, 150', 51.4886, -0.1678),
    ((SELECT id FROM merchant WHERE name = 'Kitchen Pro'), 'Kitchen Pro Showroom', 'London', 'Charing Cross Road, 80', 51.5133, -0.1287),
    ((SELECT id FROM merchant WHERE name = 'Iron Pulse'), 'Iron Pulse Fitness', 'London', 'Fleet Street, 120', 51.5139, -0.1077),
    ((SELECT id FROM merchant WHERE name = 'Swift Gears'), 'Swift Gears Cycling', 'London', 'The Mall, 1', 51.5039, -0.1353),
    ((SELECT id FROM merchant WHERE name = 'Azure Stays'), 'Azure Stays Office', 'London', 'Victoria Street, 200', 51.4975, -0.1415),
    ((SELECT id FROM merchant WHERE name = 'Harmony Hub'), 'Harmony Hub Music', 'London', 'Abbey Road, 3', 51.5367, -0.1774),
    ((SELECT id FROM merchant WHERE name = 'Fresh Market'), 'Fresh Market Sol', 'London', 'Portobello Road, 100', 51.5173, -0.2057);
