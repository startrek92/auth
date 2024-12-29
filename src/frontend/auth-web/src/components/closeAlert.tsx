import { Alert } from "react-bootstrap";

interface CloseAlertMessageProps {
  message: string;
  onClose: () => void;
}

const CloseAlertMessage: React.FC<CloseAlertMessageProps> = ({
  message,
  onClose,
}) => {
  return (
    <Alert
      variant="danger"
      dismissible
      show={message.length > 0}
      onClose={onClose}
    >
      {message}
    </Alert>
  );
};


export default CloseAlertMessage;
