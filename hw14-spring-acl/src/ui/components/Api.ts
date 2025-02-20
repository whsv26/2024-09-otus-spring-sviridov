import {useEffect, useState} from "react";
import {useAuth} from "./AuthProvider";

interface ApiError {
    errorCode: string;
    errorMessage: string;
    errorDetails: Record<string, any>;
}

type Token = string

type ApiResult<T> =
    | { type: "loading" }
    | { type: "failure", error: Error|ApiError }
    | { type: "success", data: T };

export const useApi =
    <T> (request: () => Promise<T>): ApiResult<T> => {
        const [response, setResponse] = useState<T|null>(null);
        const [error, setError] = useState<Error|ApiError|null>(null);

        useEffect(() => {
            request()
                .then(response => setResponse(response))
                .catch(error => setError(error))
        }, []);

        if (response) {
            return { type: "success", data: response };
        } else if (error) {
            return { type: "failure", error };
        } else {
            return { type: "loading" };
        }
    }

const parseResponse: <T> (response: Response) => Promise<T> =
    async (response: Response)=> {
        const body = await response.json();
        if (!response.ok) {
            throw body;
        }
        return body;
    }

interface UpsertBookRequest {
    title: string;
    authorId: number;
    genreIds: number[];
}
type CreateBookRequest = UpsertBookRequest
type UpdateBookRequest = UpsertBookRequest

interface TokenResponse { token: Token }

export interface Api {
    login(username: string, password: string): Promise<Token>;
    findAllAuthors(): Promise<Author[]>;
    findAllGenres(): Promise<Genre[]>;
    findAllBooks(): Promise<Book[]>;
    findBook(bookId: number): Promise<Book>;
    createBook(book: CreateBookRequest): Promise<Book>;
    updateBook(bookId: number, book: UpdateBookRequest): Promise<Book>;
    deleteBook(bookId: number): Promise<void>;
}

export const useApiClient =
    (): Api => {
        const { token } = useAuth();
        const accept = { "Accept": "application/json" };
        const contentType = { "Content-Type": "application/json" };
        const authorization = (token: Token) => {
            return { "Authorization": "Bearer " + token };
        };
        const headers = (token: Token|null) => {
            return {
                ...contentType,
                ...accept,
                ...(token ? authorization(token) : {})
            };
        };

        return {
            async login(username: string, password: string): Promise<Token> {
                const response = await fetch("/token", {
                    method: "POST",
                    headers: headers(null),
                    body: JSON.stringify({ username, password }),
                });
                const body: TokenResponse = await parseResponse(response);
                return body.token;
            },
            async findAllAuthors(): Promise<Author[]> {
                const response = await fetch('/api/v1/authors', { headers: headers(token) });
                return await parseResponse(response);
            },
            async findAllGenres(): Promise<Genre[]> {
                const response = await fetch('/api/v1/genres', { headers: headers(token) });
                return await parseResponse(response);
            },
            async findAllBooks(): Promise<Book[]> {
                const response = await fetch('/api/v1/books', { headers: headers(token) });
                return await parseResponse(response);
            },
            async findBook(bookId: number): Promise<Book> {
                const response = await fetch('/api/v1/books/' + bookId, { headers: headers(token) });
                return await parseResponse(response);
            },
            async createBook(book: CreateBookRequest): Promise<Book> {
                const response = await fetch("/api/v1/books", {
                    method: "POST",
                    headers: headers(token),
                    body: JSON.stringify(book),
                });
                return await parseResponse(response);
            },
            async updateBook(bookId: number, book: UpdateBookRequest): Promise<Book> {
                const response = await fetch("/api/v1/books/" + bookId, {
                    method: "PUT",
                    headers: headers(token),
                    body: JSON.stringify(book),
                });
                return await parseResponse(response);
            },
            async deleteBook(bookId: number): Promise<void> {
                const response = await fetch("/api/v1/books/" + bookId, {
                    method: "DELETE",
                    headers: headers(token)
                });

                if (!response.ok) {
                    console.error(response);
                }
            }
        }
    }