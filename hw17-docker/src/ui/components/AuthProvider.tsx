import React, { createContext, useContext, useState } from 'react';

interface AuthContextType {
    token: string | null;
    setToken: (token: string | null) => void;
}

const AuthContext =
    createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
    children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> =
    ({ children }) => {
        const [token, setToken] = useState<string | null>(null);

        return (
            <AuthContext.Provider value={{ token, setToken }}>
                {children}
            </AuthContext.Provider>
        );
    };

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};