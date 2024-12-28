import { companyEndpointsConstant } from "../constants/api"
import { restBackendInstance } from "./api"

const companyService = {
   
    getAllUsers: async(query: string | null) => {
        const urlParams = new URLSearchParams({
            ...(query && {query: query})
        }).toString();
        const url = `${companyEndpointsConstant.GET_ALL_USERS.GET}${urlParams ? `?${urlParams}` : ''}`;
        const response = await restBackendInstance.get(url);
        return response;
    }
}

export {companyService};