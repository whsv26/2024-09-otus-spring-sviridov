import React, {useEffect, useState} from 'react'
import { useNavigate } from 'react-router-dom';

const ListBooks = () => {

    const navigate = useNavigate();
    const goToEditPage = (bookId) => navigate('/edit/' + bookId);
    const goToCreatePage = () => navigate('/create');

    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchBooks = () => {
        fetch('/api/v1/books')
            .then(response => response.json())
            .then(books => setBooks(books))
            .catch(error => setError(error))
            .finally(() => setLoading(false));
    }

    useEffect(fetchBooks, []);

    const deleteBook = async (bookId) => {
        await fetch("/api/v1/books/" + bookId, { method: "DELETE" });
        setBooks(books.filter((book) => book.id !== bookId));
    }

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <React.Fragment>
            <div>
                <h1>Books <button onClick={goToCreatePage}>+</button></h1>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Genres</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        books.map((book, i) => (
                            <tr key={i}>
                                <td>{book.id}</td>
                                <td>{book.title}</td>
                                <td>{book.author.fullName}</td>
                                <td>{book.genres.map(genre => genre.name).join(", ")}</td>
                                <td>
                                    <button onClick={() => goToEditPage(book.id)}>Edit</button>
                                </td>
                                <td>
                                    <button onClick={() => deleteBook(book.id)}>Delete</button>
                                </td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </div>
        </React.Fragment>
    )
}

export default ListBooks;
