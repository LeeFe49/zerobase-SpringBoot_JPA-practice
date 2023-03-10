package com.example.practice.board.service;

import com.example.practice.board.common.model.ResponseResult;
import com.example.practice.board.entity.Board;
import com.example.practice.board.entity.BoardBadReport;
import com.example.practice.board.entity.BoardBookmark;
import com.example.practice.board.entity.BoardComment;
import com.example.practice.board.entity.BoardHits;
import com.example.practice.board.entity.BoardLike;
import com.example.practice.board.entity.BoardScrap;
import com.example.practice.board.entity.BoardType;
import com.example.practice.board.model.BoardBadReportInput;
import com.example.practice.board.model.BoardPeriod;
import com.example.practice.board.model.BoardTypeCount;
import com.example.practice.board.model.BoardTypeInput;
import com.example.practice.board.model.BoardTypeUsing;
import com.example.practice.board.model.ServiceResult;
import com.example.practice.board.repository.BoardBadReportRepository;
import com.example.practice.board.repository.BoardBookmarkRepository;
import com.example.practice.board.repository.BoardCommentRepository;
import com.example.practice.board.repository.BoardHitsRepository;
import com.example.practice.board.repository.BoardLikeRepository;
import com.example.practice.board.repository.BoardRepository;
import com.example.practice.board.repository.BoardScrapRepository;
import com.example.practice.board.repository.BoardTypeCustomRepository;
import com.example.practice.board.repository.BoardTypeRepository;
import com.example.practice.common.exception.BizException;
import com.example.practice.user.entity.User;
import com.example.practice.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardTypeRepository boardTypeRepository;
    private final BoardRepository boardRepository;
    private final BoardTypeCustomRepository boardTypeCustomRepository;
    private final BoardHitsRepository boardHitsRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardBadReportRepository boardBadReportRepository;
    private final BoardScrapRepository boardScrapRepository;
    private final BoardBookmarkRepository boardBookmarkRepository;
    private final BoardCommentRepository boardCommentRepository;

    public ServiceResult addBoard(BoardTypeInput boardTypeInput) {

        BoardType boardType = boardTypeRepository.findByBoardName(boardTypeInput.getName());

        if (boardType != null && boardTypeInput.getName().equals(boardType.getBoardName())) {
            return ServiceResult.fail("?????? ????????? ???????????? ???????????????.");
        }

        BoardType addBoardType = BoardType.builder()
            .boardName(boardTypeInput.getName())
            .regDate(LocalDateTime.now())
            .build();

        boardTypeRepository.save(addBoardType);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult updateBoard(long id, BoardTypeInput boardTypeInput) {

        Optional<BoardType> optionalBoardType = boardTypeRepository.findById(id);
        if (!optionalBoardType.isPresent()) {
            return ServiceResult.fail("????????? ?????????????????? ????????????.");
        }

        BoardType boardType = optionalBoardType.get();

        if (boardType.getBoardName().equals(boardTypeInput.getName())) {
            return ServiceResult.fail("????????? ????????? ????????? ???????????? ?????????.");
        }

        boardType.setBoardName(boardTypeInput.getName());
        boardType.setUpdateDate(LocalDateTime.now());
        boardTypeRepository.save(boardType);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult deleteBoard(Long id) {

        Optional<BoardType> optionalBoardType = boardTypeRepository.findById(id);
        if (!optionalBoardType.isPresent()) {
            return ServiceResult.fail("????????? ?????????????????? ????????????.");
        }

        BoardType boardType = optionalBoardType.get();

        if (boardRepository.countByBoardType(boardType) > 0) {
            return ServiceResult.fail("????????? ?????????????????? ???????????? ???????????????.");
        }

        boardTypeRepository.delete(boardType);

        return ServiceResult.success();
    }

    @Override
    public List<BoardType> getAllBoardType() {

        return boardTypeRepository.findAll();
    }

    @Override
    public ServiceResult setBoardTypeUsing(Long id, BoardTypeUsing boardTypeUsing) {

        Optional<BoardType> optionalBoardType = boardTypeRepository.findById(id);
        if (!optionalBoardType.isPresent()) {
            return ServiceResult.fail("????????? ?????????????????? ????????????.");
        }

        BoardType boardType = optionalBoardType.get();

        boardType.setUsingYn(boardTypeUsing.isUsingYn());
        boardTypeRepository.save(boardType);

        return ServiceResult.success();
    }

    @Override
    public List<BoardTypeCount> getBoardTypeCount() {

        return boardTypeCustomRepository.getBoardTypeCount();
    }

    @Override
    public ServiceResult setBoardTop(Long id, boolean topYn) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }

        Board board = optionalBoard.get();
        if (board.isTopYn() == topYn) {
            if(topYn) {
                return ServiceResult.fail("?????? ???????????? ???????????? ???????????? ????????????.");
            }else {
                return ServiceResult.fail("?????? ???????????? ????????? ????????? ???????????? ????????????.");
            }
        }

        board.setTopYn(topYn);
        boardRepository.save(board);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardPeriod(Long id, BoardPeriod boardPeriod) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }
        Board board = optionalBoard.get();

        board.setPublishStartDate(boardPeriod.getStartDate());
        board.setPublishEndDate(boardPeriod.getEndDate());
        boardRepository.save(board);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardHits(Long id, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }
        Board board = optionalBoard.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        if (boardHitsRepository.countByBoardAndUser(board, user) > 0) {
            return ServiceResult.fail("?????? ???????????? ????????????.");
        }

        boardHitsRepository.save(BoardHits.builder()
            .board(board)
            .user(user)
            .regDate(LocalDateTime.now())
            .build());
        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardLike(Long id, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }
        Board board = optionalBoard.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        long boardLikeCount = boardLikeRepository.countByBoardAndUser(board, user);
        if (boardLikeCount > 0) {
            return ServiceResult.fail("?????? ???????????? ????????? ????????????.");
        }

        boardLikeRepository.save(BoardLike.builder()
            .board(board)
            .user(user)
            .regDate(LocalDateTime.now()).build());

        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardUnLike(Long id, String email) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }
        Board board = optionalBoard.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndUser(board, user);
        if (!optionalBoardLike.isPresent()) {
            return ServiceResult.fail("???????????? ????????? ????????????.");
        }
        BoardLike boardLike = optionalBoardLike.get();

        boardLikeRepository.delete(boardLike);
        return ServiceResult.success();
    }

    @Override
    public ServiceResult badReport(Long id, String email, BoardBadReportInput boardBadReportInput) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }
        Board board = optionalBoard.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        BoardBadReport boardBadReport = BoardBadReport.builder()
            .userId(user.getId())
            .userName(user.getUserName())
            .userEmail(user.getEmail())
            .boardId(board.getId())
            .boardUserId(board.getUser().getId())
            .boardTitle(board.getTitle())
            .boardContents(board.getContents())
            .boardRegDate(board.getRegDate())
            .comments(boardBadReportInput.getComment())
            .regDate(LocalDateTime.now()).build();
        boardBadReportRepository.save(boardBadReport);

        return ServiceResult.success();
    }

    @Override
    public List<BoardBadReport> badReportList() {

        return boardBadReportRepository.findAll();
    }

    @Override
    public ServiceResult scrapBoard(Long id, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }
        Board board = optionalBoard.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        BoardScrap boardScrap = BoardScrap.builder()
            .user(user)
            .boardId(board.getId())
            .boardTypeId(board.getBoardType().getId())
            .boardContents(board.getContents())
            .boardRegDate(board.getRegDate())
            .regDate(LocalDateTime.now())
            .build();

        boardScrapRepository.save(boardScrap);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult removeScrap(Long id, String email) {

        Optional<BoardScrap> optionalBoardScrap = boardScrapRepository.findById(id);
        if (!optionalBoardScrap.isPresent()) {
            return ServiceResult.fail("????????? ???????????? ????????????.");
        }
        BoardScrap boardScrap = optionalBoardScrap.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        if (user.getId() != boardScrap.getUser().getId()) {
            return ServiceResult.fail("????????? ???????????? ????????? ??? ????????????.");
        }

        boardScrapRepository.delete(boardScrap);
        return ServiceResult.success();
    }

    private String getBoardUrl(long boardId) {
        return String.format("/board/%d", boardId);
    }

    @Override
    public ServiceResult addBookmark(Long id, String email) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("???????????? ???????????? ????????????.");
        }
        Board board = optionalBoard.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        BoardBookmark boardBookmark = BoardBookmark.builder()
            .user(user)
            .boardId(board.getId())
            .boardTypeId(board.getBoardType().getId())
            .boardTitle(board.getTitle())
            .boardUrl(getBoardUrl(board.getId()))
            .regDate(LocalDateTime.now())
            .build();

        boardBookmarkRepository.save(boardBookmark);
        return ServiceResult.success();
    }

    @Override
    public ServiceResult removeBookmark(Long id, String email) {
        Optional<BoardBookmark> optionalBoardBookmark = boardBookmarkRepository.findById(id);
        if (!optionalBoardBookmark.isPresent()) {
            return ServiceResult.fail("????????? ???????????? ????????????.");
        }
        BoardBookmark boardBookmark = optionalBoardBookmark.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        if (user.getId() != boardBookmark.getUser().getId()) {
            return ServiceResult.fail("????????? ???????????? ????????? ??? ????????????.");
        }

        boardBookmarkRepository.delete(boardBookmark);
        return ServiceResult.success();
    }

    @Override
    public List<Board> postList(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new BizException("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        List<Board> list = boardRepository.findByUser(user);
        return list;
    }

    @Override
    public List<BoardComment> commentList(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new BizException("?????? ????????? ???????????? ????????????.");
        }
        User user = optionalUser.get();

        List<BoardComment> list = boardCommentRepository.findByUser(user);
        return list;
    }
}
