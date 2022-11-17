package com.PSVM.dopamin.dao.contents;

import com.PSVM.dopamin.domain.*;
import com.PSVM.dopamin.domain.contents.ContentsDto;
import com.PSVM.dopamin.domain.contents.ContentsUserDto;
import com.PSVM.dopamin.domain.contents.ContentsWishDto;

import java.util.List;

public interface ContentsDao {
    List<ContentsDto> selectAllCnts();

    List<ContentsUserDto> selectUserAllCnts(String user_id);

    ContentsUserDto selectUserId(String user_id);

    ContentsDto selectCnts(Integer cnts_id);

    List<ContentsDto> searchSelectCnts(SearchCondition sc);

    int searchResultCnt(SearchCondition sc);

    /*
    //컨텐츠 평가
    CntsEvalDto evalCheck(Integer cnts_id, String user_id);*/

    //컨텐츠 찜
    int insertWish(ContentsWishDto contentsWishDto) throws Exception;

    int deleteWish(Integer cnts_id, String user_id) throws Exception;

    void insertWish(Integer cnts_id, int i);
}
