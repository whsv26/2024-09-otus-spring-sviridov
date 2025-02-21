import React, {useEffect, useState} from 'react'
import { useNavigate } from 'react-router-dom';
import {useApi, useApiClient} from "./Api";

const ListBooks = () => {
    const api = useApiClient();
    const navigate = useNavigate();
    const goToEditPage = (bookId: number) => navigate('/edit/' + bookId);
    const goToCreatePage = () => navigate('/create');
    const goToLoginPage = () => navigate('/login');

    const booksResult = useApi(api.findAllBooks);
    const [books, setBooks] = useState<Book[]>([]);

    useEffect(() => {
        if (booksResult.type === 'success') {
            setBooks(booksResult.data);
        }
    }, [booksResult.type]);

    if (booksResult.type === 'loading') {
        return <div>Loading...</div>;
    }

    if (booksResult.type === 'failure') {
        return <div>Error: {booksResult.error.toString()}</div>;
    }

    const handleDeleteBook = async (bookId: number) => {
        api.deleteBook(bookId).then(() => {
            setBooks(books.filter((book) => book.id !== bookId));
        })
    }

    return (
        <React.Fragment>
            <div>
                <h1>Books
                    <button onClick={goToLoginPage}>SignIn</button>
                    <button onClick={goToCreatePage}>Create</button>
                </h1>
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
