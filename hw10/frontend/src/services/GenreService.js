import http from '../http-common.js';

const getAllGenres = () => http.get('/genres');

export default { getAllGenres }