import React from "react";
import { css } from "@emotion/react";
import ValuesEdit from "./ValuesEdit";
import ImageEdit from "./ImageEdit";
import { useQuery } from "react-query";
import getMyInfo from "../../../utils/RequestApis/MyPage/getMyInfo";
import SectionWrapper from "components/common/SectionWrapper";

function UserEdit() {
    const { data } = useQuery(["myinfo"], () => getMyInfo(),
        {

        }
    );
    const myInfo = data?.data;
    const { image } = myInfo;
    return (
        <SectionWrapper css={[articleStyle]}>
            <ImageEdit
                image={image}
            />
            <ValuesEdit
                myInfo={myInfo}
            />
        </SectionWrapper>
    );
}

const articleStyle = css`
    min-height: 20vh;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
    section {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    
    }
`;

export default React.memo(UserEdit);