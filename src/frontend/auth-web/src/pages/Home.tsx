import { Container, Row, Col, Card, Navbar, Nav } from "react-bootstrap";
import { UserInfoType } from "../types/user";
import { getFromLocalStorage } from "../utils/localStorage";
import "../styles/global.css";
import { SearchBar } from "../components/searchBar";
import { AuthTopHeader } from "../components/topHeader";
import { useEffect, useState } from "react";
import { userService } from "../services/userService";
import { companyService } from "../services/endpointService";
/**
 * Router in App tsx checks is user is logged in or not.
 * if will handle redirection of user
 * @returns
 */
export default function HomePage() {
  const [userInfoList, setUserInfoList] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const getUserList = async () => {
      const allUserList = await companyService.getAllUsers();
      setUserInfoList(allUserList.data.data);
      setIsLoading(false);
    };
    getUserList();
  }, []);

  return (
    <>
      <AuthTopHeader showSearch={true} />
      {isLoading ? (
        <div>Loading...</div>
      ) : (
        <Row className="mt-5 pt-5 ms-3">
          {userInfoList.map((user, index) => (
            <Col key={index} sm={12} md={6} lg={4} className="mb-4">
              <Card>
                <Card.Body>
                  <Card.Title>{user.name.toUpperCase()}</Card.Title>
                  <Card.Subtitle className="mb-2 text-muted">
                    {user.company_name}
                  </Card.Subtitle>
                  <Card.Text>
                    <strong>Username:</strong> {user.username} <br />
                    <strong>Email:</strong> {user.email} <br />
                    <strong>Age:</strong> {user.age} <br />
                    <strong>Active:</strong> {user.is_active ? "Yes" : "No"}{" "}
                    <br />
                    <strong>Created On:</strong>{" "}
                    {new Date(user.created_on).toLocaleDateString()}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </>
  );
}
