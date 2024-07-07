import http from '../http-common';

const getAllBooks = () => http.get(`/books`);

const addBook = (data) => http.post(`/books`, data);

const editBook = (id, body) => http.put(`/books/${id}`, body);

const deleteBook = (id) => http.delete(`/books/${id}`);

export default { getAllBooks, addBook, editBook, deleteBook }