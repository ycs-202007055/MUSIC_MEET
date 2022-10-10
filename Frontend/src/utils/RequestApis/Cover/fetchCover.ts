
import customAxios from "../../customAxios";
import CoverType from "pages/Cover/CoverType";

const fetchCover =
    (id: string): Promise<CoverType> => {
        const token = localStorage.getItem("token");
        const axios = customAxios();
        return axios({
            method: "GET",
            url: `/cover/${id}`,
            headers: {
                "authorization": `${token}`
            },
        }).then(res => res.data);
    };

export default fetchCover;