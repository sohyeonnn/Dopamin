package com.PSVM.dopamin.controller.item;

import com.PSVM.dopamin.domain.item.Cart_ItemDto;
import com.PSVM.dopamin.domain.item.ItemDto;
import com.PSVM.dopamin.domain.item.OrderDto;
import com.PSVM.dopamin.error.Message;
import com.PSVM.dopamin.service.item.ItemUserService;
import com.PSVM.dopamin.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/item")
public class ItemUserController {
    //유저의 기능들-> 아이템 담기 + 아이템 조회.

    private ItemUserService itemUserService;
    public ItemUserController(ItemUserService itemUserService) {
        this.itemUserService = itemUserService;
    }
    @Autowired
    UserService userService;
    //환불 버튼 클릭시
    //프런트에서 받아올 데이터는, item_id, item_price 이렇게만 받아오면 될듯.
    @PostMapping("/exchangeItem")
    @ResponseBody
    public String exchange_Item(@RequestBody OrderDto orderDto, HttpSession session, HttpServletRequest request){
        //로그인 체크해야함.지금은 하드코딩
        if(!userService.loginCheck(request)){
            return "redirect:/login/login";
        }
        String user_id= (String) session.getAttribute("USERID");
        try{
            orderDto.setUser_id(user_id);
            int item_stat=itemUserService.getStat_from_possesion(orderDto);
            if(item_stat!=-1){
                throw new Exception("사용하신 아이템은 환불이 불가능합니다.");
            }
            boolean result=itemUserService.exchange_item(orderDto);
            if (!result) {
                throw new Exception("환불에 실패했습니다.");
            }
        }catch (Exception e){
            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();
        }

        return null;
    }
    @GetMapping("/chargePoint")
    public String charge_point(HttpServletRequest request){
        if(!userService.loginCheck(request)){
            return "redirect:/login/login";
        }
        return "Item/new_point_charge";
    }
    //구매 버튼 클릭 시
    //일단 포인트가 충분한지 확인
    //만일 포인트가 부족하다면, 보유 포인트가 부족합니다. 포인트 충전하러 가시겠습니까? 예/아니오
    //    포인트가 충분하다면, 구매하시겠습니까? 예/아니오
    //                     구매가 되었다면? 1. 포인트 차감과 함께 구매한 아이템들은 장바구니에서 사라지는 동시에.
    //                                   2. 아이템 보유 목록과 거래내역에, 포인트 사용내역 추가되어야 함.
    @PostMapping("/buyCart")//장바구니에서 아이템 구매 시, 발생하는 사건들.
    @ResponseBody
    public ResponseEntity<Object> buy_item_in_Cart(@RequestBody List<OrderDto> orderDtoList, HttpSession session){
        try{

            int cart_id = Integer.parseInt((String)session.getAttribute("CARTID"));
            String user_id = (String)session.getAttribute("USERID");
            if(orderDtoList.size()==0 || orderDtoList==null){
                throw new Exception("구매할 아이템을 선택해주세요.");
            }
            itemUserService.buy_item(orderDtoList,user_id,cart_id);
            //뭔가 컨트롤러에서 다 처리하기에는 부담되는데
            //데이터들 싹다 모아 서비스단에서 처리하고
            return new ResponseEntity<>("BUY_OK", HttpStatus.OK);
        }catch(Exception e){
            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/deleteCart/{item_id}")//장바구니에서 유저가 아이템 삭제
    @ResponseBody
    public ResponseEntity<Object> delete_Cart(@PathVariable Integer item_id, HttpSession session){
        //cart_item은 (item_id,cart_id)가 PK
        //cart_id는 session으로부터 얻어올 수 있음
        //item_id는 장바구니에서 삭제 버튼 누를 때 넘어오게 하자.
        int cart_id = Integer.parseInt((String)session.getAttribute("CARTID"));

        try {
            Map map=new HashMap<>();
            map.put("item_id",item_id);
            map.put("cart_id",cart_id);
            int delete_result=itemUserService.delete_cart(map);
            if(delete_result!=1){
                throw new Exception("삭제에 실패했습니다.");
            }
            return new ResponseEntity<Object>("장바구니에서 제거되었습니다.",HttpStatus.OK);
        } catch (Exception e) {
            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();
            return new ResponseEntity<Object>(message,HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/addCart/{item_id}")//장바구니에 아이템 담기
    @ResponseBody
    public ResponseEntity<Object> add_Cart(@PathVariable Integer item_id,HttpSession session){
        //세션에서 cart_id 받는다. 지금은 세션이 없으니깐, 임시로 cart_id 하드코딩
        Cart_ItemDto cart_itemDto=new Cart_ItemDto();
        try {
            int cart_id = Integer.parseInt((String)session.getAttribute("CARTID"));
            String user_id = (String)session.getAttribute("USERID");
            cart_itemDto.setCart_id(cart_id);
            cart_itemDto.setIn_user(user_id);
            cart_itemDto.setUp_user(user_id);
            //없는 아이템이거나 비i공개 아이템인 경우,
            itemUserService.find_item(item_id);
            cart_itemDto.setItem_id(item_id);
            //개인당 하나의 아이템밖에 구매하지 못함.//todo
            //구매한 아이템의 경우 예외 발생.
            //따라서, 장바구니에 이미 있는 경우 예외 발생해야함.
            itemUserService.addCart(cart_itemDto);
            return new ResponseEntity<>("장바구니에 추가되었습니다.", HttpStatus.OK);
        }catch(SQLException e){
            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();
            return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
        }
        catch (NullPointerException e) {
            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {

            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/cart_main")//장바구니 조회
    public ResponseEntity<Object> cart_list(Model m,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        try {//지금은 로그인이 항상 되어있다는 가정하에 진행. 추후, 수정해야함.
            if(session == null){
                throw new Exception("로그인이 필요합니다.");
            }
            int cart_id = Integer.parseInt((String)session.getAttribute("CARTID"));
            String user_id= (String) session.getAttribute("USERID");

            int my_point= itemUserService.getUser_point(user_id);
            int total_point=0;
            List<ItemDto> list=itemUserService.getCart_list(cart_id);
            if(list.size()==0){
                throw new Exception("장바구니에 담긴 아이템이 없습니다.");
            }
            for(ItemDto itemDto:list){
                total_point+=itemDto.getItem_price();
            }
            Map map = new HashMap();
            map.put("list",list);
            map.put("my_point",my_point);
            map.put("total_point",total_point);
            map.put("after_point",my_point-total_point);
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (Exception e) {
            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        //없으면 없는대로 보여주면 됨.
    }
    @GetMapping("/main")//todo
    @ResponseBody
    public ResponseEntity<Object> main(){
        List<ItemDto> list=null;
        try {
            list=itemUserService.getItem_list();
            if(list.size()==0){
                throw new Exception("시스템 점검으로 인해 잠시후 다시 시도해주시기 바랍니다.");
            }
            //      m.addAttribute("list",list);
            //return "Item/cart_main2";
            return new ResponseEntity<>(list,HttpStatus.OK);
        } catch (Exception e) {
            Message message = Message.builder()
                    .message1(e.getMessage())
                    .build();

//            return "Item/cart_main2";
            return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
        }
    }
}
