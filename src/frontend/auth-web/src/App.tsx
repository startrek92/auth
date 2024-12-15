import {
  Route,
  createBrowserRouter,
  createRoutesFromElements,
  RouterProvider,
  Navigate,
} from "react-router-dom";
import LoginPage from "./components/login";
import SignUpPage from "./components/signup";
import HomePage from "./pages/Home";
import "bootstrap/dist/css/bootstrap.min.css";
import RouteLayout from "./layout/RouteLayout";
import { createContext, useContext, useState } from "react";
import { AuthContextType } from "./types/auth";

export const AuthContext = createContext<AuthContextType>({
  isLoggedIn: false,
  setIsLoggedIn: () => {},
});

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    return localStorage.getItem("isLoggedIn") === "true";
  });

  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/" element={<RouteLayout />}>
        <Route index element={<HomePage />} />
        <Route path="login" element={<LoginPage />} />
        <Route path="signup" element={<SignUpPage />} />
        <Route path="home" element={<PrivateRoute />} />
      </Route>
    )
  );

  return (
    <AuthContext.Provider value={{ isLoggedIn, setIsLoggedIn }}>
      <RouterProvider router={router}></RouterProvider>
    </AuthContext.Provider>
  );
}

function PrivateRoute() {
  const { isLoggedIn } = useContext(AuthContext);

  return isLoggedIn ? <HomePage /> : <Navigate to="/login"></Navigate>;
}

export default App;
