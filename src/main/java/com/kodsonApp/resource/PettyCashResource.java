package com.kodsonApp.resource;

import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.service.PettyCashService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = { "/","kodson/petty"})
@RequiredArgsConstructor
public class PettyCashResource {
    private final PettyCashService pettyCashService;

    @PostMapping
    public ResponseEntity<PettyCash> createPettyCash(@RequestBody PettyCash pettyCash) {
        return ResponseEntity.created(URI.create("/kodson/petty/pettyID")).body(pettyCashService.createPettyCash(pettyCash));
    }

    @GetMapping
    public ResponseEntity<Page<PettyCash>> getAllPetty(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(pettyCashService.getAllPettyCash(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PettyCash> getPetty(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(pettyCashService.getPettyCash(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PettyCash> updatePetty(@PathVariable(value = "id") String id, @RequestBody PettyCash pettyCash) {
        return ResponseEntity.ok().body(pettyCashService.updatePettyCash(id, pettyCash));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetty(@PathVariable(value = "id") String id) {
        pettyCashService.deletePettyCash(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/approved")
    public ResponseEntity<List<PettyCash>> getApprovedPetty() {
        return ResponseEntity.ok().body(pettyCashService.getApprovedPettyCash());
    }

    @GetMapping("/approvedWithUser")
    public ResponseEntity<List<PettyCash>> getApprovedPettyWithUser(@RequestParam String userName) {
        List<PettyCash> pettyCashList = pettyCashService.getApprovedPettyCashWithUser(userName);
        return ResponseEntity.ok().body(pettyCashList);
    }

    @GetMapping("/approvedWithStation")
    public ResponseEntity<List<PettyCash>> getApprovedPettyWithStation(@RequestParam String station) {
        List<PettyCash> pettyCashList = pettyCashService.getApprovedPettyCashWithStation(station);
        return ResponseEntity.ok().body(pettyCashList);
    }


    @GetMapping("/pending")
    public ResponseEntity<List<PettyCash>> getPendingPetty() {
        return ResponseEntity.ok().body(pettyCashService.getPendingPettyCash());
    }

    @GetMapping("/pendingWithUser")
    public ResponseEntity<List<PettyCash>> getPendingPettyWithUser(@RequestParam String userName) {
        List<PettyCash> pettyCashList = pettyCashService.getPendingPettyCashWithUser(userName);
        return ResponseEntity.ok().body(pettyCashList);
    }

    @GetMapping("/pendingWithStation")
    public ResponseEntity<List<PettyCash>> getPendingPettyWithStation(@RequestParam String station) {
        List<PettyCash> pettyCashList = pettyCashService.getPendingPettyCashWithStation(station);
        return ResponseEntity.ok().body(pettyCashList);
    }

    @GetMapping("/suspended")
    public ResponseEntity<List<PettyCash>> getSuspendedPetty() {
        return ResponseEntity.ok().body(pettyCashService.getSuspendedPettyCash());
    }

    @GetMapping("/suspendedWithUser")
    public ResponseEntity<List<PettyCash>> getSuspendedPettyWithUser(@RequestParam String userName) {
        List<PettyCash> pettyCashList = pettyCashService.getSuspendPettyCashWithUser(userName);
        return ResponseEntity.ok().body(pettyCashList);
    }

    @GetMapping("/suspendedWithStation")
    public ResponseEntity<List<PettyCash>> getSuspendedPettyWithStation(@RequestParam String station) {
        List<PettyCash> pettyCashList = pettyCashService.getSuspendPettyCashWithStation(station);
        return ResponseEntity.ok().body(pettyCashList);
    }

    @GetMapping("/declined")
    public ResponseEntity<List<PettyCash>> getDeclinedPetty() {
        return ResponseEntity.ok().body(pettyCashService.getDeclinedPettyCash());
    }

    @GetMapping("/declinedWithUser")
    public ResponseEntity<List<PettyCash>> getDeclinedPettyWithUser(@RequestParam String userName) {
        List<PettyCash> pettyCashList = pettyCashService.getDeclinedPettyCashWithUser(userName);
        return ResponseEntity.ok().body(pettyCashList);
    }

    @GetMapping("/declinedWithStation")
    public ResponseEntity<List<PettyCash>> getDeclinedPettyWithStation(@RequestParam String station) {
        List<PettyCash> pettyCashList = pettyCashService.getDeclinedPettyCashWithStation(station);
        return ResponseEntity.ok().body(pettyCashList);
    }

    @GetMapping("/approvedBetween")
    public ResponseEntity<List<PettyCash>> getApprovedPettyBetween(@RequestParam LocalDate startDate,
                                                                   @RequestParam LocalDate endDate,
                                                                   @RequestParam String station) {
        return ResponseEntity.ok().body(pettyCashService.getApprovedPettyCashBetweenDates(startDate, endDate, station));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PettyCash>> getRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.ok().body(pettyCashService.getRequestsByStatus(status));
    }


    @PutMapping("/status/{id}")
    public ResponseEntity<PettyCash> updateStatus(@PathVariable(value = "id") String id, @RequestBody PettyCash pettyCash) {
        return ResponseEntity.ok().body(pettyCashService.updateStatus(id, pettyCash));
    }


}
