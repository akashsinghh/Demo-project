package com.akashsyncr.Demo.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue  //(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Image() {
    }



    public Image(Long id, String name, byte[] data, User user) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.user = user;
    }
// getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

