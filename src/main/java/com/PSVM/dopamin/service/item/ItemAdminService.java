package com.PSVM.dopamin.service.item;

import com.PSVM.dopamin.domain.item.ItemDto;
import com.PSVM.dopamin.dao.item.ItemAdminDaoImpl;
import com.PSVM.dopamin.domain.item.ItemForm;
import com.PSVM.dopamin.domain.item.Pymt_DetlDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ItemAdminService {
    private final ItemAdminDaoImpl itemAdminDaoImpl;
    public ItemAdminService(ItemAdminDaoImpl itemAdminDaoImpl) {
        this.itemAdminDaoImpl = itemAdminDaoImpl;
    }
    public int getCount() throws Exception{
        return itemAdminDaoImpl.getCount();
    }

    public List<ItemDto> getPage(String order) throws Exception{
        int index;
        if(order.equals("스킨")){
            index=1;
        }
        else{
            index=2;
        }
        return itemAdminDaoImpl.getPage(index);
    }
    public List<ItemDto> getStat_0() throws Exception {
        return itemAdminDaoImpl.getStat_0();
    }
    public List<ItemDto> getStat_1() throws Exception {
        return itemAdminDaoImpl.getStat_1();
    }
    public int registerItem(ItemForm itemForm, String s3Url, Map map) throws Exception{
        //Controller에서 이미 검증된 정보들이 service로 넘어왔다.
        //여기서는 itemForm들의 정보를 item table처럼 가공해야하고,
        //file의 저장 경로도 설정해줘야 한다.
        //전제: 이미지 무조건 있다 -> Controller에서 검사하고 들어왔으니
        //String save_url=save_File(multipartFile);//파일 저장 //이미지 경로 반환
        ItemDto itemDto=save_into_ItemDto(itemForm);
        itemDto.setItem_img(s3Url);
        itemDto.setIn_user((String)map.get("user_id"));
        itemDto.setUp_user((String)map.get("user_id"));
        return itemAdminDaoImpl.registerItem(itemDto);
    }
    public int remove(Integer item_id) throws Exception{
        return itemAdminDaoImpl.remove(item_id);
    }

    public int modify(ItemForm itemForm) throws Exception{
        ItemDto itemDto=save_into_ItemDto(itemForm);
        itemDto.setItem_id(itemForm.getItem_id());
        return itemAdminDaoImpl.modify(itemDto);
    }
    private ItemDto save_into_ItemDto(ItemForm itemForm) {
        ItemDto itemDto = new ItemDto();
        if(itemForm.getList_nm()!=null){
            if(itemForm.getList_nm().equals("스킨")){
                itemDto.setList_id(1);
            }
            else{
                itemDto.setList_id(2);
            }
        }
        itemDto.setGrd_nm(itemForm.getItem_grd());
        itemDto.setItem_nm(itemForm.getItem_nm());
        itemDto.setItem_dsc(itemForm.getItem_dsc());
        itemDto.setItem_price(Integer.parseInt(itemForm.getItem_price()));
        return itemDto;
    }
    private String save_File(MultipartFile multipartFile) {
        //1. file 경로 설정.
        String uploadFolder="C:\\Users\\LDH\\Desktop\\Java_jungsuck";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str=sdf.format(date);//2022-08-19
        String datePath=str.replace("-", File.separator);
        File uploadPath=new File(uploadFolder,datePath);
        if(uploadPath.exists()==false){
            uploadPath.mkdirs();
        }//여기까지 저장할 경로 생성
        String uploadFileName= multipartFile.getOriginalFilename();
        String uuid= UUID.randomUUID().toString();
        uploadFileName=uuid+'_'+uploadFileName;

        File saveFile=new File(uploadPath,uploadFileName);

        try {
            multipartFile.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
        //파일 저장
        String save_url=uploadFolder+'\\'+datePath+'\\'+uploadFileName;
        return save_url;
    }
    public String getUser_nic(String user_id) throws Exception {
        return itemAdminDaoImpl.getUser_nic(user_id);
    }

    public int noShow(Integer item_id) throws Exception{
        return itemAdminDaoImpl.ShowToNoShow(item_id);
    }

    public int show(Integer item_id) throws Exception{
        return itemAdminDaoImpl.NoShowToShow(item_id);
    }

    public List<ItemDto> getPage_map(Map map) {
        int index;
        if(map.get("order").equals("스킨")){
            index=1;
        }
        else{
            index=2;
        }
        map.put("index",index);
        return itemAdminDaoImpl.getPage_map(map);
    }

    public List<ItemDto> get_pop(Integer num) throws Exception {
        return itemAdminDaoImpl.get_pop(num);
    }
    @Transactional
    //결제 실패 시 되돌려야 함.
    public boolean pymt_detl(Pymt_DetlDto pymt_detlDto) throws Exception{
        //현금으로 포인트 충전 시, 유저 포인트 올려주고, 결제내역 테이블에 내역 쌓아야 함.
        int result_PY=itemAdminDaoImpl.insert_pymt_detl(pymt_detlDto);
        if(result_PY!=1){
            throw new Exception("잠시 후 다시 시도해주세요.");
        }
        //유저포인트 가져와서
        int user_point=itemAdminDaoImpl.get_user_point(pymt_detlDto.getUser_id());
        //충전한 포인트를 기존포인트에 더해서 올려줌.
        pymt_detlDto.setChg_pnt(user_point+ pymt_detlDto.getChg_pnt());
        int result_UP=itemAdminDaoImpl.increase_user_point(pymt_detlDto);
        if(result_UP!=1){
            throw new Exception("잠시 후 다시 시도해주세요.");
        }

        return true;
    }
}
