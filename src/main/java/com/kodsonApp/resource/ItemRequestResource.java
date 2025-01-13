package com.kodsonApp.resource;

import com.kodsonApp.domain.ItemRequest;
import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.service.ItemRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = { "/", "api/items" })
@RequiredArgsConstructor
public class ItemRequestResource {
    private final ItemRequestService itemRequestService;

    // Create a new ItemRequest
    @PostMapping
    public ResponseEntity<ItemRequest> createItemRequest(@RequestBody ItemRequest itemRequest) throws IOException {
        ItemRequest createdItemRequest = itemRequestService.save(itemRequest);
        return new ResponseEntity<>(createdItemRequest, HttpStatus.CREATED);
    }

    // Get all ItemRequests
    @GetMapping
    public ResponseEntity<List<ItemRequest>> getAllItemRequests() {
        List<ItemRequest> itemRequests = itemRequestService.findAll();
        return new ResponseEntity<>(itemRequests, HttpStatus.OK);
    }

    // Get an ItemRequest by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemRequest> getItemRequestById(@PathVariable String id) {
        return itemRequestService.findById(id)
                .map(itemRequest -> new ResponseEntity<>(itemRequest, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update an existing ItemRequest
    @PutMapping("/{id}")
    public ResponseEntity<ItemRequest> updateItemRequest(@PathVariable String id, @RequestBody ItemRequest updatedItemRequest) {
        return itemRequestService.update(id, updatedItemRequest)
                .map(itemRequest -> new ResponseEntity<>(itemRequest, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete an ItemRequest by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemRequest(@PathVariable String id) {
        if (itemRequestService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Find ItemRequests by status and date range
   /* @GetMapping("/status")
    public ResponseEntity<List<ItemRequest>> findByStatusAndDateBetween(
            @RequestParam String status,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ItemRequest> itemRequests = itemRequestService.findByStatusAndDateBetween(status, startDate, endDate);
        return new ResponseEntity<>(itemRequests, HttpStatus.OK);
    }*/

    // Find ItemRequests by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ItemRequest>> findByStatus(@PathVariable String status) {
        List<ItemRequest> itemRequests = itemRequestService.findByStatus(status);
        return ResponseEntity.ok(itemRequests);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ItemRequest>> getPendingItems() {
        List<ItemRequest> pendingItems = itemRequestService.findByStatus("pending");
        return new ResponseEntity<>(pendingItems, HttpStatus.OK);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ItemRequest>> getApprovedItems() {
        List<ItemRequest> pendingItems = itemRequestService.findByStatus("approved");
        return new ResponseEntity<>(pendingItems, HttpStatus.OK);
    }

    @GetMapping("/suspend")
    public ResponseEntity<List<ItemRequest>> getSuspendedItems() {
        List<ItemRequest> pendingItems = itemRequestService.findByStatus("suspend");
        return new ResponseEntity<>(pendingItems, HttpStatus.OK);
    }

    @GetMapping("/declined")
    public ResponseEntity<List<ItemRequest>> getDeclinedItems() {
        List<ItemRequest> pendingItems = itemRequestService.findByStatus("declined");
        return new ResponseEntity<>(pendingItems, HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<ItemRequest> updateStatus(@PathVariable(value = "id") String id, @RequestBody ItemRequest pettyCash) throws IOException {
        return ResponseEntity.ok().body(itemRequestService.updateStatus(id, pettyCash));
    }

    @GetMapping("/approvedBetween")
    public ResponseEntity<Page<ItemRequest>> getApprovedPettyBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(itemRequestService.getApprovedPettyCashBetweenDates(
                startDate, endDate, page, size, sortDirection));
    }

}
