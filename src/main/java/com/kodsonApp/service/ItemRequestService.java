package com.kodsonApp.service;

import com.kodsonApp.domain.ItemRequest;
import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.repository.ItemRequestRepo;
import com.kodsonApp.utility.ItemSocketHandler;
import com.kodsonApp.utility.PettyCashSms;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepo itemRequestRepo;
    private final PettyCashSms pettyCashSms;
    @Autowired
    private ItemSocketHandler itemSocketHandler;

    // Save a new ItemRequest
    public ItemRequest save(ItemRequest itemRequest) throws IOException {
        //log.info("Saving ItemRequest: {}", itemRequest);
        itemRequestRepo.save(itemRequest);
        pettyCashSms.sendGmItem(itemRequest.getItem());
        itemSocketHandler.broadcastMessage("New petty item request submitted.");
        return itemRequest;
    }

    // Get all ItemRequests
    public List<ItemRequest> findAll() {
        //log.info("Fetching all ItemRequests");
        return itemRequestRepo.findAll();
    }

    // Find an ItemRequest by ID
    public Optional<ItemRequest> findById(String id) {
        //log.info("Fetching ItemRequest by ID: {}", id);
        return itemRequestRepo.findById(id);
    }

    // Update an existing ItemRequest
    public Optional<ItemRequest> update(String id, ItemRequest updatedItemRequest) {
        //log.info("Updating ItemRequest with ID: {}", id);
        return itemRequestRepo.findById(id).map(itemRequest -> {
            itemRequest.setDate(updatedItemRequest.getDate());
            itemRequest.setTruck(updatedItemRequest.getTruck());
            itemRequest.setItem(updatedItemRequest.getItem());
            itemRequest.setDescription(updatedItemRequest.getDescription());
            itemRequest.setQty(updatedItemRequest.getQty());
            itemRequest.setPrice(updatedItemRequest.getPrice());
            itemRequest.setTotal(updatedItemRequest.getTotal());
            itemRequest.setCompany(updatedItemRequest.getCompany());
            itemRequest.setReciever(updatedItemRequest.getReciever());
            itemRequest.setUserName(updatedItemRequest.getUserName());
            return itemRequestRepo.save(itemRequest);
        });
    }

    // Delete an ItemRequest
    public boolean delete(String id) {
        log.info("Deleting ItemRequest with ID: {}", id);
        return itemRequestRepo.findById(id).map(itemRequest -> {
            itemRequestRepo.delete(itemRequest);
            return true;
        }).orElse(false);
    }

    // Find ItemRequests by status and date range
    public List<ItemRequest> findByStatusAndDateBetween(String status, LocalDate startDate, LocalDate endDate) {
        //log.info("Fetching ItemRequests with status: {} between {} and {}", status, startDate, endDate);
        return itemRequestRepo.findByStatusAndDateBetween(status, startDate, endDate);
    }


    // New: Find ItemRequests by status
    public List<ItemRequest> findByStatus(String status) {
        //log.info("Fetching ItemRequests with status: {}", status);
        return itemRequestRepo.findByStatus(status);
    }

    public ItemRequest updateStatus(String id, ItemRequest pettyCash) throws IOException {
        ItemRequest existingPettyCash = getItems(id);
        existingPettyCash.setStatus(pettyCash.getStatus());
        pettyCashSms.sendManagerItem(existingPettyCash.getItem(),existingPettyCash.getUserPhone());
        return itemRequestRepo.save(existingPettyCash);
    }

    public ItemRequest getItems(String id) {
        return itemRequestRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }
}
