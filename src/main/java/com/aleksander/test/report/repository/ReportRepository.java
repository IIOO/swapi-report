package com.aleksander.test.report.repository;

import com.aleksander.test.report.persistance.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> { }
