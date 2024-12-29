import { useParams } from "react-router-dom";
import { AuthTopHeader } from "../components/topHeader";
import { useEffect, useState } from "react";
import { Container, Card, Row, Col, Tab, Nav, Badge, Button } from 'react-bootstrap';
import { format } from 'date-fns';
import reactLogo from '../assets/react.svg';
import { userService } from "../services/userService";

interface UserDetails {
  id: string;
  name: string;
  username: string;
  email: string;
  createdOn: Date;
  contact: {
    work_phone?: string;
    extension?: string;
    corporate_email: string;
  };
  address?: string;
  roles: string[];
  profileImage?: string;
}

function UserInfoPage() {
    const urlParams = useParams();
    const userId: string| undefined = urlParams.id;
    const [userDetails, setUserDetails] = useState<UserDetails | null>(null);

    useEffect(() => {
        const fetchUserDetails = async () => {
            const apiResponse = await userService.getUserInfoById(userId);
            const userInfoById = apiResponse.data.data[0];
            const mockUser: UserDetails = {
                id: userInfoById.id,
                name: userInfoById.name,
                username: userInfoById.username,
                email: userInfoById.email,
                createdOn: new Date(userInfoById.created_on),
                contact: {
                    work_phone: "+1 (555) 123-4567",
                    extension: "4567",
                    corporate_email: "john.doe@company.com"
                },
                address: "123 Main St, City, Country",
                roles: ["Pharmacy Technician"],
                profileImage: ""
            };
            console.log("mock user: ", mockUser)
            setUserDetails(mockUser);
        };

        fetchUserDetails();
    }, [userId]);

    return (
        <>
            <AuthTopHeader showSearch={false} onSearch={() => {}} />
            <Container className="mt-6 pt-6">
                <Card className="border-0 shadow-sm mb-4">
                    <Card.Body>
                        <div className="text-center mb-4">
                            <img
                                src={ userDetails?.profileImage || reactLogo }
                                alt={`${userDetails?.name}'s profile`}
                                style={{
                                    width: '150px',
                                    height: '150px',
                                    borderRadius: '50%',
                                    objectFit: 'cover',
                                    border: '3px solid #f8f9fa'
                                }}
                            />
                        </div>
                        <Row className="mt-3 pt-3">
                            <Col md={6}>
                                <h5 className="mb-3">Basic Information</h5>
                                <div className="mb-2"><strong>ID:</strong> {userDetails?.id}</div>
                                <div className="mb-2"><strong>Name:</strong> {userDetails?.name}</div>
                                <div className="mb-2"><strong>Username:</strong> {userDetails?.username}</div>
                                <div className="mb-2"><strong>Email:</strong> {userDetails?.email}</div>
                                <div className="mb-2">
                                    <strong>Created On:</strong> 
                                    {userDetails?.createdOn && format(userDetails.createdOn, 'MMM dd, yyyy')}
                                </div>
                            </Col>
                            <Col md={6}>
                                <h5 className="mb-3">Contact & Role Information</h5>
                                <div className="mb-2">
                                    <strong>Work Phone:</strong> {userDetails?.contact.work_phone}
                                </div>
                                <div className="mb-2">
                                    <strong>Extension:</strong> {userDetails?.contact.extension}
                                </div>
                                <div className="mb-2">
                                    <strong>Corporate Email:</strong> {userDetails?.contact.corporate_email}
                                </div>
                                <div className="mb-2">
                                    <strong>Address:</strong> {userDetails?.address}
                                </div>
                                <div className="mb-2">
                                    <strong>Roles:</strong> {userDetails?.roles.join(', ')}
                                </div>
                            </Col>
                        </Row>
                    </Card.Body>
                </Card>
            </Container>
        </>
    );
}

export default UserInfoPage;
