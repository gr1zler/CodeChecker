package gr1zler.team.work.codechecker.repository;

import gr1zler.team.work.codechecker.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase,Long> {
}
