import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { AuthTopHeader } from "../components/topHeader";
import "../styles/global.css"

const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="d-flex flex-column min-vh-100">
      <AuthTopHeader showSearch={false} />
      <Container className="text-center flex-grow-1 d-flex align-items-center">
        <Row className="justify-content-center w-100">
          <Col md={6}>
            <h1 className="display-1 fw-bold">404</h1>
            <h2 className="mb-3">Page Not Found</h2>
            <p className="mb-4 text-muted">
              The page you are looking for might have been removed, had its name
              changed, or is temporarily unavailable.
            </p>
            <Button className="auth-login-button" onClick={() => navigate("/")}>
              Go Back Home
            </Button>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default NotFoundPage;
