package com.abol.abol.app.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "files")
@ToString
public class CustomFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    public long TimeMills;
    private String fileName;
    private String fileDownloadUri;
    private String author;
    private Integer size;
}
