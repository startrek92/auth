// define base method here, like post get etc

import axios from "axios";

const getBaseURL = () => {
    const isDomain = /^[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(window.location.hostname);
    if (!isDomain) {
      return 'http://0.0.0.0:8080';
    }
    return `${window.location.origin}/api`;
  };

const restBackendInstance = axios.create({
    baseURL: getBaseURL(),
    timeout: 1000
})

export {restBackendInstance};