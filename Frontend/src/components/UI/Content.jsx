import React, { useContext } from "react";
import { css } from "@emotion/react";
import ThemeContext from "../../store/ThemeContext";


const divStyle = css`
    width: 100vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    padding: 1rem;
    justify-content: flex-start;
    align-items: center;
    overflow-x: hidden;
`;

function Content(props) {
    const ctx = useContext(ThemeContext);
    const { background, fontColor } = ctx.themeStyle.content;
    return (
        <div className={props.className}css={[divStyle, css`background: ${background}; color: ${fontColor};`]}>
            {props.children}
        </div>
    );
}

export default Content;