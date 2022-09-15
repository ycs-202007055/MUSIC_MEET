package com.example.music_meet.service;

import com.example.music_meet.dto.Request.*;
import com.example.music_meet.dto.Response.Response_GetGenreBoardList;
import com.example.music_meet.dto.Response.Response_searchGenreBoard;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class BoardService
{
    @Value("${spring.datasource.url}")
    private String mysqlurl;
    @Value("${spring.datasource.username}")
    private String mysqlid;
    @Value("${spring.datasource.password}")
    private String mysqlpassword;
    @Value("${spring.datasource.driver-class-name}")
    private String classForName;

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    private int rsInt = 0;
    private String sql;


    //
    //  글을 DB에 저장하는 함
    //
    public void WriteGenreBoard(Request_WriteGenreBoard requestWriteGenreBoard)
    {
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        final String genreBoard = requestWriteGenreBoard.getGenre() + "board";
        try
        {
            sql = "INSERT INTO " + genreBoard + "(title, usernum, content, createdat) VALUES(?,?,?,?) ";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            //pstmt.setString(1, board);
            pstmt.setString(1, requestWriteGenreBoard.getTitle());
            pstmt.setInt(2, requestWriteGenreBoard.getUsernum());
            pstmt.setString(3, requestWriteGenreBoard.getContent());
            pstmt.setTimestamp(4, date);

            rsInt = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //
    // 해당 글 번호 가져오는 함수
    //
    public Map<String, String> getBoardForGenreNum(String genre, String num)
    {
        Map<String, String> responseMap = new HashMap<>();
        final String genreBoard = genre + "board";
        try
        {
             sql = "SELECT a.userimage, a.nickname, b.title, b.content, DATE_FORMAT(b.`createdat`, '%y-%m-%d %T') AS createdat, b.`view`, b.upvote, b.downvote FROM user a, "
                     + genreBoard + " b WHERE a.usernum = b.usernum AND b.boardnum = ? AND b.`state` = 0;";

            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(num));

            rs = pstmt.executeQuery();
            if(rs.next())
            {
                responseMap.put("userimage", rs.getString("userimage"));
                responseMap.put("nickname", rs.getString("nickname"));
                responseMap.put("title", rs.getString("title"));
                responseMap.put("content", rs.getString("content"));
                responseMap.put("createdAt", rs.getString("createdat"));
                responseMap.put("view", rs.getString("view"));
                responseMap.put("upvote", rs.getString("upvote"));
                responseMap.put("downvote", rs.getString("downvote"));

                sql = "UPDATE " + genreBoard + " SET `view` = `view` + 1 WHERE boardnum = " + num;

                rsInt = pstmt.executeUpdate(sql);

            }
            else
            {
                responseMap.put("userimage", "NoData");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return responseMap;
    }

    //
    //  해당 글 수정
    //
    public Boolean ModifyGenreBoard(Request_ModifyGenreBoard request_modifyGenreBoard)
    {
        boolean responseBoolean = false;
        final String genreboard = request_modifyGenreBoard.getGenre() + "board";
        try{
            sql = "UPDATE " + genreboard + " SET title = ? , content = ? WHERE boardnum = ? AND state = 0 AND usernum = ?";
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl,mysqlid,mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, request_modifyGenreBoard.getTitle());
            pstmt.setString(2, request_modifyGenreBoard.getContent());
            pstmt.setString(3, request_modifyGenreBoard.getBoardNum());
            pstmt.setString(4, request_modifyGenreBoard.getUserNum());

            rsInt = pstmt.executeUpdate();

            if (rsInt != 0)
            {
                responseBoolean = true;
            }
            else {
                responseBoolean = false;
            }
        }
        catch (Exception e){

        }
        finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return responseBoolean;
    }

    //
    //  해당 글 삭제
    //
    public void DeleteGenreBoard(Request_DeleteGenreBoard request_deleteGenreBoard)
    {
        final String genreBoard = request_deleteGenreBoard.getGenre() + "board";
        final int boardNum = Integer.parseInt(request_deleteGenreBoard.getBoardNum());
        final int userNum = Integer.parseInt(request_deleteGenreBoard.getUserNum());
        try
        {
            sql = "UPDATE " + genreBoard + " SET state = 1 WHERE boardnum = ? AND usernum = ? AND state = 0";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,boardNum);
            pstmt.setInt(2,userNum);
            rsInt = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //
    // 장르 게시판 글 목록 호출
    //
    public ArrayList<Response_GetGenreBoardList> getGenreBoarList(Request_GetGenreBoardList request_getGenreBoardList)
    {
        final String genreBoard = request_getGenreBoardList.getGenre() + "board";
        final int min = request_getGenreBoardList.getMin();
        final int max = request_getGenreBoardList.getMax();
        ArrayList<Response_GetGenreBoardList> boards = new ArrayList<Response_GetGenreBoardList>();
        try
        {
            sql = "SELECT a.title, a.usernum, a.boardnum, a.createdat, a.`view`, a.upvote, a.downvote, b.nickname FROM " + genreBoard +
                    " a, user b WHERE a.usernum = b.usernum AND a.state = 0 ORDER BY a.boardnum DESC " +
                    " LIMIT ?,?";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, min);
            pstmt.setInt(2, max);
            rs = pstmt.executeQuery();

            for (int i = 0; rs.next(); i++)
            {
                Response_GetGenreBoardList response_getGenreBoardList= new Response_GetGenreBoardList();
                response_getGenreBoardList.setTitle(rs.getString("title"));
                response_getGenreBoardList.setBoardNum(rs.getInt("boardnum"));
                response_getGenreBoardList.setNickname(rs.getString("nickname"));
                response_getGenreBoardList.setCreatedAt(rs.getString("createdat"));
                response_getGenreBoardList.setView(rs.getInt("view"));
                response_getGenreBoardList.setVote(rs.getInt("upvote") - rs.getInt("downvote"));
                boards.add(response_getGenreBoardList);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return boards;
    }


    //
    // 장르 게시판 추천, 비추천 추가
    //
    public Boolean genreBoardPlusVote(String sql ,Request_GenreBoardVote request_genreBoardVote)
    {
        boolean result = false;
        final int boardNum = request_genreBoardVote.getBoardNum();
        try
        {
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, boardNum);

            rsInt = pstmt.executeUpdate();
            if (rsInt != 0)
                result = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    //
    // 장르 게시판 글 호출_Small
    //
    public Map<String , String> getGenreBoard_Small(String genre, int boardNum)
    {
        final String genreBoard = genre + "board";
        Map<String , String> responseMap = new HashMap<>();
        try
        {
            sql = "SELECT a.title, a.content, b.nickname FROM " + genreBoard + " a, user b WHERE a.usernum = b.usernum " +
                    " AND a.boardnum = ? AND a.state = 0";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, boardNum);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                responseMap.put("title", rs.getString("title"));
                responseMap.put("content", rs.getString("content"));
                responseMap.put("nickname", rs.getString("nickname"));
            }
            else
                responseMap.put("title", null);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return responseMap;
    }


    //
    // 장르BoardCommentVote 테이블에서 값 확인
    //
    public int isSelectVote(String userNum, Request_GenreBoardVote request_genreBoardVote)
    {
        int voteState = 2;
        final String genreBoardVote = request_genreBoardVote.getGenre() + "BoardVote";

        final int boardNum = request_genreBoardVote.getBoardNum();
        try
        {
            sql = "SELECT vote FROM " + genreBoardVote + " WHERE  usernum = ? AND boardnum = ?";

            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(userNum));
            pstmt.setInt(2, boardNum);

            rs = pstmt.executeQuery();

            if (rs.next())
                voteState = rs.getInt("vote");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return voteState;
    }


    //
    // 장르BoardVote 테이블에 값 삽입
    //
    public void insertVoteTable(String userNum, Request_GenreBoardVote request_genreBoardVote)
    {
        final int boardNum = request_genreBoardVote.getBoardNum();
        final String genreBoardVote = request_genreBoardVote.getGenre() + "BoardVote";
        final int voteStateCode;
        if (request_genreBoardVote.getVote().equals("upvote"))
            voteStateCode = 0;
        else
            voteStateCode = 1;

        try
        {
            sql = "INSERT INTO " + genreBoardVote + "(usernum, boardnum, vote) VALUES(?,?,?)";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(userNum));
            pstmt.setInt(2, boardNum);
            pstmt.setInt(3, voteStateCode);

            rsInt = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    //
    // 제목으로 검색
    //
    public ArrayList<Response_searchGenreBoard> searchGenreBoard_Title(String genre,String title)
    {
        ArrayList<Response_searchGenreBoard> response_searchGenreBoards = new ArrayList<>();
        final String genreBoard = genre + "board";
        try
        {
            sql = "SELECT a.boardnum, a.title, b.nickname, a.createdat, a.`view`, a.upvote, a.downvote FROM " + genreBoard + " a, user b WHERE a.title LIKE ? AND a.state = 0 AND a.usernum = b.usernum";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, "%" + title + "%");

            rs = pstmt.executeQuery();
            for(int i = 0; rs.next(); i++)
            {
                Response_searchGenreBoard response_searchGenreBoard = new Response_searchGenreBoard();
                response_searchGenreBoard.setBoardNum(rs.getInt("boardnum"));
                response_searchGenreBoard.setTitle(rs.getString("title"));
                response_searchGenreBoard.setNickname(rs.getString("nickname"));
                response_searchGenreBoard.setCreatedAt(rs.getTime("createdat").toString());
                response_searchGenreBoard.setView(rs.getInt("view"));
                response_searchGenreBoard.setUpvote(rs.getInt("upvote"));
                response_searchGenreBoard.setDownvote(rs.getInt("downvote"));
                response_searchGenreBoards.add(response_searchGenreBoard);

            }

            if (response_searchGenreBoards.size() == 0)
            {
                response_searchGenreBoards = null;
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return response_searchGenreBoards;

    }

    public ArrayList<Response_searchGenreBoard> searchGenreBoard_Nickname(String genre, String nickname)
    {
        ArrayList<Response_searchGenreBoard> response_searchGenreBoards = new ArrayList<>();
        final String genreBoard = genre + "board";
        try
        {
            sql = "SELECT a.boardnum, a.title, b.nickname, a.createdat, a.`view`, a.upvote, a.downvote FROM " + genreBoard + " a, user b WHERE b.nickname LIKE ? AND a.state = 0 AND a.usernum = b.usernum";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, "%" + nickname + "%");

            rs = pstmt.executeQuery();
            for(int i = 0; rs.next(); i++)
            {
                Response_searchGenreBoard response_searchGenreBoard = new Response_searchGenreBoard();
                response_searchGenreBoard.setBoardNum(rs.getInt("boardnum"));
                response_searchGenreBoard.setTitle(rs.getString("title"));
                response_searchGenreBoard.setNickname(rs.getString("nickname"));
                response_searchGenreBoard.setCreatedAt(rs.getTime("createdat").toString());
                response_searchGenreBoard.setView(rs.getInt("view"));
                response_searchGenreBoard.setUpvote(rs.getInt("upvote"));
                response_searchGenreBoard.setDownvote(rs.getInt("downvote"));
                response_searchGenreBoards.add(response_searchGenreBoard);
            }

            if (response_searchGenreBoards.size() == 0)
            {
                response_searchGenreBoards = null;
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return response_searchGenreBoards;
    }

    public ArrayList<Response_searchGenreBoard> searchGenreBoard_TitleAndNickname(String genre, String text)
    {
        ArrayList<Response_searchGenreBoard> response_searchGenreBoards = new ArrayList<>();
        final String genreBoard = genre + "board";
        try
        {
            sql = "SELECT a.boardnum, a.title, b.nickname, a.createdat, a.`view`, a.upvote, a.downvote FROM " + genreBoard + " a, user b WHERE (b.nickname LIKE ? OR a.title LIKE ? )AND a.state = 0 AND a.usernum = b.usernum";
            //
            // DB구간
            //
            Class.forName(classForName);
            conn = DriverManager.getConnection(mysqlurl, mysqlid, mysqlpassword);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, "%" + text + "%");
            pstmt.setString(2, "%" + text + "%");

            rs = pstmt.executeQuery();
            for(int i = 0; rs.next(); i++)
            {
                Response_searchGenreBoard response_searchGenreBoard = new Response_searchGenreBoard();
                response_searchGenreBoard.setBoardNum(rs.getInt("boardnum"));
                response_searchGenreBoard.setTitle(rs.getString("title"));
                response_searchGenreBoard.setNickname(rs.getString("nickname"));
                response_searchGenreBoard.setCreatedAt(rs.getTime("createdat").toString());
                response_searchGenreBoard.setView(rs.getInt("view"));
                response_searchGenreBoard.setUpvote(rs.getInt("upvote"));
                response_searchGenreBoard.setDownvote(rs.getInt("downvote"));
                response_searchGenreBoards.add(response_searchGenreBoard);
            }

            if (response_searchGenreBoards.size() == 0)
            {
                response_searchGenreBoards = null;
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return response_searchGenreBoards;
    }
}
