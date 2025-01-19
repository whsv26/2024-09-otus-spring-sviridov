import React from 'react';
import {Routes, Route, BrowserRouter} from 'react-router-dom';
import {useApi, findAllAuthors, findAllGenres} from './Api'

import ListBooks from './ListBooks';
import EditBook from "./EditBook";
import CreateBook from "./CreateBook";

const App = () => {
    const [authorsLoading, authorsError, authors] = useApi(findAllAuthors);
    const [genresLoading, genresError, genres] = useApi(findAllGenres);

    if (authorsLoading || genresLoading) {
        return <div>Loading...</div>;
    }

    if (authorsError || genresError) {
        return <div>Error: {authorsError || genresError}</div>;
    }

    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<ListBooks />} />
            <Route path="/edit/:bookId" element={<EditBook genres={genres} authors={authors}/>} />
            <Route path="/create" element={<CreateBook genres={genres} authors={authors}/>} />
        </Routes>
    </BrowserRouter>;
}

export default App;
