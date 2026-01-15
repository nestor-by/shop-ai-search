-- Taxonomy update for new schema
ALTER TABLE merchant_category ADD COLUMN parent_id BIGINT;
ALTER TABLE merchant_category ADD CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES merchant_category(id);

-- 100 Popular Categories Taxonomy
-- Level 1: Electronics & Computers
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Electronics', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Smartphones & Tablets', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Laptops & Computers', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Audio & Headphones', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Cameras & Photography', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Video Games & Consoles', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Wearable Technology', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Home Theater & TV', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Computer Components', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Drones & Robotics', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics')),
('Office Electronics', (SELECT id FROM merchant_category WHERE name_eng = 'Electronics'));

-- Level 1: Fashion & Apparel
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Fashion', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Men''s Clothing', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Women''s Clothing', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Children''s Clothing', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Shoes & Footwear', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Handbags & Accessories', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Jewelry', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Watches', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Sportswear', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Luxury Fashion', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion')),
('Underwear & Sleepwear', (SELECT id FROM merchant_category WHERE name_eng = 'Fashion'));

-- Level 1: Home & Garden
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Home & Garden', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Furniture', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Home Decor', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Kitchen & Dining', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Bedding & Bath', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Appliances', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Garden & Outdoor', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Tools & DIY', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Smart Home', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Lighting', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden')),
('Storage & Organization', (SELECT id FROM merchant_category WHERE name_eng = 'Home & Garden'));

-- Level 1: Beauty & Health
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Beauty & Health', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Skincare', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Makeup', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Hair Care', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Fragrances', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Personal Care', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Vitamins & Supplements', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Medical Supplies', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Fitness Equipment', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Optical & Eyewear', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health')),
('Salon & Spa Services', (SELECT id FROM merchant_category WHERE name_eng = 'Beauty & Health'));

-- Level 1: Sports & Outdoors
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Sports & Outdoors', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Camping & Hiking', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Cycling', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Water Sports', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Winter Sports', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Team Sports', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Fishing & Hunting', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Yoga & Pilates', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Running', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Golf', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors')),
('Exercise Machines', (SELECT id FROM merchant_category WHERE name_eng = 'Sports & Outdoors'));

-- Level 1: Travel & Leisure
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Travel', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Hotels & Accommodation', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Flights', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Car Rentals', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Tour Packages', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Luggage & Travel Gear', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Cruises', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Attractions & Events', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Transportation', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Travel Insurance', (SELECT id FROM merchant_category WHERE name_eng = 'Travel')),
('Camping Sites', (SELECT id FROM merchant_category WHERE name_eng = 'Travel'));

-- Level 1: Automotive
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Automotive', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Auto Parts', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Tires & Wheels', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Car Accessories', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Motorcycles & ATVs', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Maintenance & Repair', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Car Audio & GPS', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Cleaning & Care', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Oils & Fluids', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Tools & Equipment', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive')),
('Electric Vehicle Accessories', (SELECT id FROM merchant_category WHERE name_eng = 'Automotive'));

-- Level 1: Toys & Hobbies
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Toys & Hobbies', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Action Figures', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Board Games & Puzzles', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Dolls & Stuffed Toys', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Educational Toys', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Remote Control Toys', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Musical Instruments', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Art & Craft Supplies', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Collectibles', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Model Building', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies')),
('Party Supplies', (SELECT id FROM merchant_category WHERE name_eng = 'Toys & Hobbies'));

-- Level 1: Education & Services
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Services & Education', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Online Courses', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Books & eBooks', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Software & Subscriptions', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Professional Services', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Insurance Services', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Financial Services', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Photography Services', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Event Planning', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Tutoring', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education')),
('Legal Services', (SELECT id FROM merchant_category WHERE name_eng = 'Services & Education'));

-- Level 1: Groceries & Food
INSERT INTO merchant_category (name_eng, parent_id) VALUES ('Food & Groceries', NULL);
INSERT INTO merchant_category (name_eng, parent_id) VALUES 
('Supermarkets', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Beverages', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Gourmet Food', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Health Food', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Pet Food', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Wine & Spirits', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Meal Kits', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Coffee & Tea', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Sweets & Snacks', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries')),
('Organic Produce', (SELECT id FROM merchant_category WHERE name_eng = 'Food & Groceries'));
