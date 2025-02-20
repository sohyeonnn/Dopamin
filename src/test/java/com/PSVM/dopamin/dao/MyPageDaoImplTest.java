//package com.PSVM.dopamin.dao;
//
//import com.PSVM.dopamin.domain.MyPageCntsDto;
//import com.PSVM.dopamin.domain.MyPageDto;
//import com.PSVM.dopamin.domain.MyPagePostDto;
//import com.PSVM.dopamin.domain.RevwDto;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
//public class MyPageDaoImplTest {
//    @Autowired
//    MyPageDao myPageDao;
//
//    @Test
//    public void selectRevwRt() throws Exception {
//        List<RevwDto> list = myPageDao.selectRevwRt("eunbi");
//        String title = list.get(0).getCnts_title();
//        System.out.println("title = " + title);
//        for (int i = 0; i < list.size(); i++)
//            System.out.println(list.get(i).getUser_id());
//        assertTrue(list.size() == 3);
//    }
//
//    @Test
//    public void cntsWishList() throws Exception {
//        List<MyPageCntsDto> list = myPageDao.selectCntsWish("hohoma5");
//        for (int i = 0; i < list.size(); i++) {
//            String test = list.get(i).getCnts_postr_img();
//            System.out.println("test = " + test);
//        }
//        assertTrue(list.size() == 1);
//    }
//
//    @Test
//    public void cntsViewList() throws Exception {
//        List<MyPageCntsDto> list = myPageDao.selectCntsView("testid2");
//        for (int i = 0; i < list.size(); i++) {
//            String test = list.get(i).getCnts_postr_img();
//            System.out.println("test = " + test);
//        }
//        assertTrue(list.size() == 1);
//    }
//
//    @Test
//    public void selectPost() throws Exception {
//        List<MyPagePostDto> list = myPageDao.selectPost("eunbi");
//        for (int i = 0; i < list.size(); i++) {
//            String test = list.get(i).getPost_title();
//            System.out.println("test = " + test);
//        }
//        System.out.println("list = " + list);
//        assertTrue(list.size() == 17);
//    }
//
//    @Test
//    public void selectMyInfo() throws Exception {
//        MyPageDto myPageDto = myPageDao.selectMyInfo("eunbi");
//        int birthYear = Integer.parseInt(myPageDto.getBtdt().substring(0, 4));
//        System.out.println("birthYear = " + birthYear);
//
//        int curYear = LocalDate.now().getYear();
//        System.out.println("curYear = " + curYear);
//
//        int age = (int) (curYear - birthYear + 1) / 10 * 10;
//        ;
//        assertTrue(age == 20);
//
//        assertTrue(myPageDto.getSex() == 0);
//    }
//
//}