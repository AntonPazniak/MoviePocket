package com.moviePocket.entities.image;

import com.moviePocket.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "images")
public class ImageEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;

}
