package com.aleksander.test.report.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {
    @Id
    private Long id;

    @NotNull
    @Length(max = 50)
    private String queryCriteriaCharacterPhrase;

    @NotNull
    @Length(max = 50)
    private String queryCriteriaPlanetName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "result_id")
    private Set<FilmEntryEntity> result;

}
