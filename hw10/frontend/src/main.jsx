import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import AppContext from "./context/AppContext";
import Authors from "./routes/authors";
import Books from "./routes/books";
import ErrorPage from "./routes/error-page";
import Genres from "./routes/genres";
import GreetingPage from "./routes/greeting-page";
import Root from "./routes/root";

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

const router = createBrowserRouter([
    {
        path: '/',
        element: <Root />,
        errorElement: <ErrorPage />,
        children: [
            {
                index: true,
                element: <GreetingPage />
            },
            {
                path: '/books',
                element: <Books />
            },
            {
                path: '/authors',
                element: <Authors />
            },
            {
                path: '/genres',
                element: <Genres />
            }
        ]
    }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <AppContext>
        <RouterProvider router={router} />
    </AppContext>
);
