package com.xeno.xenoAssignment.Model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Campaigns {
    private int id;
    private String name;
    private Date validityInTermsOfDuration;
    private int validityInTermsOfAmountSpent;
    private Date campaignLaunchDate;
}
