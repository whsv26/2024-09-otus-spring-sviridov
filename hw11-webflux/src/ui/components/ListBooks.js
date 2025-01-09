import React, {useEffect, useState} from 'react'
import { useNavigate } from 'react-router-dom';
import {useApi, findAllBooks, deleteBook} from "./Api";

const ListBooks = () => {
    const navigate = useNavigate();
    const goToEditPage = (bookId) => navigate('/edit/' + bookId);
    const goToCreatePage = () => navigate('/create');

    const [loading, error, initialBooks] = useApi(findAllBooks);
    const [books, setBooks] = useState([]);

    useEffect(() => {
        if (initialBooks) {
            setBooks(initialBooks);
        }
    }, [initialBooks]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    const handleDeleteBook = async (bookId) => {
        deleteBook(bookId).then(() => {
            setBooks(books.filter((book) => book.id !== bookId));
        })
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
                                    <button onClick={() => handleDeleteBook(book.id)}>Delete</button>
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
