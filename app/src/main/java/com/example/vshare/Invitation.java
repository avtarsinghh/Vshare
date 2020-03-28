package com.example.vshare;

public class Invitation {
    private User receiver;
    private String status = "Pending";
    private String movie = "";
    private User sender;
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }



    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }


    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
