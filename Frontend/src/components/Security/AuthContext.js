import { useState } from "react";
import { useContext } from "react";
import { createContext } from "react";

export const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

/**
 * This component is responsible to make user Authenticated after Login/Register.
 * @param {*} param0 takes parameter of all the children component which is to be shown if user is Authenticated.
 * @returns
 */
export default function AuthProvider({ children }) {
  const [isAuthenticated, setAuthenticated] = useState(false);
  const toShare = { isAuthenticated, setAuthenticated };
  return (
    <AuthContext.Provider value={toShare}>{children}</AuthContext.Provider>
  );
}
