package com.xeno.xenoAssignment.Resource;

import com.xeno.xenoAssignment.Model.Campaigns;
import com.xeno.xenoAssignment.Service.CampaignServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class XenoResource {

    private CampaignServiceImpl campaignService;

    public XenoResource(CampaignServiceImpl campaignService) {
        this.campaignService = campaignService;
    }

    // Method to fetch all campaigns
    public List<Campaigns> getAllCampaigns() {
        return new ArrayList<>(campaignService.getListOfAllCampaigns().values());
    }

    // Method to fetch a campaign by ID
    public Campaigns getCampaignById(int campaignId) {
        return campaignService.getListOfAllCampaigns().get(campaignId);
    }

    // Method to add a new campaign
    public String addCampaign(Campaigns campaign) {
        try {
            campaignService.addCampaign(campaign);
            return "Campaign added successfully!";
        } catch (Exception e) {
            return "Error adding campaign: " + e.getMessage();
        }
    }
}


