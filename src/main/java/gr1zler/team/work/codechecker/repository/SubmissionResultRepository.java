package gr1zler.team.work.codechecker.repository;

import gr1zler.team.work.codechecker.model.SubmissionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionResultRepository extends JpaRepository<SubmissionResult,Long> {
}
