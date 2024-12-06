import { authEndpointsConstant } from "../constants/api"
import { restBackendInstance } from "./api"

const authService = {
    login: async (credentials: any) => {
        const response = await restBackendInstance.post(authEndpointsConstant.LOGIN, credentials);
        return response
    }
}


function authLoginUser(credentials: any) {
    console.log("auth user login")
    return authService.login(credentials);
}


export {authLoginUser};