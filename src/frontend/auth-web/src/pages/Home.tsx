import { Row, Col, Card } from "react-bootstrap";
import "../styles/global.css";
import { AuthTopHeader } from "../components/topHeader";
import { useEffect, useState } from "react";
import { companyService } from "../services/endpointService";
import { getFromLocalStorage } from "../utils/localStorage";
import { useNavigate } from "react-router-dom";
/**
 * Router in App tsx checks is user is logged in or not.
 * if will handle redirection of user
 * @returns
 */
interface User {
  id: number;
  name: string;
  company_name: string;
  username: string;
  email: string;
  age: number;
  is_active: boolean;
  created_on: string;
  profile_image?: string;
}

export default function HomePage() {
  const [userInfoList, setUserInfoList] = useState<User[] | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const currentUserInfoBackend: any = getFromLocalStorage("userInfo");
  const navigate = useNavigate();

  const handleSearch = (query: string) => {
    console.log("setting search query: ", query);
    setSearchQuery(query);
  };

  if(currentUserInfoBackend == null) {
    navigate("./login");
    return <></>
  }

  useEffect(() => {
    const getUserList = async () => {
      const allUserList = await companyService.getAllUsers(searchQuery);
      setUserInfoList(allUserList.data.data);
      setIsLoading(false);
    };
    getUserList();
  }, [searchQuery]);

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  return (
    <>
      <AuthTopHeader showSearch={true} onSearch={handleSearch} />
      <div className="p-4" style={{ marginTop: "60px" }}>
        {isLoading && userInfoList == null ? (
          <div>Loading...</div>
        ) : (
          <Row className="g-4">
            {userInfoList != null &&
              userInfoList.map((user, index) => (
                <Col key={index} sm={12} md={6} lg={4} xl={3}>
                  <a 
                    href={`./user/${user.id}`}
                    style={{ 
                      textDecoration: 'none', 
                      display: 'block',
                      color: 'inherit',
                      cursor: 'default'
                    }}
                  >
                    <Card className="border-0 shadow-sm" id={`user-id-${user.id}`}>
                      <Card.Body className="d-flex justify-content-between p-3">
                        <div className="d-flex align-items-center">
                          <div
                            className="rounded-circle bg-light d-flex align-items-center justify-content-center me-3"
                            style={{
                              width: "48px",
                              height: "48px",
                              overflow: "hidden",
                            }}
                          >
                            {user.profile_image ? (
                              <img
                                src={user.profile_image}
                                alt={user.name}
                                className="w-100 h-100 object-fit-cover"
                              />
                            ) : (
                              <i className="bi bi-person text-secondary fs-4"></i>
                            )}
                          </div>
                          <div>
                            <h6 className="mb-1 fw-semibold">
                              {user.name}
                              {user.id === currentUserInfoBackend.id && (
                                <span className="ms-2 badge bg-secondary">You</span>
                              )}
                            </h6>
                            <p className="mb-0 text-muted small">{user.company_name}</p>
                            <small className="text-muted">{user.email}</small>
                          </div>
                        </div>

                        <div className="text-end">
                          <div className="mb-2">
                            <span className={`badge ${user.is_active ? 'bg-dark' : 'bg-danger'}`}>
                              {user.is_active ? 'Active' : 'Inactive'}
                            </span>
                          </div>
                          <small className="text-muted d-block">
                            Joined {formatDate(user.created_on)}
                          </small>
                        </div>
                      </Card.Body>
                    </Card>
                  </a>
                </Col>
              ))}
          </Row>
        )}
      </div>
    </>
  );
}
