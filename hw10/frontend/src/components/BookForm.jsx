import PropTypes from 'prop-types';
import { useContext, useState } from "react";
import { Col, Container, Form, Row } from "react-bootstrap";
import Select from "react-select";
import { StateContext } from "../context/AppContext";

const fields = {
    title: "title",
    author: "authorId",
    genreIds: "genreIds",
}

const BookForm = ({ id, state, onSubmit, errorsState }) => {
    const [ book, setBook ] = state;
    const [ errors, setErrors ] = errorsState;

    const {
        genres: { genres },
        authors: { authors },
    } = useContext(StateContext);

    const [ selectedGenres, setSelectedGenres ] = useState(
        genres.map(({ id, name }) => ({ value: id, label: name })).filter(({ value }) => book.genres.includes(value)),
    );

    const handleSingleInput = (event) => {
        const { name, value } = event.target;
        setErrors(errors.filter(({ field }) => field !== fields[name]));
        setBook({ ...book, [name]: value });
    };

    const handleGenresSelect = (selected, { name }) => {
        const updated = Array.from(selected, item => item.value);
        setSelectedGenres(selected);
        setErrors(errors.filter(({ field }) => field !== fields[name]));
        setBook({ ...book, ["genres"]: updated });
    };

    return (
        <Form id={id} onSubmit={onSubmit} noValidate>
            <Container>
                <Row>
                    <Col>
                        <Form.Group className="mb-3">
                            <Form.Label>Title</Form.Label>
                            <Form.Control type="text" placeholder="Title" defaultValue={book.title}
                                          name="title" onChange={handleSingleInput}
                                          isInvalid={errors.find(({ field }) => field === 'title')} />
                            <Form.Control.Feedback type="invalid">{
                                errors.find(({ field }) => field === 'title')?.['defaultMessage']
                            }</Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="align-items-end gx-3">
                    <Col>
                        <Form.Group>
                            <Form.Label>Author</Form.Label>
                            <Form.Select defaultValue={book.author} onChange={handleSingleInput}
                                         name="author"
                                         isInvalid={errors.find(({ field }) => field === 'authorId')}>
                                {!book.author ? <option value="">Select an author</option> : null}
                                {authors.map(({ id, fullName }) =>
                                    <option key={id} value={id}>{fullName}</option>)}
                            </Form.Select>
                            <Form.Control.Feedback type="invalid">{
                                errors.find(({ field }) => field === 'authorId')?.['defaultMessage']
                            }</Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="align-items-end gx-3">
                    <Col>
                        <Form.Group>
                            <Form.Label>Genres</Form.Label>
                            <Select isMulti
                                    options={genres.map(({ id, name }) => ({ value: id, label: name }))}
                                    onChange={handleGenresSelect}
                                    value={selectedGenres}
                                    name="genreIds"
                                    isOptionDisabled={() => selectedGenres.length >= 4}
                            />
                        </Form.Group>
                    </Col>
                    {
                        errors.find(({ field }) => field === 'genreIds') ?
                            <div className="invalid-feedback" style={{ display: "block" }}>
                                {errors.find(({ field }) => field === 'genreIds')?.['defaultMessage']}
                            </div> : null
                    }
                </Row>
            </Container>
        </Form>
    );
};

BookForm.propTypes = {
    id: PropTypes.string.isRequired,
    state: PropTypes.array.isRequired,
    onSubmit: PropTypes.func.isRequired,
}

export default BookForm;