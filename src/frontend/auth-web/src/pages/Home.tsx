import { UserInfoType } from "../types/user";
import { getFromLocalStorage } from "../utils/localStorage";

/**
 * Router in App tsx checks is user is logged in or not.
 * if will handle redirection of user
 * @returns
 */
export default function HomePage() {
  const userInfo: UserInfoType | null = getFromLocalStorage("userInfo");

  return (
    <div>
      <h3>Identity Management</h3>
      <div>
        {userInfo ? (
          <>
            <p>Welcome, {userInfo.name}!</p>
            <p>Email: {userInfo.email}</p>
          </>
        ) : (
          <p>Loading user information...</p>
        )}
      </div>
    </div>
  );
}
