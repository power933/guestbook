package com.kcs.guestbook;

import com.kcs.guestbook.entity.GuestBook;
import com.kcs.guestbook.entity.QGuestBook;
import com.kcs.guestbook.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private GuestBookRepository guestBookRepository;

    @Test
    public void insertTest(){
        IntStream.rangeClosed(1,300).forEach(i -> {
            GuestBook guestbook = GuestBook.builder()
                    .title("Title...."+i)
                    .content("Content..."+i)
                    .writer("user"+(i%10))
                    .build();
            guestBookRepository.save(guestbook);
        });
    }

    @Test
    public void updateTest(){
        Optional<GuestBook> result = guestBookRepository.findById(105L);
        if(result.isPresent()){
            GuestBook guestBook = result.get();
            guestBook.changeTitle("제목 변경");
            guestBook.changeContent("내용 수정");
            guestBookRepository.save(guestBook);

        }else{

        }
    }
    @Test
    public void deleteTest(){
        Optional<GuestBook> result = guestBookRepository.findById(106L);
        if(result.isPresent()){
            guestBookRepository.delete(result.get());
        }
        else{
            System.out.println("nodata");
        }
    }
    @Test
    public void selectAllTest(){
        List<GuestBook> list = guestBookRepository.findAll();
        for(GuestBook guestBook : list){
            System.out.println(guestBook);
        }
    }
    @Test
    public void selectOne(){
        Optional<GuestBook> guestBook = guestBookRepository.findById(105L);
        if(guestBook.isPresent()){
            System.out.println(guestBook.get());
        }
    }
    @Test
    public void dslSelect(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());
        //엔티티에 동적 쿼리를 수행할 수 있는 도메인 클래스를 찾아오기
        //쿼리dsl설정을 해야 사용가능
        //컬럼들을 속성으로 포함시켜 조건을 설정하는 것이 가능
        QGuestBook qGuestBook = QGuestBook.guestBook;
        //검색어 생성
        String keyword = "1";
        //검색을 적용하기 위한 빌더객체 생성
        BooleanBuilder builder = new BooleanBuilder();
        //조건 표현식 생성
        BooleanExpression expression = qGuestBook.title.contains(keyword);
        BooleanExpression expression2 = qGuestBook.content.contains(keyword);
        BooleanExpression ex = expression.or(expression2);
        //검색 객체에 표현식을 추가
        builder.and(ex);
        Page<GuestBook> result = guestBookRepository.findAll(builder,pageable);
        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }
}
