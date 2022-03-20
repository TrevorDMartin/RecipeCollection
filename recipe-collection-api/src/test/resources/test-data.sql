BEGIN TRANSACTION;

DROP TABLE IF EXISTS recipe_ingredient, recipe, ingredient;

CREATE TABLE recipe (
	recipe_id serial,
	name varchar(40) UNIQUE NOT NULL,
	preparation text,
	CONSTRAINT PK_recipe PRIMARY KEY (recipe_id)
);

CREATE TABLE ingredient (
	ingredient_id serial,
	name varchar(40) UNIQUE NOT NULL,
	CONSTRAINT PK_ingredient PRIMARY KEY (ingredient_id)
);

CREATE TABLE recipe_ingredient (
    recipe_id int NOT NULL,
    ingredient_id int NOT NULL,
    unit varchar(30),
    amount NUMERIC(5,2),
	preparation varchar(100),
	CONSTRAINT PK_recipe_ingredient PRIMARY KEY (recipe_id, ingredient_id),
	CONSTRAINT FK_recipe_ingredient_recipe FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id),
	CONSTRAINT FK_recipe_ingredient_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id)
);


INSERT INTO recipe (name, preparation) VALUES ('Blue Cheese Dressing', E'1. Whisk rice wine vinegar and sugar to dissolve.\n' ||
        E'2. Combine with blue cheese and mayo.\n3. Add salt and pepper to taste.');

INSERT INTO ingredient (name) VALUES
('Blue Cheese'),
('Mayo'),
('Rice Wine Vinegar'),
('Sugar'),
('Salt'),
('Pepper');

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit, amount, preparation) VALUES
(1, 1,'Quart', 1, 'Crumble the blue cheese by hand.');

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit, amount) VALUES
(1, 2, 'Quart', 2),
(1, 3, 'Cup', 1),
(1, 4, 'Cup', 0.25);

INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES
(1, 5),
(1, 6);


INSERT INTO recipe (name, preparation) VALUES
('Cheese Sauce', E'1. Sweat peppers and onions.\n2. Add half & half and jalapenos.\n3. Bring to a very low simmer.\n' ||
 E'4. Add cheese in batches till fully incorporated.\n5. Cook very low until sauce reaches 145 degrees.');


INSERT INTO ingredient (name) VALUES
('Pepperjack Cheese'),
('Half & Half'),
('Red Bell Pepper'),
('Yellow Onion'),
('Jalapeno');


INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit, amount, preparation) VALUES
(2, 7, 'Logs', 2, 'Shred Cheese using food processor attachment.'),
(2, 9, 'Each', 4, 'Small diced.'),
(2, 10, 'Each', 2, 'Small diced.'),
(2, 11, 'Each', 2, 'Roasted, peeled and small diced.');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit, amount) VALUES
(2, 8, 'Quart', 3);


COMMIT;
