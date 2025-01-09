import {useEffect, useState} from "react";

export const useApi = (request) => {
    const [response, setResponse] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        request()
            .then(response => setResponse(response))
            .catch(error => setError(error))
            .finally(() => setLoading(false));
    }, []);

    return [loading, error, response];
}

export const findAllAuthors = () => {
    return fetch('/api/v1/authors')
        .then(response => response.json());
}

export const findAllGenres = () => {
    return fetch('/api/v1/genres')
        .then(response => response.json());
}

export const findAllBooks = () => {
    return fetch('/api/v1/books')
        .then(response => response.json());
}

export const findBook = (bookId) => {
    return fetch('/api/v1/books/' + bookId)
        .then(response => response.json());
}

export const createBook = (book) => {
    return fetch("/api/v1/books", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(book),
    }).then(response => response.json());
}

export const updateBook = (bookId, book) => {
    return fetch("/api/v1/books/" + bookId, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(book),
    }).then(response => response.json());
}

export const deleteBook = (bookId) => {
    return fetch("/api/v1/books/" + bookId, { method: "DELETE" });
}