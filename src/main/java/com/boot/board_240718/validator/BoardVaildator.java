package com.boot.board_240718.validator;

import com.boot.board_240718.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class BoardVaildator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        return Board.class.equals(clazz);

    }

    @Override
    public void validate(Object obj, Errors e) {
        log.info("@# vaildate()");

        Board b = (Board) obj;
        log.info("@# b=>"+b);

        if (b.getContent().equals("")){ //.equals("") : 빈 값을 안가져오기 위해서
            // 내용이 null일 때
            // errorCode는 아무렇게 작성
            e.rejectValue("content","key","내용을 입력허세요");
        }
    }
//        try {
//            errors.pushNestedPath("address");
//            ValidationUtils.invokeValidator(this.addressValidator, customer.getAddress(), errors);
//        } finally {
//            errors.popNestedPath();
//        }
//    }
}
