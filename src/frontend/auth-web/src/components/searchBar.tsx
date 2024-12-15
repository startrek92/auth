import { Form, InputGroup } from "react-bootstrap";
import { FaSearch } from "react-icons/fa";
import "../styles/global.css";

function SearchBar() {
    return (
        <>
        <InputGroup>
            <InputGroup.Text>
                <FaSearch />
            </InputGroup.Text>
            <Form.Control 
            placeholder="Search by username or email" 
            aria-label="Search"
            />

        </InputGroup>
        </>
    )
}

export {SearchBar};