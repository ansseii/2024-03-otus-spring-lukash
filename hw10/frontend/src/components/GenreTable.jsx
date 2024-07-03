import { useContext, useEffect } from "react";
import { Container, Row, Table } from "react-bootstrap";
import { StateContext } from "../context/AppContext";

const GenreTable = () => {
    const { genres: { genres, loadGenres } } = useContext(StateContext);

    useEffect(() => {
        loadGenres().catch((error) => console.log(error));
    }, []);

    return (
        <Container>
            <Row>
                <Table striped bordered hover>
                    <thead>
                    <tr className="text-center">
                        <th scope="col">Id</th>
                        <th scope="col">Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {genres.map((genre) => <tr key={genre.id}>
                        <td>{genre.id}</td>
                        <td>{genre.name}</td>
                    </tr>)}
                    </tbody>
                </Table>
            </Row>
        </Container>
    );
};

export default GenreTable;