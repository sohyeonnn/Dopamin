package com.PSVM.dopamin.service.contents;

import com.PSVM.dopamin.domain.contents.ContentsDto;
import com.PSVM.dopamin.domain.contents.ContentsUserDto;
import com.PSVM.dopamin.domain.contents.ContentsWishDto;
import com.PSVM.dopamin.domain.SearchCondition;

import java.util.List;

public interface ContentsService {

    List<ContentsDto> contentsList() ;

    List<ContentsUserDto> selectUserAllCnts(String user_id);

    ContentsUserDto selectUserId(String user_id);

    ContentsDto contentsView(Integer cnts_id);

    List<ContentsDto> getSearchCntsPage(SearchCondition sc);

    int getSearchResultCnt(SearchCondition sc);

    int insertWish(ContentsWishDto contentsWishDto) throws Exception;

    int deleteWish(Integer cnts_id, String user_id) throws Exception;

}