package com.PSVM.dopamin.dao;

import com.PSVM.dopamin.dao.contents.ContentsDao;
import com.PSVM.dopamin.domain.contents.ContentsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
//@WebAppConfiguration
public class ContentsDaoImplTest {

    @Autowired
    ContentsDao contentsDao;

    //데이터 접근 테스트(stat=0에 접근하려는 경우 수정중.....)
    @Test
    public void selectcnts() throws Exception {
        List<ContentsDto> list = contentsDao.selectAllCnts();
        assertTrue(contentsDao != null);
        System.out.println("contentsDao = " + list);
    }


}