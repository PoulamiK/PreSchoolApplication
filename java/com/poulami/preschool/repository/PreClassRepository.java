package com.poulami.preschool.repository;

import com.poulami.preschool.model.PreClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreClassRepository extends JpaRepository<PreClass, Integer> {
}
