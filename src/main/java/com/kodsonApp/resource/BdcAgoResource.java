package com.kodsonApp.resource;

import com.kodsonApp.domain.BdcAgo;
import com.kodsonApp.service.BdcAgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = { "/","mobik/bdcago"})
@RequiredArgsConstructor
public class BdcAgoResource {
    private final BdcAgoService bdcAgoService;

    @PostMapping
    public ResponseEntity<BdcAgo> createContact(@RequestBody BdcAgo bdc) {
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/bdcago/bdcID")).body(bdcAgoService.createBdcpms(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<BdcAgo>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "40") int size) {
        return ResponseEntity.ok().body(bdcAgoService.getAllBdcAgo(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BdcAgo> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(bdcAgoService.getBdcPms(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BdcAgo> updateBdc(@PathVariable(value = "id") String id, @RequestBody BdcAgo bdcDetails) {
        return ResponseEntity.ok().body(bdcAgoService.updateBdcAgo(id, bdcDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBdc(@PathVariable(value = "id") String id) {
        bdcAgoService.deleteBdcAgo(id);
        return ResponseEntity.noContent().build();
    }
}
