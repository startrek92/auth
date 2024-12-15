import React, { useContext, useState } from "react";
import { Form, Button, Container, Row, Col, Alert } from "react-bootstrap";
import { authLoginUser } from "../services/authService";
import { authUtils } from "../utils/auth";
import { HttpStatusCode } from "axios";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../App";
import { saveToLocalStorage } from "../utils/localStorage";

export default function LoginPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [submitEnable, setSubmitEnable] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const{setIsLoggedIn} = useContext(AuthContext);

  async function doLogin(e: React.FormEvent) {
    e.preventDefault();
    setErrorMessage("");
    const credentials = {
      username: username,
      password: password,
      state: authUtils.generateState(),
    };

    try {
      const response = await authLoginUser(credentials);
      if (response.status == HttpStatusCode.Ok) {
        setIsLoggedIn(true);
        const userInfo = response.data.data;
        saveToLocalStorage("isLoggedIn", true);
        saveToLocalStorage("userInfo", userInfo)
       navigate('/home', {state: {userInfo}});
      }
      else {
        const content = JSON.parse(response.data);
        const msg = content.error_description || "Invalid Username or Password";
        setErrorMessage(msg);
      }
    } catch (error: any) {
      setErrorMessage(
        error.response?.data?.data?.error_description || "Something Went Wrong"
      );
    }
  }

  function updateButtonState(username: string, password: string) {
    setSubmitEnable(username.length > 0 && password.length > 0);
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
          <Col sm={10}>
            <div className="text-center">
              <h5 className="fw-bold text-secondary">
                Identity Management
              </h5>
            </div>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col sm={10}>
            <Form onSubmit={doLogin}>
              {errorMessage && (
                <Alert
                  variant="danger"
                  dismissible
                  show={errorMessage.length > 0}
                  onClose={() => {
                    setErrorMessage("");
                  }}
                >
                  {errorMessage}
                </Alert>
              )}
              <Form.Group className="mb-4" controlId="formBasicEmail">
                <Form.Label className="small fw-semibold">Username</Form.Label>
                <Form.Control
                  type="text"
                  className="py-2"
                  placeholder="Enter your username"
                  onChange={(e) => {
                    setUsername(e.target.value);
                    updateButtonState(username, password);
                  }}
                />
              </Form.Group>

              <Form.Group className="mb-4" controlId="formBasicPassword">
                <Form.Label className="small fw-semibold">Password</Form.Label>
                <Form.Control
                  type="password"
                  className="py-2"
                  placeholder="Enter your password"
                  onChange={(e) => {
                    setPassword(e.target.value);
                    updateButtonState(username, password);
                  }}
                />
              </Form.Group>
              <div className="d-flex justify-content-center mt-4">
                <Button
                  variant="dark"
                  type="submit"
                  className="px-4 py-2 fw-semibold"
                  style={{ borderRadius: "25px" }}
                  disabled={!submitEnable}
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
