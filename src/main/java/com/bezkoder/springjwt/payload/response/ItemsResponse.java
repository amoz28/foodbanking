package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Items;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class ItemsResponse {

    private int status;

    private List<Items> message;

    public ItemsResponse(int status, List<Items> message) {
        this.status = status;
        this.message = message;
    }
}
