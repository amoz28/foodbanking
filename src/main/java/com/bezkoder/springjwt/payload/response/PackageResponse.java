package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Packages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class PackageResponse {

    private int status;

    private List<Packages> message;

    public PackageResponse(int status, List<Packages> message) {
        this.status = status;
        this.message = message;
    }
}

