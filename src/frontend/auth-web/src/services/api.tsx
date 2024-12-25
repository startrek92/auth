// define base method here, like post get etc

import axios, { HttpStatusCode } from "axios";
import { getFromLocalStorage } from "../utils/localStorage";

const noAuthUrls = ["/auth/login"];

const getBaseURL = () => {
  const isDomain = /^[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(
    window.location.hostname
  );
  if (!isDomain) {
    return "http://0.0.0.0:80/api";
  }
  return `${window.location.origin}/api`;
};

const restBackendInstance = axios.create({
  baseURL: getBaseURL(),
  timeout: 1000,
});

restBackendInstance.interceptors.request.use(
  function (config) {
    if (noAuthUrls.some((url) => config.url?.startsWith(url))) {
      return config;
    }

    const userInfo: any = getFromLocalStorage("userInfo");
    if (userInfo == null || userInfo?.token == null) {
      window.location.href = "/login";
      console.log("auth info not found ... redirecting to login page");
    }

    const token = userInfo.token;
    config.headers.Authorization = `Bearer ${token}`;
    console.log("added auth header in config request")
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

restBackendInstance.interceptors.response.use(
  function (response) {
    console.log("checking status")
    if(response.status == HttpStatusCode.Unauthorized) {
      console.log("unauthorized 401")
      window.location.href = "/login";
    }
    return response;
  },
  function (error) {
    if(error?.response?.status == HttpStatusCode.Unauthorized) {
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export { restBackendInstance };
