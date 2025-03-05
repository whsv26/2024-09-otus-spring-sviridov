interface Author {
    id: number;
    fullName: string;
}

interface Genre {
    id: number;
    name: string;
}

interface Comment {
    id: number;
    bookId: number;
    text: string;
}

interface Book {
    id: number;
    title: string;
    author: Author;
    genres: Genre[];
}