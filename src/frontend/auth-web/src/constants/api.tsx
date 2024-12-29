const authEndpointsConstant = {
    LOGIN: "/auth/login"
}

const userEndpointsConstant = {
    USER_INFO: {
        GET: "/user", // get userInfo
        POST: "/user" // update userInfo
    }
}

const companyEndpointsConstant = {
    GET_ALL_USERS: {
        GET: "/company/user"
    }
}

export {authEndpointsConstant, userEndpointsConstant, companyEndpointsConstant};