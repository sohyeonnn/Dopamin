package com.PSVM.dopamin.service.mypage;

import com.PSVM.dopamin.domain.mypage.*;

import java.util.List;

public interface MyPageService {
    List<MyPageRevwDto> revwList(String user_id) throws Exception;

    List<MyPageRevwDto> revwRtList(String user_id);

    List<MyPageCntsDto> cntsWishList(String user_id);

    List<MyPageCntsDto> cntsViewList(String user_id);

    List<MyPagePostDto> postList(String user_id);

    MyPageDto selectMyInfo(String user_id);

    List<MyPageItemsDto> profList(String user_id);

    List<MyPageItemsDto> skinList(String user_id);

    List<MyPagePointDto> pntList(String user_id);

    void modSkin(MyPageItemsDto myPageItemsDto) throws Exception;

    void modProf(MyPageItemsDto myPageItemsDto)throws Exception;

    void defaultSkin(MyPageItemsDto myPageItemsDto);

    void defaultProf(MyPageItemsDto myPageItemsDto);

    int deleterevw(List<Integer> valueArr) throws Exception;

    int deletePost(List<Integer> valueArr) throws Exception;
}
