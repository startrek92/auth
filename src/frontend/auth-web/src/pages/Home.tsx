import { Container, Row, Col, Card, Navbar, Nav } from 'react-bootstrap';
import { UserInfoType } from "../types/user";
import { getFromLocalStorage } from "../utils/localStorage";
import "../styles/global.css";
import { SearchBar } from '../components/searchBar';
import { AuthTopHeader } from '../components/topHeader';
/**
 * Router in App tsx checks is user is logged in or not.
 * if will handle redirection of user
 * @returns
 */
export default function HomePage() {
  return (
    <>
        <AuthTopHeader showSearch={true} />
    </>
  );
}
