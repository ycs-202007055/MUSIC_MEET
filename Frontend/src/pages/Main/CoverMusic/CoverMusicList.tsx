import React, { useCallback, useState } from "react";
import { useQuery } from "react-query";
import MusicList from "../MusicList";
import fetchCoverMusic from "utils/RequestApis/Main/fetchCoverMusic";

interface CoverMusicListProps {
    type: "latest" | "popular";
}

function CoverMusicList(props: CoverMusicListProps) {
    const { data } = useQuery(["fetchCoverMusic", props.type], () => fetchCoverMusic({ type: props.type, count: 20 }));
    return (
        <React.Fragment>
            <MusicList
                list={data}
            />
        </React.Fragment>
    );
}

export default CoverMusicList;