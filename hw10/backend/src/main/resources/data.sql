INSERT INTO authors (full_name)
VALUES
    ('Harper Lee'),
    ('George Orwell'),
    ('Jane Austen'),
    ('F. Scott Fitzgerald'),
    ('Herman Melville'),
    ('Leo Tolstoy'),
    ('J.D. Salinger'),
    ('J.R.R. Tolkien'),
    ('Fyodor Dostoevsky');


INSERT INTO genres (name)
VALUES
    ('Fiction'),
    ('Classic'),
    ('Dystopian'),
    ('Science Fiction'),
    ('Romance'),
    ('Adventure'),
    ('Historical Fiction'),
    ('Young Adult'),
    ('Fantasy'),
    ('Psychological Fiction');

INSERT INTO books (title, author_id)
VALUES
    ('To Kill a Mockingbird', 1),
    ('1984', 2),
    ('Pride and Prejudice', 3),
    ('The Great Gatsby', 4),
    ('Moby-Dick', 5),
    ('War and Peace', 6),
    ('The Catcher in the Rye', 7),
    ('The Hobbit', 8),
    ('Crime and Punishment', 9);


INSERT INTO books_genres (book_id, genre_id)
VALUES
    (1, 1), (1, 2),      -- To Kill a Mockingbird
    (2, 3), (2, 4),      -- 1984
    (3, 5), (3, 2),      -- Pride and Prejudice
    (4, 1), (4, 2),      -- The Great Gatsby
    (5, 6), (5, 2),      -- Moby-Dick
    (6, 7), (6, 2),      -- War and Peace
    (7, 1), (7, 8),      -- The Catcher in the Rye
    (8, 9), (8, 6),      -- The Hobbit
    (9, 10), (9, 2);     -- Crime and Punishment


INSERT INTO comments (message, book_id)
VALUES
-- Comments for "To Kill a Mockingbird" (book_id = 1)
('A timeless classic!', 1),
('Loved the characters and the storyline.', 1),

-- Comments for "1984" (book_id = 2)
('A chilling dystopian novel.', 2),
('Orwell''s vision is both terrifying and thought-provoking.', 2),

-- Comments for "Pride and Prejudice" (book_id = 3)
('An engaging romance with witty dialogue.', 3),
('Austen''s best work!', 3),

-- Comments for "The Great Gatsby" (book_id = 4)
('A tragic tale of love and loss.', 4),
('Fitzgerald captures the essence of the Roaring Twenties.', 4),

-- Comments for "Moby-Dick" (book_id = 5)
('A thrilling adventure on the high seas.', 5),
('Melville''s writing is both poetic and powerful.', 5),

-- Comments for "War and Peace" (book_id = 6)
('An epic tale of love and war.', 6),
('Tolstoy''s masterpiece.', 6),

-- Comments for "The Catcher in the Rye" (book_id = 7)
('Holden Caulfield is a fascinating character.', 7),
('A must-read for any teenager.', 7),

-- Comments for "The Hobbit" (book_id = 8)
('A wonderful fantasy adventure.', 8),
('Tolkien''s world-building is unmatched.', 8),

-- Comments for "Crime and Punishment" (book_id = 9)
('A deep psychological exploration.', 9),
('Dostoevsky''s writing is intense and gripping.', 9);

