import http from '../http-common';

const getAllAuthors = () => http.get('/authors');

export default { getAllAuthors };