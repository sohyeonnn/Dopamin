package com.PSVM.dopamin.dao;

import com.PSVM.dopamin.dao.item.ItemAdminDaoImpl;
import com.PSVM.dopamin.domain.item.ItemDto;
import com.PSVM.dopamin.domain.item.Pymt_DetlDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class ItemAdminDaoImplTest {
    @Autowired
    ItemAdminDaoImpl itemAdminDaoImpl;

    @Test
    public void getCount() throws Exception {
        int rowcnt= itemAdminDaoImpl.getCount();
        System.out.println(rowcnt);
    }
    @Test
    public void getPage() throws Exception{
        int index=1;
        List<ItemDto> list=itemAdminDaoImpl.getPage(1);
        System.out.println("list = " + list);
        System.out.println("list.size() = " + list.size());
    }
    @Test
    public void get_pop() throws Exception{
        int num=8;
        List<ItemDto> list=itemAdminDaoImpl.get_pop(num);
        assertTrue(list.size()==num);
    }
    @Test
    public void getPage_map() throws Exception{
        Map map=new HashMap<>();
        int page=1;
        int pageSize=8;
        int totalCnt=15;
        map.put("offset",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("index",1);
        List<ItemDto> lsit=itemAdminDaoImpl.getPage_map(map);
        System.out.println("lsit = " + lsit);
    }
    @Test
    public void getUser_stat_0() throws Exception{
        int user_stat= itemAdminDaoImpl.getUser_stat("ldhoon0813");
        assertTrue(user_stat==0);
    }
    @Test
    public void getUser_stat_X() throws Exception{
        int user_stat= itemAdminDaoImpl.getUser_stat("aaaa");
        assertTrue(user_stat!=0);
    }
    @Test
    public void getUser_nic() throws Exception{
        String user_nic= itemAdminDaoImpl.getUser_nic("ldhoon0813");
        assertTrue(user_nic.equals("후후른훈"));
    }
    @Test
    public void 신규아이템_등록() throws Exception{
        //신규 아이템을 등록하는데, 테스트에서는 독립적으로 작동해야하므로
        //테스트용으로 집어넣고자하는 아이템을 찾아서 있으면 지우고
        //없으면 바로 집어넣자 ㅎ
        String item_name="테두리뿌시기";
        String result= itemAdminDaoImpl.test_select(item_name);
        if(result!=null) {
            itemAdminDaoImpl.test_delete_item(item_name);
        }//테스트의 독립성을 위해 넣고자하는 데이터가 있으면 지우고 Insert를 한다.
        ItemDto itemDto=new ItemDto(2,"전설",item_name,"야 빡치면 뿌시지말고 스킨으로 뿌셔봐 뿌셔뿌셔",3000, "url",new Timestamp(System.currentTimeMillis()),"ldhoon0813","ldhoon0813");
        int result1= itemAdminDaoImpl.registerItem(itemDto);
        assertTrue(result1==1);
    }

    @Test
    @Transactional
    public void 삭제_성공() throws Exception{
        int result= itemAdminDaoImpl.remove(3);
        assertTrue(result==1);
    }
    @Test
    @Transactional
    public void 삭제_실패() throws Exception{
        int result= itemAdminDaoImpl.remove(0);
        assertTrue(result==0);
    }
    @Test
    @Transactional
    public void 수정_성공() throws Exception{
        ItemDto itemDto = new ItemDto(2,"우영우","박은빈팬이동훈",3000);
        int result= itemAdminDaoImpl.modify(itemDto);
        assertTrue(result==1);
    }
    //1이 공개 0이 비공개
    //공개를 비공개로 테스트시 유의해야할 점.
    //성공과 실패를 어떻게 나누어 기준으로 둘것인가?
    //예를 들어, 공개를 하는 것에 또 공개를 한다? -> 실패
    //없는 아이디를 비공개를 한다?-> 실패

    @Test
    @Transactional
    public void 공개를비공개로_성공()throws Exception{
        //item_id의 상태 확인. 만약에 상태가 0이라면 이미 비공개이니깐->실패
        //0이 아니라면 수행하고 결과값 보고 결정.
        int flag=0;
        //given
        List<ItemDto> list= itemAdminDaoImpl.getStat_1();
        for(int i=0;i<list.size();i++){
            int result= itemAdminDaoImpl.ShowToNoShow(list.get(i).getItem_id());
            if(result!=1){
                flag=1;
                break;
            }
        }
        assertTrue(flag==0);
    }
    @Test
    @Transactional
    public void 비공개를공개로_성공() throws Exception{
        int flag=0;
        //given
        List<ItemDto> list= itemAdminDaoImpl.getStat_0();
        for(int i=0;i<list.size();i++) {
            int result = itemAdminDaoImpl.ShowToNoShow(list.get(i).getItem_id());
            if (result != 1) {
                flag = 1;
                break;
            }
            assertTrue(flag == 0);
        }
    }
    @Test
    public void 유저_포인트_가져오기() throws Exception{
        Pymt_DetlDto pymt_detlDto = new Pymt_DetlDto();
        pymt_detlDto.setPymt_amt(5000);
        pymt_detlDto.setChg_pnt(8000);
        pymt_detlDto.setUser_id("ldhoon0813");
        int result=itemAdminDaoImpl.get_user_point(pymt_detlDto.getUser_id());
        System.out.println("result = " + result);
    }
    @Test
    @Transactional
    public void 유저_포인트_증가() throws Exception{
        Pymt_DetlDto pymt_detlDto = new Pymt_DetlDto();
        pymt_detlDto.setPymt_amt(5000);
        pymt_detlDto.setChg_pnt(8000);
        pymt_detlDto.setUser_id("ldhoon0813");
        int user_point=itemAdminDaoImpl.get_user_point(pymt_detlDto.getUser_id());
        pymt_detlDto.setChg_pnt(user_point+ pymt_detlDto.getChg_pnt());
        int result=itemAdminDaoImpl.increase_user_point(pymt_detlDto);
        System.out.println("result = " + result);
    }
    @Test
    @Transactional
    public void 포인트_사용_내역_추가() throws Exception{
        Pymt_DetlDto pymt_detlDto = new Pymt_DetlDto();
        pymt_detlDto.setPymt_amt(5000);
        pymt_detlDto.setChg_pnt(8000);
        pymt_detlDto.setUser_id("ldhoon0813");
        pymt_detlDto.setPg_corp_detl_id("2c7785a334e1fbab53a2");

        System.out.println("pymt_detlDto = " + pymt_detlDto);
        int result=itemAdminDaoImpl.insert_pymt_detl(pymt_detlDto);
        System.out.println("result = " + result);
    }
}