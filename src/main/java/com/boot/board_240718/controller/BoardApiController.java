package com.boot.board_240718.controller;

import com.boot.board_240718.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BoardApiController {

    private static final Logger log = LoggerFactory.getLogger(BoardApiController.class);
    @Autowired
    private BoardRepository boardRepository;

    //메소드 작성
//    public void deleteBoard(){ 강사님이 작성
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/boards/{id}")//스프링 레거시 했을 때 사용
    public void deleteBd(@PathVariable Long id){
        log.info("@# deleteBoard()");

        boardRepository.deleteById(id);//jpa-쿼리 만들 필요없이 id 삭제,이걸 호출 할려면 form.html
    }
}
