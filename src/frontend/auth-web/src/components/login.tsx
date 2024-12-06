import { useState } from "react";
import { Form, Button, Container, Row, Col } from "react-bootstrap";
import { authLoginUser } from "../services/authService";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  async function doLogin(e: React.FormEvent) {
    e.preventDefault();

    const credentials = {
      username: username,
      password: password,
    };

    await authLoginUser(credentials);
  }

  return (
    <Container
      className="d-flex align-items-center justify-content-center"
      style={{ minHeight: "100vh" }}
    >
      <div
        className="bg-white p-4 rounded shadow-lg"
        style={{ width: "100%", maxWidth: "500px" }}
      >
        <Row className="justify-content-center mb-4">
          <Col sm={12}>
            <div className="text-center">
              <h3 className="fw-bold text-secondary">
                Welcome to DosePacker System
              </h3>
            </div>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col sm={10}>
            <Form onSubmit={doLogin}>
              <Form.Group className="mb-4" controlId="formBasicEmail">
                <Form.Label className="small fw-semibold">Username</Form.Label>
                <Form.Control
                  type="text"
                  className="py-2"
                  placeholder="Enter your username"
                  onChange={(e) => setUsername(e.target.value)}
                />
              </Form.Group>

              <Form.Group className="mb-4" controlId="formBasicPassword">
                <Form.Label className="small fw-semibold">Password</Form.Label>
                <Form.Control
                  type="password"
                  className="py-2"
                  placeholder="Enter your password"
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Form.Group>
              <div className="d-flex justify-content-center mt-4">
                <Button
                  variant="dark"
                  type="submit"
                  className="px-4 py-2 fw-semibold"
                  style={{ borderRadius: "25px" }}
                >
                  Login
                </Button>
              </div>
            </Form>
          </Col>
        </Row>
      </div>
    </Container>
  );
}
