package com.kodsonApp.resource;

import com.kodsonApp.domain.Attendants;
import com.kodsonApp.service.AttendantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/attendants"})
@RequiredArgsConstructor
public class AttendantsResource {
    private final AttendantsService attendantsService;

    @PostMapping
    public ResponseEntity<Attendants> createContact(@RequestBody Attendants bdc) {
        System.out.println(bdc.getName()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/attendants/attendantsID")).body(attendantsService.createBdc(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<Attendants>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(attendantsService.getAllAttendants(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendants> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(attendantsService.getBdc(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Attendants>> getAttendanceByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(attendantsService.getAttendantByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        attendantsService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }


}
