import com.xeno.xenoAssignment.Model.Campaigns;
import com.xeno.xenoAssignment.Model.Customer;
import com.xeno.xenoAssignment.Service.CampaignServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class XenoController {

    @Autowired
    private CampaignServiceImpl campaignService;

    // Endpoint to add customers
    @PostMapping("/customers/add")
    public ResponseEntity<?> addCustomers(@RequestBody List<Customer> customers) {
        return campaignService.addCustomer(customers);
    }

    // Endpoint to create and apply campaigns
    @PostMapping("/campaigns/create")
    public ResponseEntity<?> createCampaign(@RequestBody List<Integer> campaignIds) {
        try {
            int count = campaignService.createCampaign(campaignIds);
            return ResponseEntity.ok("Number of qualified customers: " + count);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
    }

    // Endpoint to get all customers
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = campaignService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Endpoint to get all campaigns
    @GetMapping("/campaigns")
    public ResponseEntity<List<Campaigns>> getAllCampaigns() {
        List<Campaigns> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    // Endpoint to get all customers who fall under selected campaigns
    @PostMapping("/campaigns/customers")
    public ResponseEntity<?> getCustomersByCampaign(@RequestBody List<Integer> campaignIds) {
        try {
            Map<Integer, List<Customer>> customersByCampaign = campaignService.getCustomersByCampaign(campaignIds);
            return ResponseEntity.ok(customersByCampaign);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
    }
}
