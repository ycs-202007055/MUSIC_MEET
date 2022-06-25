import React, { useCallback, useState } from "react";
import Input from "components/common/Input";
import { css } from "@emotion/react";
import Submit from "components/common/Submit";
import { useTranslation } from "react-i18next";
import useAxios from "hooks/use-Axios";
function Id() {
    const { t } = useTranslation("findPage");
    const [email, setEmail] = useState("");
    const { fetchData } = useAxios({
        url: "/findid",
        method: "POST",
        body: {
            email: email
        }
    });
    const onChangeHandler = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setEmail(e.target.value);
    }, []);
    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        fetchData()
            .then(() => {
                // then
            })
            .catch(() => {
                // catch
            });
    };

    return (
        <section css={[style]} >
            <p>{t("id.ment")}</p>
            <form onSubmit={onSubmit}>
                <Input
                    type="email"
                    w={"25rem"}
                    h={"2.5rem"}

                    value={email}
                    input={{
                        id: "email",
                        placeholder: t("id.placeholder"),
                        type: "email",
                        onChange: onChangeHandler
                    }}
                />
                <Submit
                    w={"5rem"}
                    h={"2.5rem"}
                    value={t("id.submit")}
                />
            </form>
        </section >
    );
}

const style = css`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    p { 
        margin-bottom: 1rem;
    }
`;

export default Id;