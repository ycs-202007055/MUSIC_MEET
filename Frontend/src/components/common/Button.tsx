import { css } from "@emotion/react";
import React, { useContext } from "react";
import ThemeContext from "../../store/ThemeContext";

interface ButtonProps {
    value: string;
    onClick?: () => void;
    w?: string,
    h?: string
}

function Button(props: ButtonProps) {
    const { value, onClick, w, h } = props;
    const ctx = useContext(ThemeContext);
    const { borderColor } = ctx.themeStyle.input;
    const { fontColor } = ctx.themeStyle.content;
    return (
        <button
            css={
                css`
            border: 1px solid ${borderColor}; 
            color: ${fontColor}; 
            width: ${w};
            height: ${h};
            background: none;
            cursor: pointer;
            &:disabled {
                color: #b6b6b6;
                cursor: not-allowed;
            }
            `
            }
            onClick={onClick}
        >{value}</button>
    );
}

export default Button;