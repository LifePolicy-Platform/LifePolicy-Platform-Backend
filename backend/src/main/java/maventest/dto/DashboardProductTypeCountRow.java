package maventest.dto;

import lombok.Getter;
import lombok.Setter;

/** MyBatis 對應：商品類型銷售件數（本月核准） */
@Getter
@Setter
public class DashboardProductTypeCountRow {

    private String productType;
    private Long count;
}
