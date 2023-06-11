package com.mosaic.server.controller;

import com.mosaic.server.service.MosaicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/api")
public class ApiController {

    private final MosaicService service;
    @Autowired
    public ApiController(MosaicService service) {
        this.service = service;
    }
    @RequestMapping(path = "/tiles/{name}/{z}/{x}/{y}/data")
    @CrossOrigin(origins = "*")
    public ResponseEntity<byte[]> getTileData(@PathVariable("name") String name,
                                              @PathVariable("z") int z,
                                              @PathVariable("x") int x,
                                              @PathVariable("y") int y) {


        return new ResponseEntity<>(service.getTileData(name, z, x, y), HttpStatus.OK);

    }

}
