package com.kcs.guestbook.service;

import com.kcs.guestbook.domain.GuestBookDTO;
import com.kcs.guestbook.domain.PageRequestDTO;
import com.kcs.guestbook.domain.PageResponseDTO;
import com.kcs.guestbook.entity.GuestBook;
import com.kcs.guestbook.entity.QGuestBook;
import com.kcs.guestbook.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class GuestBookServiceImpl implements GuestBookService {
    private final GuestBookRepository guestBookRepository;

    @Override
    public PageResponseDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        //페이지 단위 요청을 위한 Pageable 객체를 생성
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);


        //db에서 조회
        Page<GuestBook> result = guestBookRepository.findAll(booleanBuilder,pageable);
        //엔티티를 dto로 변형하기 위한 객체 생성
        Function<GuestBook,GuestBookDTO> fn = (entity -> entityToDTO(entity));
        //데이터 목록 작성
        return new PageResponseDTO<>(result,fn);
    }

    @Override
    public GuestBookDTO read(Long gno) {
        Optional<GuestBook> guestBook = guestBookRepository.findById(gno);

        return guestBook.isPresent()?entityToDTO(guestBook.get()):null;
    }

    @Override
    public Long register(GuestBookDTO dto) {
        log.info("데이터삽입");
        log.info(dto);

        //레포지퇼에서 사용하기 위해 DTO를 엔티티로 변환
        GuestBook entity = dtoToEntity(dto);
        //데이터 삽입
        GuestBook result = guestBookRepository.save(entity);
        //삽입후 리턴받은 데이터의 gno리턴
        return result.getGno();
    }

    //검색 조건을 만들어주는 메서드
    public BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();
        if(keyword!=null) {
            requestDTO.setKeyword(keyword.trim());
            keyword = requestDTO.getKeyword();
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;

        //검색조건이 없는 경우
        if(type==null || type.trim().length()==0)
            return booleanBuilder;
        //검색조건이 있는 경우
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }
}
