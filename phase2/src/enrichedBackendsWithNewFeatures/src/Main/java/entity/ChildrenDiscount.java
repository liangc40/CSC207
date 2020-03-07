package entity;


import lombok.Data;

import java.math.BigDecimal;

@Data
/** A ChildrenDiscount which is the discount on children. */
public class ChildrenDiscount {
    /** Id in database. */
    private int childrenDiscountID;
    /** The discount rate of this ChildrenDiscount. */
    private BigDecimal discountRate;
    /** The expiration date of this ChildrenDiscount. */
    private String expirationDate;
    private int UserID;
}