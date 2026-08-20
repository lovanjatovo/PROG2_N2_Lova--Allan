package controllers;

import model.Donation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.DonationService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations()
            throws SQLException {

        return ResponseEntity.ok(
                donationService.getAllDonations()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(
            @PathVariable Long id
    ) throws SQLException {

        Donation donation =
                donationService.getDonationById(id);

        if (donation == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(donation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Donation>> getDonationsByUserId(
            @PathVariable Long userId
    ) throws SQLException {

        return ResponseEntity.ok(
                donationService.getDonationsByUserId(userId)
        );
    }

    @PostMapping
    public ResponseEntity<Donation> createDonation(
            @RequestParam Long userId,
            @RequestBody Donation donation
    ) throws SQLException {

        return ResponseEntity.ok(
                donationService.createDonation(
                        userId,
                        donation
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDonation(
            @PathVariable Long id,
            @RequestBody Donation donation
    ) throws SQLException {

        donation.setId(id);
        donationService.updateDonation(donation);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(
            @PathVariable Long id
    ) throws SQLException {

        donationService.deleteDonation(id);

        return ResponseEntity.noContent().build();
    }
}