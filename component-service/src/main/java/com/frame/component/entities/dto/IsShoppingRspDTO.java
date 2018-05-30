package com.frame.component.entities.dto;

import com.frame.component.entities.IsShoppingRsp;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class IsShoppingRspDTO implements Mapper<IsShoppingRsp> {
    /**
     "isFree": "是否收费（0免费 1收费）",
     "price": "收费价格,宝石数",
     "isShopping": "是否需要购买（0 无需购买 1 购买）"
     */
    private Integer isFree;
    private Integer price;
    private Integer isShopping;

    @Override
    public IsShoppingRsp transform() {
        IsShoppingRsp object = new IsShoppingRsp();

        object.setIsFree(EntitiesUtil.assertNotNull(isFree));
        object.setPrice(EntitiesUtil.assertNotNull(price));
        object.setIsShopping(EntitiesUtil.assertNotNull(isShopping));

        return object;
    }
}
