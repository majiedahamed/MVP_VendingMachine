package com.mvp.vending.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Majied on 05/10/2021.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MachineNotFoundException extends RuntimeException {
    /**
     * Machine not found exception
     *
     * @param machineId the machine name
     */
    public MachineNotFoundException(String machineId) {
        super("could not find machine '" + machineId + "'.");
    }
}
