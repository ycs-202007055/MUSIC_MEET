import { css } from "@emotion/react";
import React, { useCallback, useState } from "react";
import MusicPlayerContenxt from "store/MusicPlayerContext";
import PlayerContent from "./PlayerContent/PlayerContent";
import PlayerController from "./PlayerController/PlayerController";

interface MusicPlayerProps {
    className?: string;
}

function MusicPlayer(props: MusicPlayerProps) {
    const [list] = useState([
        "http://localhost:8080/musics/play/소녀시대-FOREVER1.mp3",
        "http://localhost:8080/musics/play/한동근-새벽에걸려온너의전화는.mp3",
        "http://localhost:8080/uploads/play/1666766492963_윤하+사건의지평선류민희COVER.mp3"
    ]);
    const [currentIndex, setCurrentIndex] = useState<number>(0);
    const [shownPlayerContent, setShownPlayerContent] = useState<boolean>(false);
    const changeShwonStateHandler = useCallback(() => {
        setShownPlayerContent((prev) => !prev);
    }, []);

    const prevHandler = useCallback(() => {
        setCurrentIndex((prev) => prev > 0 ? prev - 1 : 0);
    }, []);

    const nextHandler = useCallback(() => {
        setCurrentIndex((prev) => prev < list.length - 1 ? prev + 1 : list.length - 1);
    }, [list.length]);
    return (
        <MusicPlayerContenxt.Provider value={{
            isShownContent: shownPlayerContent,
            onChangeShownContentState: changeShwonStateHandler,
            currentMusic: list[currentIndex],
        }}>
            <article className={`${props.className}`} css={root}>
                <PlayerContent
                    className={`${shownPlayerContent ? "shown" : "hidden"} player-content`}
                />
                <PlayerController
                    className={"player-controller"}
                    playingMusic={list[currentIndex]}
                    prev={prevHandler}
                    next={nextHandler}
                />
            </article>
        </MusicPlayerContenxt.Provider >


    );
}

const root = css`

    display: flex;
    /* flex-wrap: wrap; */
    flex-direction: column;
    width: 100%;
    height: 100%;
    .shown {
        /* height: calc(100%);  */
        height: 100%;
    }

    .hidden {
        height: 0;
        overflow: hidden;
    }

    & > .player-content {
        position: fixed;
        padding-bottom: 5rem;
        bottom: 0;
        z-index: 2000;
    }

    & > .player-controller {
        position: fixed;
        bottom: 0;
        max-height: 5rem;
        z-index: 2000;
    }
`;



export default React.memo(MusicPlayer);