/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import React, { useContext, useEffect } from "react";
import ThemeContext from "../../store/ThemeContext";

function Title(props) {
    useEffect(() => {

    },[]);
    const ctx = useContext(ThemeContext);
    const { background, fontColor } = ctx.themeStyle.content;
    return (
        <h1
            css={[style, css`background: ${background}; color: ${fontColor};`]}
        >
            {props.children}
        </h1>
    );
}

const style = css`
    font-weight: 700;
    font-size: 2.6rem;
    margin-bottom: 50px;
`;

export default React.memo(Title);