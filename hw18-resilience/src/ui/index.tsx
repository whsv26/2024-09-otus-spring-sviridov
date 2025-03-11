import { createRoot } from 'react-dom/client';
import App from './components/App';
import React from "react";
import {AuthProvider} from "./components/AuthProvider";

const container = document.getElementById('root');
if (!container) {
    throw new Error("Root container not found");
}

const root = createRoot(container);

root.render(
    <AuthProvider>
        <App />
    </AuthProvider>
);