package com.kodsonApp.resource;

import com.kodsonApp.domain.BdcPms;
import com.kodsonApp.service.BdcPmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = { "/","mobik/bdcpms"})
@RequiredArgsConstructor
public class BdcPmsResource {
    private final BdcPmsService bdcPmsService;

    @PostMapping
    public ResponseEntity<BdcPms> createContact(@RequestBody BdcPms bdc) {
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/bdcpms/bdcpmsID")).body(bdcPmsService.createBdcpms(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<BdcPms>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "40") int size) {
        return ResponseEntity.ok().body(bdcPmsService.getAllBdcPmss(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BdcPms> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(bdcPmsService.getBdcPms(id));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
//        bdcPmsService.deleteExpense(id);
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping("/{id}")
    public ResponseEntity<BdcPms> updateBdcPms(@PathVariable(value = "id") String id, @RequestBody BdcPms bdc) {
        return ResponseEntity.ok().body(bdcPmsService.updateBdcPms(id, bdc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBdcPms(@PathVariable(value = "id") String id) {
        bdcPmsService.deleteBdcPms(id);
        return ResponseEntity.noContent().build();
    }
}
