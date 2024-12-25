import { userEndpointsConstant } from "../constants/api"
import { restBackendInstance } from "./api"

const userService = {
   
    getLoggedInUserInfo: async() => {
        const response = await restBackendInstance.get(userEndpointsConstant.CURRENT_USER_INFO.GET);
        return response;
    }
}

export {userService};