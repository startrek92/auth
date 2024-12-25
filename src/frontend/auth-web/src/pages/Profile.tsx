import { data, useNavigate } from "react-router-dom";
import { UserInfoType } from "../types/user";
import { getFromLocalStorage } from "../utils/localStorage";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../App";
import { AuthTopHeader } from "../components/topHeader";
import { Card, Button, Container, Row, Col } from "react-bootstrap";
import "../styles/global.css";
import { userService } from "../services/userService";
import { FaEnvelope, FaUser, FaBuilding, FaCalendar } from "react-icons/fa";

export default function UserProfile() {
  console.log("Component mounting");
  const { setIsLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();
  const userInfo: UserInfoType | null = getFromLocalStorage("userInfo");
  const [loggedInUserInfo, setLoggedInUserInfo] = useState<any>(null);
  const [componentLoading, setComponentLoading] = useState(false);

  useEffect(() => {
    const fetchUserInfo = async () => {
      console.log("use Effect executed!");
      const currentUserInfoBackend: any =
        await userService.getLoggedInUserInfo();
      console.log(currentUserInfoBackend.data.data);
      setLoggedInUserInfo(currentUserInfoBackend.data.data);
      setComponentLoading(true);
    };
    fetchUserInfo();
  }, []);

  if (userInfo == null) {
    setIsLoggedIn(false);
    navigate("/login");
    return <></>;
  }

  if (!componentLoading) {
    return <h3>Loading User Info</h3>;
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  return (
    <>
      <AuthTopHeader showSearch={false} />
      <Container className="py-5">
        <Row className="justify-content-center">
          <Col md={8} lg={6}>
            <Card className="shadow">
              <Card.Body className="p-4">
                <Card.Title className="text-center mb-4">
                  <h2 className="text-uppercase">
                    {loggedInUserInfo.name} {console.log(loggedInUserInfo)}
                  </h2>
                </Card.Title>

                <div className="user-info mb-4">
                  <Row className="mb-3 align-items-center">
                    <Col xs={2} sm={1} className="text-muted">
                      <FaEnvelope />
                    </Col>
                    <Col xs={10} sm={3} className="text-muted">
                      Email:
                    </Col>
                    <Col xs={12} sm={8}>
                      {loggedInUserInfo.email}
                    </Col>
                  </Row>

                  <Row className="mb-3 align-items-center">
                    <Col xs={2} sm={1} className="text-muted">
                      <FaUser />
                    </Col>
                    <Col xs={10} sm={3} className="text-muted">
                      Username:
                    </Col>
                    <Col xs={12} sm={8}>
                      {loggedInUserInfo.username}
                    </Col>
                  </Row>

                  <Row className="mb-3 align-items-center">
                    <Col xs={2} sm={1} className="text-muted">
                      <FaBuilding />
                    </Col>
                    <Col xs={10} sm={3} className="text-muted">
                      Company:
                    </Col>
                    <Col xs={12} sm={8}>
                      {loggedInUserInfo.company_name}
                    </Col>
                  </Row>

                  <Row className="mb-3 align-items-center">
                    <Col xs={2} sm={1} className="text-muted">
                      <FaCalendar />
                    </Col>
                    <Col xs={10} sm={3} className="text-muted">
                      Member Since:
                    </Col>
                    <Col xs={12} sm={8}>
                      {formatDate(loggedInUserInfo.created_on)}
                    </Col>
                  </Row>
                </div>

                <div className="text-center">
                  <Button
                    variant="primary"
                    className="px-4 auth-login-button"
                    onClick={() => navigate("/edit-profile")}
                  >
                    Edit Profile
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
}
