import React from 'react';
import {Routes, Route, BrowserRouter} from 'react-router-dom';
import {useApi, useApiClient} from './Api'

import ListBooks from './ListBooks';
import EditBook from "./EditBook";
import CreateBook from "./CreateBook";
import SignIn from "./SignIn";

const App = () => {
    const api = useApiClient();
    const authorsResult = useApi(api.findAllAuthors);
    const genresResult = useApi(api.findAllGenres);

    if (authorsResult.type === 'loading' || genresResult.type === 'loading') {
        return <div>Loading...</div>;
    }

    if (authorsResult.type === 'failure') {
        return <div>Error: {authorsResult.error.toString()}</div>;
    } else if (genresResult.type === 'failure') {
        return <div>Error: {genresResult.error.toString()}</div>;
    }

    const authors = authorsResult.data
    const genres = genresResult.data

    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<ListBooks />}/>
            <Route path="/login" element={<SignIn />}/>
            <Route path="/create" element={<CreateBook genres={genres} authors={authors} />} />
            <Route path="/edit/:bookId" element={<EditBook genres={genres} authors={authors} />}/>
        </Routes>
    </BrowserRouter>;
}

export default App;
