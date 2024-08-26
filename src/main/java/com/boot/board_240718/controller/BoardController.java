package com.boot.board_240718.controller;

import com.boot.board_240718.model.Board;
import com.boot.board_240718.repository.BoardRepository;
import com.boot.board_240718.service.BoardService;
import com.boot.board_240718.validator.BoardVaildator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.List;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardVaildator boardVaildator;

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
//    public String list(Model model){
//    public String list(Model model, Pageable pageable){
//    public String list(Model model,@PageableDefault(size = 2) Pageable pageable){
//    public String list(Model model,@PageableDefault(size = 2) Pageable pageable, @RequestParam String searchText){
    public String list(Model model,@PageableDefault(size = 2) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText){
        log.info("@# list()");
        log.info("@# searchText()"+searchText);
//        List<Board> boards = boardRepository.findAll();
//        Page<Board> boards = boardRepository.findAll(PageRequest.of(1, 20));
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContent(searchText, searchText, pageable);
//      최종: 5개 이전 페이지 표시 + 5개 이후 페이지 표시
//        int startPage = Math.max(0, boards.getPageable().getPageNumber() -4);//0, -4 max => 0 ==음수가 안되게
        int startPage = Math.max(1, boards.getPageable().getPageNumber() -4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() +4);

//        log.info("@# boards=>"+boards);
//        boards.getTotalElements();//총 건수->list.html

//        model.addAttribute(attributeName: "boards",boards);
        model.addAttribute("startPage",startPage);//출력을 위해서 model에 넣어줌
        model.addAttribute("endPage",endPage);
        model.addAttribute("boards",boards);
    
        return "board/list";
    }

    @GetMapping("/form")
//    public String form(Model model){
//    public String form(Model model, @RequestParam Long id){
    public String form(Model model, @RequestParam(required = false) Long id){
        log.info("@# GetMapping form()");
        log.info("@# id=>"+id);


        if (id != null){
//            Board board = boardRepository.findById(id);
//            Optional<Board> board = boardRepository.findById(id);
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board",board);
            log.info("@# board=>"+board);
            //id가 null일때
        }else {
            model.addAttribute("board",new Board());//객체만 가져가게끔
        }

        return "board/form";
    }

    @PostMapping("/form") //글작성
//    public String form(@ModelAttribute Board board, Model model) {
    public String form(@Valid Board board, BindingResult bindingResult) {
            boardVaildator.validate(board, bindingResult);

            if (bindingResult.hasErrors()) {
            return "board/form";
        }
            //스프링 security 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();
//        model.addAttribute("greeting", board);
//        boardRepository.save(board);//저장
        boardService.save(username,board);//security
        return "redirect:/board/list";
    }
}
