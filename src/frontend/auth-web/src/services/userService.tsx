import { userEndpointsConstant } from "../constants/api"
import { restBackendInstance } from "./api"

const userService = {
   
    getLoggedInUserInfo: async() => {
        const response = await restBackendInstance.get(userEndpointsConstant.USER_INFO.GET);
        return response;
    },

    getUserInfoById: async(userId: string | undefined) => {
        const url = `${userEndpointsConstant.USER_INFO.GET}/${userId}`;
        const response = await restBackendInstance.get(url);
        return response;
    },

    updateLoggedInUserInfo: async(userUpdateData: any) => {
        
        const response = await restBackendInstance.post(
            userEndpointsConstant.USER_INFO.POST, userUpdateData);

        return response
    }
}

export {userService};