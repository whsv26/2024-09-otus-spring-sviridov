import React, { useState, useEffect } from "react";
import {useNavigate, useParams} from 'react-router-dom';

const bookToForm = (book) => {
    return {
        title: book.title,
        authorId: book.author.id,
        genreIds: book.genres.map(genre => genre.id)
    }
}

const EditBook = (props) => {
    const params = useParams();
    const navigate = useNavigate();
    const goToHomePage = () => navigate('/');
    const { bookId } = params;
    const { genres, authors } = props;

    const [bookForm, setBookForm] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchBook = (id) => {
        fetch('/api/v1/books/' + id)
            .then(response => response.json())
            .then(book => setBookForm(bookToForm(book)))
            .catch(error => setError(error))
            .finally(() => setLoading(false));
    }

    useEffect(() => fetchBook(bookId), []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBookForm({
            ...bookForm,
            [name]: value,
        });
    };

    const handleMultiselectChange = (e) => {
        const { name, selectedOptions } = e.target;
        const options = [...selectedOptions];
        const values = options.map(option => option.value);
        setBookForm({
            ...bookForm,
            [name]: values,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await fetch("/api/v1/books/" + bookId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bookForm),
        }).then(() => navigate('/'));
    }

    return (
        <React.Fragment>
            <div>
                <h1>Edit book</h1>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="title">Title:</label>
                        <input
                            type="text"
                            id="title"
                            name="title"
                            value={bookForm.title}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="author">Author:</label>
                        <select
                            id="author"
                            name="authorId"
                            value={bookForm.authorId}
                            onChange={handleChange}
                            required
                        >
                            {authors.map((author) => (
                                <option key={author.id} value={author.id}>
                                    {author.fullName}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <label htmlFor="genres">Genres:</label>
                        <select
                            id="genres"
                            name="genreIds"
                            value={bookForm.genreIds}
                            onChange={handleMultiselectChange}
                            multiple={true}
                            required
                        >
                            {genres.map((genre) => (
                                <option key={genre.id} value={genre.id}>
                                    {genre.name}
                                </option>
                            ))}
                        </select>
                    </div>
                    <button onClick={goToHomePage}>Home</button>
                    <button type="submit">Save</button>
                </form>
            </div>
        </React.Fragment>
    )
}

export default EditBook;