import Content from "components/UI/Content";
import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import Id from "./Id";
import Password from "./Password";
import { css } from "@emotion/react";
import { useRecoilValue } from "recoil";
import LoginState from "store/LoginState";

function Title(props: { children: string }) {
    return (<h1 css={[style]}> {props.children}</h1>);
}
const Index = () => {
    const { type } = useParams<{ type: string }>();
    const { t } = useTranslation("findPage");
    const title: string = type === "id" ? t("id.title") : t("pw.title");
    const render = type === "id" ? <Id /> : <Password />;
    const navigate = useNavigate();
    const { isLogIn } = useRecoilValue<{ isLogIn: boolean }>(LoginState);
    useEffect(() => {
        if (isLogIn) {
            navigate("/");
        }
    }, [isLogIn, navigate, type]);

    if (type !== "id" && type !== "pw") {
        return (
            <div>{"error"}</div>
        );
    }
    if (type === "id" || type === "pw") {
        return (
            <Content css={css`p { margin-bottom: 1rem;}`}>
                <Title>{title}</Title>
                <p>{t("ment")}</p>
                {render}
            </Content >
        );
    }

};

const style = css`
    font-weight: 700;
    font-size: 2.6rem;
    margin-bottom: 50px;    
`;

export default Index;