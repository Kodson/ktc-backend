package com.kodsonApp.service;

import com.kodsonApp.domain.PriceUpdate;
import com.kodsonApp.repository.PriceUpdateRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PriceUpdateService {

    private final PriceUpdateRepo repo;
    private final TankService tankService;

    public PriceUpdateService(PriceUpdateRepo repo, TankService tankService) {
        this.repo = repo;
        this.tankService = tankService;
    }

    public PriceUpdate savePriceUpdate(PriceUpdate update) {
        return repo.save(update);
    }

    public List<PriceUpdate> getAllUpdates() {
        return repo.findAll();
    }

    public PriceUpdate getUpdateById(UUID id) {
        return repo.findById(id.toString()).orElse(null);
    }

    // 🔹 Fetch pending updates
    public List<PriceUpdate> getPendingUpdates() {
        return repo.findAll().stream()
                .filter(update -> "Pending".equalsIgnoreCase(update.getStatus()))
                .toList();
    }

    // 🔹 Approve single update

    public PriceUpdate approveUpdate(UUID id, String approvedBy) {
        PriceUpdate update = getUpdateById(id);
        if (update != null) {
            update.setStatus("approved");
            update.setApprovedBy(approvedBy);
            PriceUpdate savedUpdate = repo.save(update);

            // Update tank prices for affected tanks
            if (update.getAffectedTankIds() != null && !update.getAffectedTankIds().isEmpty()) {
                tankService.updateTankPrices(update.getId(), update.getAffectedTankIds(), update.getNewPrice());
            }

            return savedUpdate;
        }
        return null;
    }

    // 🔹 Reject single update
    public PriceUpdate rejectUpdate(UUID id, String comment, String approvedBy) {
        PriceUpdate update = getUpdateById(id);
        if (update != null) {
            update.setStatus("rejected");
            update.setApprovalComment(comment);
            update.setApprovedBy(approvedBy);
            return repo.save(update);
        }
        return null;
    }

    // 🔹 Bulk approve updates
    public List<PriceUpdate> bulkApprove(List<UUID> ids, String approvedBy) {
        List<PriceUpdate> updates = repo.findAllById(ids.stream().map(UUID::toString).toList());
        updates.forEach(update -> {
            update.setStatus("approved");
            update.setApprovedBy(approvedBy);

            // Update tank prices for affected tanks
            if (update.getAffectedTankIds() != null && !update.getAffectedTankIds().isEmpty()) {
                tankService.updateTankPrices(update.getId(), update.getAffectedTankIds(), update.getNewPrice());
            }
        });
        return repo.saveAll(updates);
    }

    // 🔹 Statistics
    public Map<String, Object> fetchStatistics() {
        List<PriceUpdate> all = repo.findAll();
        LocalDate today = LocalDate.now();

        long pendingCount = all.stream().filter(u -> "pending".equalsIgnoreCase(u.getStatus())).count();
        long approvedToday = all.stream()
                .filter(u -> "approved".equalsIgnoreCase(u.getStatus()) && u.getCreatedAt().toLocalDate().equals(today))
                .count();
        long rejectedToday = all.stream()
                .filter(u -> "rejected".equalsIgnoreCase(u.getStatus()) && u.getCreatedAt().toLocalDate().equals(today))
                .count();
        int totalAffectedTanks = all.stream()
                .filter(u -> "pending".equalsIgnoreCase(u.getStatus()))
                .mapToInt(PriceUpdate::getTotalAffectedTanks)
                .sum();
        double averagePriceChange = all.stream()
                .filter(u -> "pending".equalsIgnoreCase(u.getStatus()))
                .mapToDouble(PriceUpdate::getPercentageChange)
                .average()
                .orElse(0.0);

        double totalValueImpact = 0.0; // Placeholder (would calculate in real backend)

        Map<String, Object> stats = new HashMap<>();
        stats.put("pendingCount", pendingCount);
        stats.put("approvedToday", approvedToday);
        stats.put("rejectedToday", rejectedToday);
        stats.put("totalAffectedTanks", totalAffectedTanks);
        stats.put("averagePriceChange", averagePriceChange);
        stats.put("totalValueImpact", totalValueImpact);

        return stats;
    }
}
