import { companyEndpointsConstant } from "../constants/api"
import { restBackendInstance } from "./api"

const companyService = {
   
    getAllUsers: async() => {
        const response = await restBackendInstance.get(companyEndpointsConstant.GET_ALL_USERS.GET);
        return response;
    }
}

export {companyService};