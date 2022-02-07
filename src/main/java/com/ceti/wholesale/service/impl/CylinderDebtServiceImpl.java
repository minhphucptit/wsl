package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.model.CylinderDebt;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.service.CylinderDebtService;

@Service
public class CylinderDebtServiceImpl implements CylinderDebtService {

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Override
    public void createCylinderDebt(CylinderDebt cylinderDebt) {
        cylinderDebtRepository.save(cylinderDebt);
    }
}
