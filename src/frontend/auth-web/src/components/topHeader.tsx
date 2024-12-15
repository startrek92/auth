import { Container, Row, Col } from 'react-bootstrap';
import { UserInfoType } from "../types/user";
import { getFromLocalStorage } from "../utils/localStorage";
import { SearchBar } from './searchBar';

interface HeaderProps {
  showSearch?: boolean;
}

export const AuthTopHeader: React.FC<HeaderProps> = ({ showSearch = false }) => {
  const userInfo: UserInfoType | null = getFromLocalStorage("userInfo");

  return (
    <Container fluid className="fixed-top">
      <Row className="align-items-center bg-dark text-white p-3 mt-0 tob-header-shadow">
        <Col className="d-flex align-items-center">
          <h5 className="mb-0">Identity Management</h5>
        </Col>
        {showSearch && (
          <Col className="d-flex align-items-center">
            <SearchBar />
          </Col>
        )}
        <Col className="d-flex align-items-center justify-content-end">
          {userInfo && (
            <a href="/profile" style={{ textDecoration: 'none', color: 'inherit' }}>
              <span>{userInfo.name.toUpperCase()} ({userInfo.email})</span>
            </a>
          )}
        </Col>
      </Row>
    </Container>
  );
};
