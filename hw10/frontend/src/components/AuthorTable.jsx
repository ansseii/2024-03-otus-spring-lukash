import { useContext, useEffect } from "react";
import { Container, Row, Table } from "react-bootstrap";
import { StateContext } from "../context/AppContext";

const AuthorTable = () => {
    const { authors: { authors, loadAuthors } } = useContext(StateContext);

    useEffect(() => {
        loadAuthors().catch((error) => console.log(error));
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
                    {authors.map(({ id, fullName }) => <tr key={id}>
                        <td>{id}</td>
                        <td>{fullName}</td>
                    </tr>)}
                    </tbody>
                </Table>
            </Row>
        </Container>
    );
};

export default AuthorTable;