import {
  Route,
  createBrowserRouter,
  createRoutesFromElements,
  RouterProvider,
  Navigate,
  Outlet
} from "react-router-dom";
import LoginPage from "./components/login";
import SignUpPage from "./components/signup";
import HomePage from "./pages/Home";
import "bootstrap/dist/css/bootstrap.min.css";
import RouteLayout from "./layout/RouteLayout";
import { createContext, useContext, useState } from "react";
import { AuthContextType } from "./types/auth";
import UserProfile from "./pages/Profile";
import NotFoundPage from "./pages/NotFound";
import UserInfoPage from "./pages/UserInfo";

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
        <Route element={<PrivateRoute />}>
          <Route index element={<HomePage />} />
          <Route path="home" element={<HomePage />} />
          <Route path="profile" element={<UserProfile />}/>
          <Route path="user/:id" element={<UserInfoPage/>} />
          <Route path="*" element= {<NotFoundPage />} />
        </Route>
        <Route path="login" element={<LoginPage />} />
        <Route path="signup" element={<SignUpPage />} />
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
  
  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}

export default App;
