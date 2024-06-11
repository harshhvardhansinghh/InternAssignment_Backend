package com.xeno.xenoAssignment.Model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Customer {
    private int id;
    private String name;
    private double amount;
    private Date lastBoughtDate;
}
