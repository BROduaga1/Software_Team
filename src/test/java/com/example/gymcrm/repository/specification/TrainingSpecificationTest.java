package com.example.gymcrm.repository.specification;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TrainingSpecificationTest {
    @Mock
    private Root<Training> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private Predicate predicate;
    @Mock
    private Path<LocalDateTime> datePath;
    @Mock
    private Path<String> usernamePath;
    @Mock
    private Path<Training> traineePath;
    @Mock
    private Path<Training> trainerPath;
    @Mock
    private Path<Training> userPath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void commonSetup() {
        lenient().doReturn(traineePath).when(root).get("trainee");
        lenient().doReturn(trainerPath).when(root).get("trainer");
        lenient().doReturn(userPath).when(traineePath).get("user");
        lenient().doReturn(userPath).when(trainerPath).get("user");
        lenient().doReturn(usernamePath).when(userPath).get("username");
        lenient().doReturn(predicate).when(cb).equal(usernamePath, TestData.createSearchDto().getUsername());
        lenient().doReturn(predicate).when(cb).and(any(Predicate[].class));
    }

    @Test
    void shouldBuildSpecificationWithUsernameAndIsTrainee() {
        TrainingSearchDto dto = TestData.createSearchDto();

        commonSetup();

        Specification<Training> specification = TrainingSpecification.buildSpecification(dto);
        Predicate result = specification.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
        verify(cb, times(1)).equal(usernamePath, dto.getUsername());
    }

    @Test
    void shouldBuildSpecificationWithFromDate() {
        TrainingSearchDto dto = TestData.createSearchDto();
        dto.setUsername(null);
        dto.setIsTrainee(null);

        lenient().doReturn(datePath).when(root).get("date");
        lenient().doReturn(predicate).when(cb).greaterThanOrEqualTo(datePath, dto.getFromDate());
        lenient().doReturn(predicate).when(cb).and(any(Predicate[].class));

        Specification<Training> specification = TrainingSpecification.buildSpecification(dto);
        Predicate result = specification.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
        verify(cb, times(1)).greaterThanOrEqualTo(datePath, dto.getFromDate());
    }

    @Test
    void shouldBuildSpecificationWithToDate() {
        TrainingSearchDto dto = TestData.createSearchDto();
        dto.setUsername(null);
        dto.setIsTrainee(null);

        lenient().doReturn(datePath).when(root).get("date");
        lenient().doReturn(predicate).when(cb).lessThanOrEqualTo(datePath, dto.getToDate());
        lenient().doReturn(predicate).when(cb).and(any(Predicate[].class));

        Specification<Training> specification = TrainingSpecification.buildSpecification(dto);
        Predicate result = specification.toPredicate(root, query, cb);

        assertThat(result).isNotNull();
        verify(cb, times(1)).lessThanOrEqualTo(datePath, dto.getToDate());
    }
}
