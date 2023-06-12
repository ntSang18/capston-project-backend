package com.capstone.backend.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddressDto(
    String province,
    String district,
    String ward,
    @JsonProperty("specific_address") String specificAddress) {

}
