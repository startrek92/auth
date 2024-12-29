import { Container, Row, Col } from "react-bootstrap";
import { UserInfoType } from "../types/user";
import { getFromLocalStorage } from "../utils/localStorage";
import { SearchBar } from "./searchBar";
import React from "react";

interface HeaderProps {
  showSearch?: boolean;
  onSearch: (query: string) => void;
}

export const AuthTopHeader: React.FC<HeaderProps> = ({
  showSearch = false,
  onSearch
}) => {
  const userInfo: UserInfoType | null = getFromLocalStorage("userInfo");
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onSearch(e.target.value);
  }
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
            <SearchBar onChange={handleInputChange}/>
          </Col>
        )}
        <Col className="d-flex align-items-center justify-content-end">
          {userInfo && (
            <a
              href="/profile"
              style={{ textDecoration: "none", color: "inherit" }}
            >
              <span>
                {userInfo.name.toUpperCase()} ({userInfo.email})
              </span>
            </a>
          )}
        </Col>
      </Row>
    </Container>
  );
};
