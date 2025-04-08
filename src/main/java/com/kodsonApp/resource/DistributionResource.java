package com.kodsonApp.resource;
import com.kodsonApp.domain.Distribution;
import com.kodsonApp.service.DistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/distribution"})
@RequiredArgsConstructor
public class DistributionResource {
    private final DistributionService distributionService;

    @PostMapping
    public ResponseEntity<Distribution> createContact(@RequestBody Distribution distribution) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/distribution/disID")).body(distributionService.createDis(distribution));
    }

    @GetMapping
    public ResponseEntity<Page<Distribution>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(distributionService.getAllDis(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Distribution> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(distributionService.getDis(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Distribution>> getDistributionsByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(distributionService.getDistributionsByStation(station));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Distribution> updateDistribution(@PathVariable(value = "id") String id,
                                                           @RequestBody Distribution distribution) {
        distribution.setId(id);
        return ResponseEntity.ok().body(distributionService.updateDis(distribution));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable(value = "id") String id) {
        distributionService.deleteDis(id);
        return ResponseEntity.noContent().build();
    }
}
