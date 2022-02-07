package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "color")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Color {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 128)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "name", nullable = false,columnDefinition = "nvarchar")
    private String name;

}
