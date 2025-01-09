import React, { useState } from "react";
import {useNavigate} from 'react-router-dom';
import {createBook} from './Api'

const BookEditing = (props) => {
    const navigate = useNavigate();
    const goToHomePage = () => navigate('/');
    const { genres, authors } = props;

    const [bookForm, setBookForm] = useState({
        title: '',
        authorId: authors[0].id,
        genreIds: [],
    });

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
        await createBook(bookForm)
            .then(() => navigate('/'));
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
                            multiple
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

export default BookEditing;