import customAxios from "../../customAxios";

const changePassword = async ({ key, password }: { key?: string | null, password: string} ): Promise<any> => {
    const axios = customAxios();
    const token = localStorage.getItem("token");
    if(token) {
        return axios({
            method: "PATCH",
            url: `/password`,
            headers: {
                authorization: `${token}`,
            }
        });
    }
    else {
        return axios({
            method: "PATCH",
            url: `/password`,
            data: {
                encoding_key: key,
                newPw: password
            }
        });
    }
        
};

export default changePassword;