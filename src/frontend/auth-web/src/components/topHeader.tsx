import { Container, Row, Col, Button } from "react-bootstrap";
import { UserInfoType } from "../types/user";
import { getFromLocalStorage, saveToLocalStorage } from "../utils/localStorage";
import { SearchBar } from "./searchBar";
import React, { useContext } from "react";
import "../styles/global.css";
import { authService } from "../services/authService";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../App";

interface HeaderProps {
  showSearch?: boolean;
  onSearch: (query: string) => void;
}

export const AuthTopHeader: React.FC<HeaderProps> = ({
  showSearch = false,
  onSearch,
}) => {
  const userInfo: UserInfoType | null = getFromLocalStorage("userInfo");
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onSearch(e.target.value);
  };

  const navigate = useNavigate();
  const {setIsLoggedIn} = useContext(AuthContext);

  const doLogout = () => {
    authService.logout();
    setIsLoggedIn(false);
    saveToLocalStorage("isLoggedIn", false);
    navigate("./login");
  };

  return (
    <Container fluid className="fixed-top">
      <Row className="align-items-center bg-dark text-white p-3 mt-0 tob-header-shadow">
        <Col className="d-flex align-items-center">
          <h5 className="mb-0">
            <a href="/" style={{ textDecoration: "none", color: "inherit" }}>
              User Management
            </a>
          </h5>
        </Col>
        {showSearch && (
          <Col className="d-flex align-items-center">
            <SearchBar onChange={handleInputChange} />
          </Col>
        )}
        <Col className="d-flex align-items-center justify-content-end">
          {userInfo && (
            <div className="d-flex align-items-center">
              <a
                href="/profile"
                style={{ textDecoration: "none", color: "inherit" }}
                className="me-3"
              >
                <span>{userInfo.name.toUpperCase()}</span>
              </a>
              <Button
                variant="outline-light"
                className="auth-logout-button"
                onClick={doLogout}
              >
                Log Out
              </Button>
            </div>
          )}
        </Col>
      </Row>
    </Container>
  );
};
