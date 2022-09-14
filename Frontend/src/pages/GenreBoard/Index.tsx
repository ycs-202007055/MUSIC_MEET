import Content from "components/UI/Content";
import React from "react";
import { Route, Routes } from "react-router-dom";
import Board from "./Board/Board";
import Write from "./Write/Write";
import View from "./View/View";
import Edit from "./Edit/Edit";
import PrivateRoute from "components/common/PrivateRoute";
function Index() {
    return (
        <Content>

            <Routes>
                <Route path="/" element={<Board />} />
                <Route path="/:genre" element={<Board />} />
                <Route path=":genre/write" element={<PrivateRoute RouteComponent={Write} />} />
                <Route path=":genre/edit/:id" element={<PrivateRoute RouteComponent={Edit} />} />
                <Route path=":genre/post/:num" element={<View />} />
            </Routes>
        </Content >

    );
}

export default React.memo(Index);