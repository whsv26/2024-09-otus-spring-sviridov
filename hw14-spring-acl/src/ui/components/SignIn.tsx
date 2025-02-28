import React, {ChangeEventHandler, FormEventHandler, useState} from "react";
import {useNavigate} from 'react-router-dom';
import {useApiClient} from './Api'
import {useAuth} from "./AuthProvider";

interface LoginForm {
    username: string;
    password: string;
}

const SignIn = () => {
    const api = useApiClient();
    const { setToken } = useAuth();
    const navigate = useNavigate();
    const goToHomePage = () => navigate('/');

    const initialForm = { password: "", username: "" };
    const [loginForm, setLoginForm] = useState<LoginForm>(initialForm);


    const handleChange: ChangeEventHandler<HTMLInputElement|HTMLSelectElement> =
        (e) => {
            const { name, value } = e.target;
            setLoginForm({
                ...loginForm,
                [name]: value,
            });
        };

    const handleSubmit: FormEventHandler<HTMLFormElement> =
        async (e) => {
            e.preventDefault();
            const username = loginForm.username;
            const password = loginForm.password;
            const token = await api.login(username, password);
            setToken(token);
            await navigate('/');
        }

    return (
        <React.Fragment>
            <div>
                <h1>Sign In</h1>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="username">Username:</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={loginForm.username}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={loginForm.password}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <button onClick={goToHomePage}>Home</button>
                    <button type="submit">Submit</button>
                </form>
            </div>
        </React.Fragment>
    )
}

export default SignIn;