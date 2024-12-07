
const authUtils = {
    generateState: () => {
        const value = crypto.randomUUID();
        return value;
    }
}


export {authUtils}
