package com.xeno.xenoAssignment.Service;

import com.xeno.xenoAssignment.Model.Campaigns;
import com.xeno.xenoAssignment.Model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CampaignServiceImpl implements CampaignService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    HashMap<Integer, Customer> customers = new HashMap<>();
    HashMap<Integer, Campaigns> listOfAllCampaigns = new HashMap<>();

    public ResponseEntity<HttpStatus> addCustomer(List<Customer> customerList) {
        HttpStatus httpStatus = HttpStatus.OK;
        if (customerList == null || customerList.isEmpty()) {
            System.out.println("Customers list is empty!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            for (Customer cust : customerList) {
                customers.putIfAbsent(cust.getId(), cust);
            }
        } catch (Exception e) {
            System.out.println("Error Adding Customers: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(httpStatus);
    }

    public int createCampaign(List<Integer> campaignIds) throws ParseException {
        addCampaigns(); // Ensure campaigns are added
        Set<Integer> customerIds = new HashSet<>();

        for (Integer id : campaignIds) {
            Campaigns campaign = listOfAllCampaigns.get(id);
            if (campaign != null) {
                applyCampaign(campaign, customerIds);
            }
        }

        return customerIds.size();
    }

    // Added public getter for all campaigns
    public HashMap<Integer, Campaigns> getListOfAllCampaigns() {
        return new HashMap<>(listOfAllCampaigns);
    }

    // Method to add a single campaign (can be called externally)
    public void addCampaign(Campaigns campaign) {
        listOfAllCampaigns.putIfAbsent(campaign.getId(), campaign);
    }

    private void applyCampaign(Campaigns campaign, Set<Integer> customerIds) {
        Date threeMonthsAgo = new Date(System.currentTimeMillis() - (90L * 24 * 3600 * 1000));
        for (Customer customer : customers.values()) {
            switch (campaign.getId()) {
                case 1: // Campaign 1: Customers who have spent more than 10,000 in the last 3 months
                    if (customer.getAmount() > 10000 && customer.getLastBoughtDate().after(threeMonthsAgo)) {
                        customerIds.add(customer.getId());
                    }
                    break;
                case 2: // Campaign 2: Customers who have not spent money in the last 3 months
                    if (customer.getLastBoughtDate().before(threeMonthsAgo)) {
                        customerIds.add(customer.getId());
                    }
                    break;
            }
        }
    }

    private void addCampaigns() throws ParseException {
        // Ensuring unique IDs for campaigns
        Campaigns camp1 = Campaigns.builder()
                .name("spend more than 10k")
                .validityInTermsOfAmountSpent(10000)
                .campaignLaunchDate(sdf.parse("2023-12-31"))
                .validityInTermsOfDuration(sdf.parse("2023-12-31"))
                .id(1)
                .build();

        Campaigns camp2 = Campaigns.builder()
                .name("not visited from last 3 months")
                .validityInTermsOfAmountSpent(0)
                .campaignLaunchDate(sdf.parse("2023-12-31"))
                .validityInTermsOfDuration(sdf.parse("2023-12-31"))
                .id(2)
                .build();

        listOfAllCampaigns.putIfAbsent(camp1.getId(), camp1);
        listOfAllCampaigns.putIfAbsent(camp2.getId(), camp2);
    }

    public List<Customer> getAllCustomers() {
        return customers.values().parallelStream().toList();
    }

    public List<Campaigns> getAllCampaigns() {
        return new ArrayList<>(listOfAllCampaigns.values());
    }

    public Map<Integer, List<Customer>> getCustomersByCampaign(List<Integer> campaignIds) throws ParseException {
        Map<Integer, List<Customer>> result = new HashMap<>();
        for (Integer id : campaignIds) {
            Campaigns campaign = listOfAllCampaigns.get(id);
            if (campaign != null) {
                Set<Integer> qualifiedCustomerIds = new HashSet<>();
                applyCampaign(campaign, qualifiedCustomerIds);
                List<Customer> qualifiedCustomers = qualifiedCustomerIds.stream()
                        .map(customerId -> customers.get(customerId))
                        .collect(Collectors.toList());
                result.put(id, qualifiedCustomers);
            }
        }
        return result;
    }
}
