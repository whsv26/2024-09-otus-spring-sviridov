import React, {ChangeEventHandler, FormEventHandler, useEffect, useState} from "react";
import {useNavigate, useParams} from 'react-router-dom';
import {useApi, useApiClient} from "./Api";

interface BookForm {
    title: string;
    authorId: number;
    genreIds: number[];
}

const bookToForm =
    (book: Book): BookForm => {
        return {
            title: book.title,
            authorId: book.author.id,
            genreIds: book.genres.map(genre => genre.id)
        }
    }

const parseParams =
    (bookId: any): Params => {
        if (!bookId) {
            throw new Error("Book ID must be provided");
        }
        return {
            bookId: parseInt(bookId)
        }
    }

interface Props {
    genres: Genre[];
    authors: Author[]
}

interface Params {
    bookId: number
}

const EditBook = (props: Props) => {
    const api = useApiClient();
    const params = useParams();
    const navigate = useNavigate();
    const goToHomePage = () => navigate('/');
    const { bookId } = parseParams(params.bookId);
    const { genres, authors } = props;

    const bookResult = useApi(() => api.findBook(bookId));

    const [bookForm, setBookForm] = useState<BookForm>({
        authorId: -1,
        genreIds: [],
        title: ""
    })

    useEffect(() => {
        if (bookResult.type === 'success') {
            setBookForm(bookToForm(bookResult.data));
        }
    }, [bookResult.type]);

    if (bookResult.type === 'loading') {
        return <div>Loading...</div>;
    }

    if (bookResult.type === 'failure') {
        return <div>Error: {bookResult.error.toString()}</div>;
    }

    const handleChange: ChangeEventHandler<HTMLInputElement|HTMLSelectElement> =
        (e) => {
            const { name, value } = e.target;
            setBookForm({
                ...bookForm,
                [name]: value,
            });
        };

    const handleMultiselectChange: ChangeEventHandler<HTMLSelectElement> =
        (e) => {
            const { name, selectedOptions } = e.target;
            const options = [...selectedOptions];
            const values = options.map(option => option.value);
            setBookForm({
                ...bookForm,
                [name]: values,
            });
        };

    const handleSubmit: FormEventHandler<HTMLFormElement> =
        async (e) => {
            e.preventDefault();
            await api.updateBook(bookId, bookForm)
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
                            value={bookForm.genreIds.map(genreId => genreId.toString())}
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