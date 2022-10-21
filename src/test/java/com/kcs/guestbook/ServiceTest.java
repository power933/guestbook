package com.kcs.guestbook;

import com.kcs.guestbook.domain.GuestBookDTO;
import com.kcs.guestbook.domain.PageRequestDTO;
import com.kcs.guestbook.domain.PageResponseDTO;
import com.kcs.guestbook.entity.GuestBook;
import com.kcs.guestbook.repository.GuestBookRepository;
import com.kcs.guestbook.service.GuestBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private GuestBookService guestBookService;

    @Test
    public void insertTest(){
        GuestBookDTO dto = GuestBookDTO.builder()
                .title("삽입테스트")
                .content("서비스 테스트")
                .writer("서비스단 작성자")
                .build();
        Long gno = guestBookService.register(dto);
        System.out.println("gno = " + gno);
    }

    @Test
    public void listTest(){
        //페이지 번호와 데이터 개수 설정
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<GuestBookDTO, GuestBook> resultDTO = guestBookService.getList(pageRequestDTO);
        for(GuestBookDTO dto : resultDTO.getDtoList()){
            System.out.println(dto);
        }
        System.out.println("이전 여부 : " + resultDTO.isPrev());
        System.out.println("다음 여부 : " + resultDTO.isNext());
        System.out.println("전체 페이지 개수 : " + resultDTO.getDtoList());
        resultDTO.getPageList().forEach(i->{
            System.out.println(i);
        });
    }

}
