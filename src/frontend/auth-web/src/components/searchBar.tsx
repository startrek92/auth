import { Form, InputGroup } from "react-bootstrap";
import { FaSearch } from "react-icons/fa";
import "../styles/global.css";

interface SearchBarProps {
    onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

function SearchBar({ onChange }: SearchBarProps) {
    return (
        <>
            <InputGroup>
                <InputGroup.Text>
                    <FaSearch />
                </InputGroup.Text>
                <Form.Control 
                    placeholder="Search by username or email" 
                    aria-label="Search"
                    onChange={onChange}
                />
            </InputGroup>
        </>
    )
}

export {SearchBar};