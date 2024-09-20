package com.kodsonApp.service;

import com.kodsonApp.domain.Dippings;
import com.kodsonApp.repository.DippingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DippingsService {

    @Autowired
    private DippingsRepo dippingsRepo;

    public List<Dippings> getAllDippings() {
        return dippingsRepo.findAll();
    }

    public Optional<Dippings> getDippingById(String id) {
        return dippingsRepo.findById(id);
    }

    public Dippings saveDipping(Dippings dipping) {
        return dippingsRepo.save(dipping);
    }

    public Dippings updateDipping(String id, Dippings dipping) {
        Optional<Dippings> existingDipping = dippingsRepo.findById(id);
        if (existingDipping.isPresent()) {
            dipping.setId(id);
            return dippingsRepo.save(dipping);
        }
        return null;  // handle properly
    }

    public void deleteDipping(String id) {
        dippingsRepo.deleteById(id);
    }
}
