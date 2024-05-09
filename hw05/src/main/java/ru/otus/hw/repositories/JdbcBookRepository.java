package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        var query = """
                select b.id as book_id, b.title as book_title,
                a.id as author_id, a.full_name as author_full_name,
                g.id as genre_id, g.name as genre_name
                from books b
                join authors a on a.id = b.author_id
                left join books_genres bg on bg.book_id = b.id
                left join genres g on g.id = bg.genre_id
                where b.id = :id
                """;
        var book = jdbcTemplate.query(query, new MapSqlParameterSource("id", id), new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from books where id = :id", new MapSqlParameterSource("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        var query = """
                select b.id as book_id, b.title as book_title, a.id as author_id, a.full_name as author_full_name
                from books b
                join authors a on a.id = b.author_id
                """;
        return jdbcTemplate.query(query, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcTemplate.query(
                "select book_id, genre_id from books_genres",
                (rs, rowNum) -> new BookGenreRelation(
                        rs.getLong("book_id"),
                        rs.getLong("genre_id")
                )
        );
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        var booksMap = booksWithoutGenres.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        var genresMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));

        relations.forEach(relation -> {
            var book = booksMap.get(relation.bookId());
            var genre = genresMap.get(relation.genreId());
            book.getGenres().add(genre);
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var query = "insert into books(title, author_id) values(:title, :author_id)";
        var params = new MapSqlParameterSource(Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId()
        ));
        jdbcTemplate.update(query, params, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("id", book.getId());

        int rowsUpdated = jdbcTemplate.update(
                "update books set author_id = :authorId, title = :title WHERE id=:id",
                params);

        if (rowsUpdated == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        var batchArgs = book.getGenres().stream()
                .map(g -> new MapSqlParameterSource()
                        .addValue("bookId", book.getId())
                        .addValue("genreId", g.getId()))
                .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(
                "insert into books_genres (book_id, genre_id) values (:bookId, :genreId)",
                batchArgs
        );
    }

    private void removeGenresRelationsFor(Book book) {
        jdbcTemplate.update(
                "delete from books_genres where book_id = :id",
                new MapSqlParameterSource("id", book.getId())
        );
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong("book_id"),
                    rs.getString("book_title"),
                    new Author(
                            rs.getLong("author_id"),
                            rs.getString("author_full_name")
                    ),
                    new ArrayList<>()
            );
        }
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            var genres = new ArrayList<Genre>();
            while (rs.next()) {
                book = new BookRowMapper().mapRow(rs, 0);
                genres.add(new Genre(
                        rs.getLong("genre_id"),
                        rs.getString("genre_name")
                ));
            }

            if (book != null) {
                book.getGenres().addAll(genres);
            }

            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
