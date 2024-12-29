import { data, useNavigate } from "react-router-dom";
import { UserInfoType } from "../types/user";
import { getFromLocalStorage } from "../utils/localStorage";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../App";
import { AuthTopHeader } from "../components/topHeader";
import { Card, Button, Container, Row, Col, Form } from "react-bootstrap";
import "../styles/global.css";
import { userService } from "../services/userService";
import { FaEnvelope, FaUser, FaBuilding, FaCalendar } from "react-icons/fa";
import CloseAlertMessage from "../components/closeAlert";
import { HttpStatusCode } from "axios";

export default function UserProfile() {
  console.log("Component mounting");
  const { setIsLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();
  const userInfo: UserInfoType | null = getFromLocalStorage("userInfo");
  const [loggedInUserInfo, setLoggedInUserInfo] = useState<any>(null);
  const [componentLoading, setComponentLoading] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editedInfo, setEditedInfo] = useState({
    name: "",
    email: "",
    username: "",
    profile_image: "",
  });

  const [updateInfoErrorMessage, setUpdateInfoErrorMessage] = useState("");

  function handleErrorMessageClose() {
    setUpdateInfoErrorMessage("");
  }

  useEffect(() => {
    const fetchUserInfo = async () => {
      console.log("use Effect executed!");
      const currentUserInfoBackend: any =
        await userService.getLoggedInUserInfo();
      console.log(currentUserInfoBackend.data.data);
      setLoggedInUserInfo(currentUserInfoBackend.data.data);
      setEditedInfo({
        name: currentUserInfoBackend.data.data.name,
        email: currentUserInfoBackend.data.data.email,
        username: currentUserInfoBackend.data.data.username,
        profile_image: currentUserInfoBackend.data.data.profile_image,
      });
      setComponentLoading(true);
    };
    fetchUserInfo();
  }, []);

  const handleSave = async () => {
    try {
      const response = await userService.updateLoggedInUserInfo(editedInfo);
      if (response.status == HttpStatusCode.Ok) {
        setLoggedInUserInfo({ ...loggedInUserInfo, ...editedInfo });
        setIsEditing(false);
      }
    } catch (error: any) {
      const response = error?.response;
      const content = response?.data?.data;
      const msg = content.error_description || "Something Went Wrong <3";
      setUpdateInfoErrorMessage(msg);
    }
  };

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
      <AuthTopHeader showSearch={false} onSearch={() => {}} />
      <Container className="py-5">
        <Row className="justify-content-center">
          <Col md={8} lg={6}>
            <Card className="shadow">
              <Card.Body className="p-4">
                {updateInfoErrorMessage.length > 0 && (
                  <CloseAlertMessage
                    message={updateInfoErrorMessage}
                    onClose={handleErrorMessageClose}
                  />
                )}
                <div className="text-center mb-4">
                  <div
                    className="rounded-circle bg-light d-flex align-items-center justify-content-center mx-auto mb-3"
                    style={{
                      width: "120px",
                      height: "120px",
                      overflow: "hidden",
                    }}
                  >
                    {loggedInUserInfo.profile_image ? (
                      <img
                        src={loggedInUserInfo.profile_image}
                        alt={loggedInUserInfo.name}
                        className="w-100 h-100 object-fit-cover"
                      />
                    ) : (
                      <i
                        className="bi bi-person text-secondary"
                        style={{ fontSize: "3rem" }}
                      ></i>
                    )}
                  </div>
                  <Card.Title className="text-center">
                    <h2 className="text-uppercase">{loggedInUserInfo.name}</h2>
                  </Card.Title>
                </div>

                <div className="user-info mb-4">
                  <Row className="mb-3 align-items-center">
                    <Col xs={2} sm={1} className="text-muted">
                      <FaEnvelope />
                    </Col>
                    <Col xs={10} sm={3} className="text-muted">
                      Email:
                    </Col>
                    <Col xs={12} sm={8}>
                      {isEditing ? (
                        <Form.Control
                          type="email"
                          value={editedInfo.email}
                          onChange={(e) =>
                            setEditedInfo({
                              ...editedInfo,
                              email: e.target.value,
                            })
                          }
                        />
                      ) : (
                        loggedInUserInfo.email
                      )}
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
                      {isEditing ? (
                        <Form.Control
                          type="text"
                          value={editedInfo.username}
                          onChange={(e) =>
                            setEditedInfo({
                              ...editedInfo,
                              username: e.target.value,
                            })
                          }
                        />
                      ) : (
                        loggedInUserInfo.username
                      )}
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
                  {isEditing ? (
                    <>
                      <Button
                        variant="success"
                        className="px-4 me-2 auth-login-button"
                        onClick={handleSave}
                      >
                        Save
                      </Button>
                      <Button
                        variant="secondary"
                        className="px-4"
                        onClick={() => {
                          setIsEditing(false);
                          setEditedInfo({
                            name: loggedInUserInfo.name,
                            email: loggedInUserInfo.email,
                            username: loggedInUserInfo.username,
                            profile_image: loggedInUserInfo.profile_image,
                          });
                        }}
                      >
                        Cancel
                      </Button>
                    </>
                  ) : (
                    <Button
                      variant="primary"
                      className="px-4 auth-login-button"
                      onClick={() => setIsEditing(true)}
                    >
                      Edit Profile
                    </Button>
                  )}
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
}
