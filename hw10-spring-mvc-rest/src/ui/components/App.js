import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Routes, Route, BrowserRouter} from 'react-router-dom';

import ListBooks from './ListBooks';
import EditBook from "./EditBook";
import CreateBook from "./CreateBook";

const fetchAuthors = () => {
    const [authors, setAuthors] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch('/api/v1/authors')
            .then(response => response.json())
            .then(authors => setAuthors(authors))
            .catch(error => setError(error))
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return <div>Loading Authors...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return authors;
}

const fetchGenres = () => {
    const [genres, setGenres] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch('/api/v1/genres')
            .then(response => response.json())
            .then(genres => setGenres(genres))
            .catch(error => setError(error))
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return <div>Loading Genres...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return genres;
}

const App = () => {
    const authors = fetchAuthors();
    const genres = fetchGenres();

    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<ListBooks />} />
            <Route path="/edit/:bookId" element={<EditBook genres={genres} authors={authors}/>} />
            <Route path="/create" element={<CreateBook genres={genres} authors={authors}/>} />
        </Routes>
    </BrowserRouter>;
}

export default App;
