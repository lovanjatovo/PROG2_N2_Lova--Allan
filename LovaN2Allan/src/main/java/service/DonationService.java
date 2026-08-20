package service;

import dao.DonationDAO;
import model.Donation;

import java.sql.SQLException;
import java.util.List;

public class DonationService {

    private final DonationDAO donationDAO;

    public DonationService(DonationDAO donationDAO) {
        this.donationDAO = donationDAO;
    }

    // CREATE
    public Donation createDonation(
            Long userId,
            Donation donation
    ) throws SQLException {

        validateDonation(userId, donation);

        return donationDAO.save(userId, donation);
    }

    // READ
    public Donation getDonationById(Long id)
            throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Donation id cannot be null"
            );
        }

        return donationDAO.findById(id);
    }

    // READ ALL
    public List<Donation> getAllDonations()
            throws SQLException {

        return donationDAO.findAll();
    }

    // READ BY USER
    public List<Donation> getDonationsByUserId(Long userId)
            throws SQLException {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User id cannot be null"
            );
        }

        return donationDAO.findByUserId(userId);
    }

    // UPDATE
    public void updateDonation(Donation donation)
            throws SQLException {

        validateDonation(donation);

        if (donation.getId() == null) {
            throw new IllegalArgumentException(
                    "Donation id cannot be null"
            );
        }

        donationDAO.update(donation);
    }

    // DELETE
    public void deleteDonation(Long id)
            throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Donation id cannot be null"
            );
        }

        donationDAO.delete(id);
    }

    private void validateDonation(
            Long userId,
            Donation donation
    ) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User id cannot be null"
            );
        }

        validateDonation(donation);
    }

    private void validateDonation(Donation donation) {

        if (donation == null) {
            throw new IllegalArgumentException(
                    "Donation cannot be null"
            );
        }

        if (donation.getAmount() == null) {
            throw new IllegalArgumentException(
                    "Donation amount cannot be null"
            );
        }

        if (donation.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Donation amount must be positive"
            );
        }
    }
}