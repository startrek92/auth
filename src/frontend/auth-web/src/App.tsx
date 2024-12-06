import {
  Route,
  createBrowserRouter,
  createRoutesFromElements,
  RouterProvider,
} from "react-router-dom";
import LoginPage from "./components/login";
import SignUpPage from "./components/signup";
import HomePage from "./pages/Home";
import "bootstrap/dist/css/bootstrap.min.css";
import RouteLayout from "./layout/RouteLayout";

function App() {
  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/" element={<RouteLayout />}>
        <Route index element={<HomePage />} />
        <Route path="login" element={<LoginPage />} />
        <Route path="signup" element={<SignUpPage />} />
      </Route>
    )
  );

  return (
    <RouterProvider router={router}></RouterProvider>
  );
}

export default App;
